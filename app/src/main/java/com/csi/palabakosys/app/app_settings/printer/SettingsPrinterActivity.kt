package com.csi.palabakosys.app.app_settings.printer

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.csi.palabakosys.R
import com.csi.palabakosys.app.app_settings.printer.browser.PrinterDevice
import com.csi.palabakosys.app.app_settings.printer.browser.SettingsPrinterBrowserActivity
import com.csi.palabakosys.databinding.ActivitySettingsPrinterBinding
import com.csi.palabakosys.services.PrinterService
import com.csi.palabakosys.services.PrinterService.Companion.CANCEL_PRINT_ACTION
import com.csi.palabakosys.services.PrinterService.Companion.CUSTOM_SETTINGS
import com.csi.palabakosys.services.PrinterService.Companion.MESSAGE
import com.csi.palabakosys.services.PrinterService.Companion.PAYLOAD_TEXT
import com.csi.palabakosys.services.PrinterService.Companion.PRINT_ERROR_ACTION
import com.csi.palabakosys.services.PrinterService.Companion.PRINT_FINISHED_ACTION
import com.csi.palabakosys.services.PrinterService.Companion.PRINT_STARTED_ACTION
import com.csi.palabakosys.util.ActivityLauncher
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsPrinterActivity : AppCompatActivity() {
    companion object {
        const val ACTION_BROWSE_PRINTER_DEVICES = "browse_printer_devices"

        const val PRINTER_DEVICE_EXTRA = "printer_device"
    }

    private val viewModel: PrinterSettingsViewModel by viewModels()


    private lateinit var binding: ActivitySettingsPrinterBinding

    private val launcher = ActivityLauncher(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings_printer)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        subscribeEvents()
        subscribeListeners()
    }

    private val receiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if(action == PRINT_STARTED_ACTION) {
                binding.buttonTest.visibility = View.GONE
            } else if(action == PRINT_FINISHED_ACTION) {
                binding.buttonTest.visibility = View.VISIBLE
            } else if(action == PRINT_ERROR_ACTION) {
                binding.buttonTest.visibility = View.VISIBLE
                val message = intent.getStringExtra(PrinterService.MESSAGE) ?: "Something went wrong"
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(PRINT_STARTED_ACTION).apply {
            addAction(PRINT_FINISHED_ACTION)
            addAction(PRINT_ERROR_ACTION)
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelPrint()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    private fun subscribeEvents() {

        binding.textPrinterName.setOnClickListener {
            viewModel.openPrinterBrowser()
        }
        launcher.onOk = {
            when(it.data?.action) {
                ACTION_BROWSE_PRINTER_DEVICES -> {
                    it.data?.getParcelableExtra<PrinterDevice>(PRINTER_DEVICE_EXTRA)?.let { printerDevice ->
                        viewModel.setPrinterDevice(printerDevice)
                    }
                }
            }
        }
        binding.buttonTest.setOnClickListener {
            viewModel.testPrint()
        }
        binding.buttonSave.setOnClickListener {
            viewModel.update()
        }
    }

    private fun subscribeListeners() {
        viewModel.dataState.observe(this, Observer {
            when (it) {
                is PrinterSettingsViewModel.DataState.OpenPrinterBrowser -> {
                    val intent = Intent(this, SettingsPrinterBrowserActivity::class.java).apply {
                        action = ACTION_BROWSE_PRINTER_DEVICES
                        putExtra(PRINTER_DEVICE_EXTRA, it.currentPrinter)
                    }
                    launcher.launch(intent)
                    viewModel.resetState()
                }
                is PrinterSettingsViewModel.DataState.Save -> {
                    viewModel.resetState()
                    finish()
                }
                is PrinterSettingsViewModel.DataState.StartTestPrint -> {
                    val intent = Intent(this, PrinterService::class.java).apply {
                        putExtra(PAYLOAD_TEXT, it.payload)
                        putExtra(CUSTOM_SETTINGS, it.settings)
                    }
                    println("start test print")
                    startForegroundService(intent)
                    viewModel.resetState()
                }
            }
        })
    }

    private fun cancelPrint() {
        val intent = Intent(this, PrinterService::class.java).apply {
            action = CANCEL_PRINT_ACTION
        }
        startForegroundService(intent)
    }
}