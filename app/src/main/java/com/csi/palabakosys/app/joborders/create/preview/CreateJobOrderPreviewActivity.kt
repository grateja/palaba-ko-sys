package com.csi.palabakosys.app.joborders.create.preview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.csi.palabakosys.R
import com.csi.palabakosys.databinding.ActivityJobOrderPreviewBinding
import com.csi.palabakosys.viewmodels.BluetoothPrinterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateJobOrderPreviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJobOrderPreviewBinding
    private val printerViewModel: BluetoothPrinterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_job_order_preview)

        subscribeEvents()

        printerViewModel.initializeBluetooth(this)
//        printerViewModel.loadPrinters()
    }

    private fun subscribeEvents() {
        binding.buttonFinish.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }

        binding.buttonLabel.setOnClickListener {
            printLabel()
        }
    }

    private fun printLabel() {
//        printerViewModel.loadPrinters()
        printerViewModel.selectPrinter()
    }
}