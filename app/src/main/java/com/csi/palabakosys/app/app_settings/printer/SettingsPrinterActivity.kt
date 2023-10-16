package com.csi.palabakosys.app.app_settings.printer

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.app.app_settings.printer.browser.PrinterDevice
import com.csi.palabakosys.app.app_settings.printer.browser.SettingsPrinterBrowserActivity
import com.csi.palabakosys.databinding.ActivitySettingsPrinterBinding
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


    private fun subscribeEvents() {
        binding.cardPrinterBrowser.setOnClickListener {
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
            }
        })
    }
}