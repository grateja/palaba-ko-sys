package com.csi.palabakosys.app.joborders.print

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.app_settings.printer.SettingsPrinterActivity
import com.csi.palabakosys.app.app_settings.printer.SettingsPrinterActivity.Companion.PRINTER_DEVICE_EXTRA
import com.csi.palabakosys.app.app_settings.printer.browser.PrinterDevice
import com.csi.palabakosys.app.app_settings.printer.browser.SettingsPrinterBrowserActivity
import com.csi.palabakosys.databinding.ActivityJobOrderPrintBinding
import com.csi.palabakosys.model.EnumPrintState
import com.csi.palabakosys.model.PrintItem
import com.csi.palabakosys.services.PrinterService
import com.csi.palabakosys.util.ActivityLauncher
import com.csi.palabakosys.util.BluetoothPrinterHelper
import com.csi.palabakosys.util.Constants
import com.csi.palabakosys.util.toUUID
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobOrderPrintActivity : AppCompatActivity() {
    private val viewModel: JobOrderPrintViewModel by viewModels()
    private lateinit var binding: ActivityJobOrderPrintBinding

    private val helper = BluetoothPrinterHelper(this)

    private val joDetailsAdapter = Adapter<PrintItem>(R.layout.recycler_item_print_item)
    private val itemsAdapter = Adapter<PrintItem>(R.layout.recycler_item_print_item)
    private val summaryAdapter = Adapter<PrintItem>(R.layout.recycler_item_print_item)
    private val paymentAdapter = Adapter<PrintItem>(R.layout.recycler_item_print_item)

    private val launcher = ActivityLauncher(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_job_order_print)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        subscribeEvents()
        subscribeListeners()

        setupTab()

        intent.getStringExtra(Constants.ID)?.toUUID()?.let {
            viewModel.setJobOrderId(it)
        }

        binding.recyclerJoDetails.adapter = joDetailsAdapter
        binding.recyclerItems.adapter = itemsAdapter
        binding.recyclerSummary.adapter = summaryAdapter
        binding.recyclerPayment.adapter = paymentAdapter
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

    private fun setupTab() {
        binding.printTab.apply {
            addTab(newTab().setText("CLAIM STUB"))
            addTab(newTab().setText("MACHINE STUB"))
            addTab(newTab().setText("JOB ORDER"))
        }
    }

    private fun subscribeEvents() {
        binding.buttonPrint.setOnClickListener {
            viewModel.print(binding.printTab.getTabAt(binding.printTab.selectedTabPosition)?.text.toString())
        }
        binding.printTab.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.selectTab(tab?.text.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) { }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                viewModel.selectTab(tab?.text.toString())
            }
        })
        helper.apply {
            setOnBluetoothStateChanged {
                viewModel.setBluetoothState(it)
            }
        }
        binding.buttonEnableBluetooth.setOnClickListener {
            helper.enableBluetooth()
        }
        binding.controls.setOnClickListener {
            val intent = Intent(this, SettingsPrinterBrowserActivity::class.java).apply {
                action = SettingsPrinterActivity.ACTION_BROWSE_PRINTER_DEVICES
            }
            launcher.launch(intent)
        }
        launcher.onOk = {
            it.data?.getParcelableExtra<PrinterDevice>(PRINTER_DEVICE_EXTRA)?.let {
                viewModel.setDevice(it)
            }
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
            when(it) {
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
        viewModel.joDetails.observe(this, Observer {
            joDetailsAdapter.setData(it)
        })
        viewModel.items.observe(this, Observer {
            itemsAdapter.setData(it)
        })
        viewModel.summary.observe(this, Observer {
            summaryAdapter.setData(it)
        })
        viewModel.paymentDetails.observe(this, Observer {
            paymentAdapter.setData(it)
        })
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action) {
                PrinterService.PRINT_ACTION -> {
                    intent.getParcelableExtra<EnumPrintState>(PrinterService.PRINT_STATE)?.let {
                        viewModel.setPrintState(it)
                    }
                }
//                PrinterService.PRINT_STARTED_ACTION -> {
//                    binding.buttonPrint.setText(R.string.cancel)
//                }
//                PrinterService.PRINT_FINISHED_ACTION -> {
//                    binding.buttonPrint.setText(R.string.Continue)
//                }
            }
        }
    }
}