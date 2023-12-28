package com.csi.palabakosys.app.joborders.print

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.app_settings.printer.SettingsPrinterActivity
import com.csi.palabakosys.app.app_settings.printer.SettingsPrinterActivity.Companion.PRINTER_DEVICE_EXTRA
import com.csi.palabakosys.app.app_settings.printer.browser.PrinterDevice
import com.csi.palabakosys.databinding.ActivityJobOrderPrintBinding
import com.csi.palabakosys.model.EnumPrintState
import com.csi.palabakosys.model.PrintItem
import com.csi.palabakosys.services.PrinterService
import com.csi.palabakosys.util.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Float.min

@AndroidEntryPoint
class JobOrderPrintActivity : AppCompatActivity() {

    companion object {
        const val TAB_CLAIM_STUB = "Claim Stub"
        const val TAB_JOB_ORDER = "Job Order"
        const val TAB_MACHINE_STUB = "Machine Stub"
    }

    private val viewModel: JobOrderPrintViewModel by viewModels()
    private lateinit var binding: ActivityJobOrderPrintBinding
    private val helper = BluetoothPrinterHelper(this)
    private val itemsAdapter = Adapter<PrintItem>(R.layout.recycler_item_print_item)
    private val launcher = ActivityLauncher(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_job_order_print)

        setSupportActionBar(binding.toolbar)

        binding.apply {
            viewModel = this@JobOrderPrintActivity.viewModel
            lifecycleOwner = this@JobOrderPrintActivity
            recyclerItems.adapter = itemsAdapter
        }

        subscribeEvents()
        subscribeListeners()

        intent.getStringExtra(Constants.ID)?.toUUID()?.let(viewModel::setJobOrderId)
    }

    private fun getWidth(charactersPerLine: Int): Int {
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels.toFloat()
        val characterWidthInDp = 18f
        val paint = Paint().apply {
            textSize = characterWidthInDp.spToPx()
            typeface = Typeface.MONOSPACE
        }
        val textWidth = paint.measureText("w".repeat(charactersPerLine))
        return min(textWidth, screenWidth).toInt()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tools_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_options) {
            openPrinterSettings()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openPrinterSettings() {
        val intent = Intent(this, SettingsPrinterActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        helper.registerReceiver(this)
        val filter = IntentFilter().apply {
            addAction(PrinterService.PRINT_ACTION)
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter)
    }

    override fun onPause() {
        super.onPause()
        helper.unregisterReceiver()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    private fun subscribeEvents() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_claim_stub -> {
                    viewModel.selectTab(TAB_CLAIM_STUB)
                    true
                }
                R.id.menu_job_order -> {
                    viewModel.selectTab(TAB_JOB_ORDER)
                    true
                }
                R.id.menu_machine_stub -> {
                    viewModel.selectTab(TAB_MACHINE_STUB)
                    true
                }
                else -> false
            }
        }

        helper.setOnBluetoothStateChanged {
            viewModel.setBluetoothState(it)
        }

        launcher.onOk = {
            it.data?.getParcelableExtra<PrinterDevice>(PRINTER_DEVICE_EXTRA)?.let(viewModel::setDevice)
        }
    }

    private fun print(formattedText: String) {
        val intent = Intent(this, PrinterService::class.java).apply {
            putExtra(PrinterService.PAYLOAD_TEXT, formattedText)
        }
        startForegroundService(intent)
    }

    private fun cancel() {
        val intent = Intent(this, PrinterService::class.java).apply {
            action = PrinterService.CANCEL_PRINT_ACTION
        }
        startForegroundService(intent)
    }

    private fun subscribeListeners() {
        viewModel.dataState.observe(this, Observer {
            when (it) {
                is JobOrderPrintViewModel.DataState.Submit -> {
                    print(it.formattedText)
                    viewModel.resetState()
                }
                is JobOrderPrintViewModel.DataState.Cancel -> {
                    cancel()
                    viewModel.resetState()
                }
            }
        })

        viewModel.characterLength.observe(this, Observer {
            binding.wrapper.layoutParams.width = getWidth(it)
        })

        viewModel.items.observe(this, Observer {
            itemsAdapter.setData(it)
        })
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                PrinterService.PRINT_ACTION -> handlePrintAction(intent)
            }
        }
    }

    private fun handlePrintAction(intent: Intent) {
        intent.getParcelableExtra<EnumPrintState>(PrinterService.PRINT_STATE)?.let { printState ->
            viewModel.setPrintState(printState)

            if (printState == EnumPrintState.ERROR) {
                intent.getStringExtra(PrinterService.MESSAGE)?.let { errorMessage ->
                    showErrorMessage(errorMessage)
                }
            }
        }
    }

    private fun showErrorMessage(errorMessage: String) {
        binding.root.showSnackBar(errorMessage, Snackbar.LENGTH_SHORT)
    }
}
