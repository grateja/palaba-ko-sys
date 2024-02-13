package com.csi.palabakosys.app.joborders.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.OptionsAdapter
import com.csi.palabakosys.app.auth.AuthActionDialogActivity
import com.csi.palabakosys.app.auth.LoginCredentials
import com.csi.palabakosys.app.joborders.create.JobOrderCreateActivity
import com.csi.palabakosys.app.joborders.payment.cashless.PaymentJoCashlessModalFragment
import com.csi.palabakosys.databinding.ActivityJobOrderPaymentBinding
import com.csi.palabakosys.model.EnumPaymentMethod
import com.csi.palabakosys.util.*
import com.sangcomz.fishbun.util.setStatusBarColor
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
    private val fragment = BottomSheetJobOrderPaymentFragment()
//    private lateinit var cashlessModalFragment: PaymentJoCashlessModalFragment
    private val adapter = JobOrderListPaymentAdapter(false)

//    private val paymentMethodAdapter = OptionsAdapter(
//        R.layout.recycler_item_payment_method_option,
//        EnumPaymentMethod.values(),
//        R.color.color_code_payments
//    )

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

        setStatusBarColor(resources.getColor(R.color.color_code_payments, null))

        subscribeEvents()
        subscribeListeners()

        val paymentId = intent.getStringExtra(PAYMENT_ID).toUUID()
        val customerId = intent.getStringExtra(CUSTOMER_ID).toUUID()

        if(paymentId != null) {
            viewModel.getPayment(paymentId)
        } else if(customerId != null) {
            viewModel.getUnpaidByCustomerId(customerId)
        }

//        binding.textInputCashReceived.selectAllOnFocus()

//        binding.recyclerOptionPaymentMethod.layoutManager = GridLayoutManager(this, 2)
//        binding.recyclerOptionPaymentMethod.adapter = paymentMethodAdapter
    }

    private fun auth(action: String, message: String) {
        val intent = Intent(this, AuthActionDialogActivity::class.java).apply {
            this.action = action
            putExtra(AuthActionDialogActivity.MESSAGE, message)
        }
        authLauncher.launch(intent)
    }

    private fun subscribeEvents() {
        binding.cardPaymentOptionCash.setOnClickListener {
            viewModel.setPaymentMethod(EnumPaymentMethod.CASH)
            fragment.show(supportFragmentManager, null)
        }
        binding.cardPaymentOptionCashless.setOnClickListener {
            viewModel.setPaymentMethod(EnumPaymentMethod.CASHLESS)
            fragment.show(supportFragmentManager, null)
        }
        binding.cardDatePaid.setOnClickListener {
            auth(AUTH_REQUEST_MODIFY_DATE_ACTION, "Modification of date paid requires authentication!")
        }
//        paymentMethodAdapter.onSelect = {
//            viewModel.setPaymentMethod(it)
//            BottomSheetJobOrderPaymentFragment().show(supportFragmentManager, null)
//        }
        adapter.onSelectionChange = {
            viewModel.selectItem(it)
        }
        adapter.onItemClick = {
            val intent = Intent(this, JobOrderCreateActivity::class.java).apply {
                action = JobOrderCreateActivity.ACTION_LOAD_BY_JOB_ORDER_ID
                putExtra(JobOrderCreateActivity.JOB_ORDER_ID, it.id.toString())
            }
            startActivity(intent)
        }
        binding.buttonSave.setOnClickListener {
            viewModel.validate()
        }
//        binding.textDatePaid.setOnClickListener {
//            auth(AUTH_REQUEST_MODIFY_DATE_ACTION)
//        }
        dateTimePicker.setOnDateTimeSelectedListener {
            viewModel.setDateTime(it)
        }
        authLauncher.onOk = { result ->
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
//        viewModel.paymentMethod.observe(this, Observer {
//            paymentMethodAdapter.selectOption(it)
//        })
        viewModel.customer.observe(this, Observer {
            title = "${it.name} - [${it.crn}]"
        })
        viewModel.payableJobOrders.observe(this, Observer {
            adapter.setData(it)
        })
        viewModel.amountToPay.observe(this, Observer {
//            binding.textInputCashReceived.setText(it.toString())
//            binding.textInputCashlessAmount.setText(it.toString())
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
                    auth(AUTH_REQUEST_SAVE, "Saving job order requires authentication!")
                    viewModel.resetState()
                }
                is JobOrderPaymentViewModel.DataState.PaymentSuccess -> {
                    setResult(RESULT_OK, Intent().apply {
                        action = intent.action
                        putExtra(PAYMENT_ID, it.payment.id.toString())
                        putExtra(SELECTED_JOB_ORDER_IDS, it.jobOrderIds)
                    })
                    viewModel.resetState()
                    finish()
                }
                is JobOrderPaymentViewModel.DataState.InvalidOperation -> {
                    showDialog(it.message)
//                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                    viewModel.resetState()
                }
                is JobOrderPaymentViewModel.DataState.InvalidInput -> {
                    fragment.show(supportFragmentManager, null)
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