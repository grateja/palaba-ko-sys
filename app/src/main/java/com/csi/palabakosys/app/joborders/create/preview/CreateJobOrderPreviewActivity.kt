package com.csi.palabakosys.app.joborders.create.preview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.csi.palabakosys.R
import com.csi.palabakosys.app.joborders.payment.PaymentJoPreviewActivity
import com.csi.palabakosys.databinding.ActivityJobOrderPreviewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CreateJobOrderPreviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJobOrderPreviewBinding
//    private val printerViewModel: BluetoothPrinterViewModel by viewModels()

    private val viewModel: JobOrderPreviewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_job_order_preview)

        subscribeEvents()
        subscribeListeners()

        intent.getStringExtra("jobOrderId")?.let {
            try {
                viewModel.setJobOrderId(UUID.fromString(it))
            } catch (e: Exception) {
                finish()
            }
        }

        intent.getStringExtra("customerId")?.let {
            try {
                viewModel.setCustomerId(UUID.fromString(it))
            } catch (e: Exception) {
                finish()
            }
        }

//        printerViewModel.initializeBluetooth(this)
//        printerViewModel.loadPrinters()
    }

    private fun subscribeListeners() {
        viewModel.dataState.observe(this, androidx.lifecycle.Observer {
            when(it) {
                is JobOrderPreviewViewModel.DataState.OpenPayment -> {
                    val intent = Intent(applicationContext, PaymentJoPreviewActivity::class.java).apply {
                        putExtra("customerId", it.customerId.toString())
                    }
                    startActivity(intent)
                }
            }
        })
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
//        printerViewModel.selectPrinter()
    }
}