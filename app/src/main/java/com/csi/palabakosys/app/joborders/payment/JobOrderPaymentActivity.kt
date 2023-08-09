package com.csi.palabakosys.app.joborders.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.app.auth.AuthActionDialogActivity
import com.csi.palabakosys.app.auth.LoginCredentials
import com.csi.palabakosys.app.joborders.payment.cashless.PaymentJoCashlessModalFragment
import com.csi.palabakosys.databinding.ActivityJobOrderPaymentBinding
import com.csi.palabakosys.util.ActivityLauncher
import com.csi.palabakosys.util.DateTimePicker
import com.csi.palabakosys.util.showSnackBar
import com.csi.palabakosys.util.toUUID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobOrderPaymentActivity : AppCompatActivity() {

    companion object {
        const val CUSTOMER_ID = "customer_id"
        const val PAYMENT_ID = "payment_id"
        const val SELECTED_JOB_ORDER_IDS = "jobOrderIds"
        const val AUTH_REQUEST_MODIFY_DATE_ACTION = "modifyDate"
        const val AUTH_REQUEST_SAVE = "save"
    }

    private lateinit var binding: ActivityJobOrderPaymentBinding
    private val viewModel: JobOrderPaymentViewModel by viewModels()
    private lateinit var cashlessModalFragment: PaymentJoCashlessModalFragment
    private val adapter = JobOrderListPaymentAdapter()

    private val authLauncher = ActivityLauncher(this)
    private val dateTimePicker: DateTimePicker by lazy {
        DateTimePicker(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_job_order_payment)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.recyclerJobOrderPaymentMinimal.adapter = adapter

        subscribeEvents()
        subscribeListeners()

        val paymentId = intent.getStringExtra(PAYMENT_ID).toUUID()
        val customerId = intent.getStringExtra(CUSTOMER_ID).toUUID()

        if(paymentId != null) {
            viewModel.getPayment(paymentId)
        } else {
            viewModel.getUnpaidByCustomerId(customerId)
        }

//        val suggestions = arrayOf("G-Cash", "PayMaya", "BPI", "BDO", "Cheque")
//        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, suggestions)
//
//        binding.textInputCashlessProvider.setAdapter(adapter)
    }

    private fun auth(action: String) {
        val intent = Intent(this, AuthActionDialogActivity::class.java).apply {
            this.action = action
        }
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
        binding.textDatePaid.setOnClickListener {
            auth(AUTH_REQUEST_MODIFY_DATE_ACTION)
        }
//        binding.buttonOpenCashless.setOnClickListener {
//            viewModel.openCashless()
//        }
        dateTimePicker.setOnDateTimeSelectedListener {
            viewModel.setDateTime(it)
        }
        authLauncher.onOk = { result ->
//            val email = it.data?.getStringExtra(AuthActionDialogActivity.EMAIL_EXTRA)
//            val password = it.data?.getStringExtra(AuthActionDialogActivity.PASSWORD_EXTRA)
            when(result.data?.action) {
                AUTH_REQUEST_SAVE -> {
                    result.data?.getParcelableExtra<LoginCredentials>(AuthActionDialogActivity.RESULT)?.let {
                        viewModel.save(it.userId)
                    }
                }
                AUTH_REQUEST_MODIFY_DATE_ACTION -> {
                    viewModel.requestModifyDateTime()
                }
            }
        }
    }

    private fun subscribeListeners() {
        viewModel.cashlessProviders.observe(this, Observer {
            val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_dropdown_item_1line, it)
            binding.textInputCashlessProvider.setAdapter(adapter)
        })
        viewModel.payableJobOrders.observe(this, Observer {
            adapter.setData(it)
        })
        viewModel.amountToPay.observe(this, Observer {
            binding.textInputCashReceived.setText(it.toString())
            binding.textInputCashlessAmount.setText(it.toString())
        })

        viewModel.dataState.observe(this, Observer {
            when(it) {
//                is JobOrderPaymentViewModel.DataState.OpenCashless -> {
//                    cashlessModalFragment = PaymentJoCashlessModalFragment.getInstance(it.cashless)
//                    cashlessModalFragment.show(supportFragmentManager, "show cashless")
//                    cashlessModalFragment.onSubmit = { cashless ->
//                        viewModel.setCashless(cashless)
//                    }
//                    viewModel.resetState()
//                }
                is JobOrderPaymentViewModel.DataState.ValidationPassed -> {
                    auth(AUTH_REQUEST_SAVE)
                    viewModel.resetState()
                }
                is JobOrderPaymentViewModel.DataState.PaymentSuccess -> {
                    viewModel.resetState()
                    setResult(RESULT_OK, Intent().apply {
                        action = intent.action
                        putExtra(PAYMENT_ID, it.payment.id.toString())
                        putExtra(SELECTED_JOB_ORDER_IDS, it.jobOrderIds)
                    })
                    finish()
                }
                is JobOrderPaymentViewModel.DataState.InvalidOperation -> {
                    binding.root.showSnackBar(it.message)
//                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                    viewModel.resetState()
                }
                is JobOrderPaymentViewModel.DataState.RequestModifyDateTime -> {
                    dateTimePicker.show(it.dateTime)
                    viewModel.resetState()
                }
                else -> {}
            }
        })
    }
}