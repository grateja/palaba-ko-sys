package com.csi.palabakosys.app.joborders.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.app.joborders.payment.cashless.PaymentJoCashlessModalFragment
import com.csi.palabakosys.databinding.ActivityPaymentJoPreviewBinding
import com.csi.palabakosys.util.ActivityLauncher
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class PaymentJoPreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentJoPreviewBinding
    private val viewModel: JobOrderPaymentViewModel by viewModels()
    private lateinit var cashlessModalFragment: PaymentJoCashlessModalFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_jo_preview)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        subscribeEvents()
        subscribeListeners()

        intent.getStringExtra("customerId")?.let {
            try {
                getJobOrders(UUID.fromString(it))
            } catch (e: Exception) {
                finish()
            }
        }
    }

    private fun getJobOrders(customerId: UUID) {
        viewModel.getUnpaidByCustomerId(customerId)
    }

    private fun subscribeEvents() {
        binding.buttonSave.setOnClickListener {
            viewModel.save()
        }
        binding.buttonOpenCashless.setOnClickListener {
            viewModel.openCashless()
        }
    }

    private fun subscribeListeners() {
        viewModel.jobOrders.observe(this, Observer {
            println("list")
            println(it.size)
        })

        viewModel.dataState.observe(this, Observer {
            when(it) {
                is JobOrderPaymentViewModel.DataState.OpenCashless -> {
                    cashlessModalFragment = PaymentJoCashlessModalFragment.getInstance(it.cashless)
                    cashlessModalFragment.show(supportFragmentManager, "show cashless")
                    cashlessModalFragment.onSubmit = { cashless ->
                        viewModel.setCashless(cashless)
                    }
                    viewModel.resetState()
                }
            }
        })
    }
}