package com.csi.palabakosys.app.joborders.unpaid.prompt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.customers.CustomerMinimal
import com.csi.palabakosys.app.joborders.create.JobOrderCreateActivity
import com.csi.palabakosys.app.joborders.payment.JobOrderPaymentActivity
import com.csi.palabakosys.app.joborders.payment.JobOrderPaymentMinimal
import com.csi.palabakosys.databinding.ActivityJobOrdersUnpaidPromptBinding
import com.csi.palabakosys.util.ActivityLauncher
import com.sangcomz.fishbun.util.setStatusBarColor
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class JobOrdersUnpaidPromptActivity : AppCompatActivity() {
    companion object {
        const val CUSTOMER_EXTRA = "customer"
        const val OPEN_PAYMENT_ACTION = "openPayment"
    }

    private lateinit var binding: ActivityJobOrdersUnpaidPromptBinding
    private val viewModel: JobOrdersUnpaidPromptViewModel by viewModels()
    private val adapter = Adapter<JobOrderPaymentMinimal>(R.layout.recycler_item_job_order_read_only)
    private val launcher = ActivityLauncher(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        title = "Unpaid Job Orders"
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_job_orders_unpaid_prompt)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.recyclerUnpaidJobOrders.adapter = adapter

        setStatusBarColor(resources.getColor(R.color.color_code_payments, null))

        subscribeListeners()
        subscribeEvents()
    }

    override fun onResume() {
        super.onResume()

        intent.getParcelableExtra<CustomerMinimal>(CUSTOMER_EXTRA)?.let {
            viewModel.setCustomer(it)
        }
    }

    private fun subscribeEvents() {
        binding.buttonPayment.setOnClickListener {
            viewModel.openPayment()
        }
        binding.buttonContinue.setOnClickListener {
            viewModel.openJobOrder()
        }
        adapter.onItemClick = {
            previewJobOrder(it.id)
        }
        binding.buttonOk.setOnClickListener {
            finish()
        }
    }

    private fun subscribeListeners() {
        viewModel.jobOrders.observe(this, Observer {
            adapter.setData(it)
        })
//        viewModel.customer.observe(this, Observer {
//            viewModel.load(it.id)
//        })
        viewModel.dataState.observe(this, Observer {
            when(it) {
                is JobOrdersUnpaidPromptViewModel.DataState.OpenPayment -> {
                    openPayment(it.customerId)
                    viewModel.resetState()
                }
                is JobOrdersUnpaidPromptViewModel.DataState.OpenJobOrder -> {
                    openCreateJobOrderActivity(it.customer)
                    viewModel.resetState()
                }
            }
        })
        viewModel.customer.observe(this, Observer {
            title = "${it.name} - [${it.crn}]"
        })
    }

    private fun openPayment(customerId: UUID) {
        val intent = Intent(this, JobOrderPaymentActivity::class.java).apply {
            action = OPEN_PAYMENT_ACTION
            putExtra(JobOrderPaymentActivity.CUSTOMER_ID, customerId.toString())
        }
        launcher.launch(intent)
    }

    private fun openCreateJobOrderActivity(customer: CustomerMinimal) {
        val intent = Intent(this, JobOrderCreateActivity::class.java).apply {
            action = JobOrderCreateActivity.ACTION_LOAD_BY_CUSTOMER_ID
            putExtra(JobOrderCreateActivity.CUSTOMER_EXTRA, customer.id.toString())
        }
        launcher.launch(intent)
    }

    private fun previewJobOrder(jobOrderId: UUID) {
        val intent = Intent(this, JobOrderCreateActivity::class.java).apply {
            action = JobOrderCreateActivity.ACTION_LOAD_BY_JOB_ORDER_ID
            putExtra(JobOrderCreateActivity.JOB_ORDER_ID, jobOrderId.toString())
        }
        launcher.launch(intent)
    }
}