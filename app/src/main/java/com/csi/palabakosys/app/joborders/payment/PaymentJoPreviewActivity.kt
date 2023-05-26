package com.csi.palabakosys.app.joborders.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.app.auth.AuthActionDialogActivity
import com.csi.palabakosys.app.auth.LoginCredentials
import com.csi.palabakosys.app.joborders.payment.cashless.PaymentJoCashlessModalFragment
import com.csi.palabakosys.databinding.ActivityPaymentJoPreviewBinding
import com.csi.palabakosys.util.ActivityLauncher
import com.csi.palabakosys.util.toUUID
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class PaymentJoPreviewActivity : AppCompatActivity() {

    companion object {
        const val CUSTOMER_ID = "customer_id"
        const val JOB_ORDER_ID = "job_order_id"
    }

    private lateinit var binding: ActivityPaymentJoPreviewBinding
    private val viewModel: JobOrderPaymentViewModel by viewModels()
    private lateinit var cashlessModalFragment: PaymentJoCashlessModalFragment
    private val adapter = JobOrderListPaymentAdapter()

    private val authLauncher = ActivityLauncher(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_jo_preview)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.recyclerJobOrderPaymentMinimal.adapter = adapter

        subscribeEvents()
        subscribeListeners()

        intent.getStringExtra(CUSTOMER_ID).toUUID()?.let {
            getJobOrders(it)
        }
    }

    private fun getJobOrders(customerId: UUID) {
        viewModel.getUnpaidByCustomerId(customerId)
    }

    private fun prepareSubmit() {
        val intent = Intent(this, AuthActionDialogActivity::class.java)
        authLauncher.launch(intent)
    }

    private fun subscribeEvents() {
        adapter.onSelectionChange = {
            viewModel.selectItem(it)
        }
        binding.buttonSave.setOnClickListener {
            viewModel.validate()
//            prepareSubmit()
        }
        binding.buttonOpenCashless.setOnClickListener {
            viewModel.openCashless()
        }
        authLauncher.onOk = { result ->
//            val email = it.data?.getStringExtra(AuthActionDialogActivity.EMAIL_EXTRA)
//            val password = it.data?.getStringExtra(AuthActionDialogActivity.PASSWORD_EXTRA)
            result.data?.getParcelableExtra<LoginCredentials>(AuthActionDialogActivity.RESULT)?.let {
                viewModel.save(it.userId)
            }
        }
    }

    private fun subscribeListeners() {
        viewModel.payableJobOrders.observe(this, Observer {
            adapter.setData(it)
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
                is JobOrderPaymentViewModel.DataState.ValidationPassed -> {
                    prepareSubmit()
                    viewModel.resetState()
                }
                is JobOrderPaymentViewModel.DataState.PaymentSuccess -> {
                    viewModel.resetState()
                    setResult(RESULT_OK)
                    finish()
                }
                is JobOrderPaymentViewModel.DataState.InvalidOperation -> {
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                    viewModel.resetState()
                }
                else -> {}
            }
        })
    }
}