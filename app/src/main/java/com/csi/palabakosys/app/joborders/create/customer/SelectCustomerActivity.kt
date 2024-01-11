package com.csi.palabakosys.app.joborders.create.customer

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.app.customers.CustomerMinimal
import com.csi.palabakosys.app.customers.create.AddEditCustomerFragment
import com.csi.palabakosys.app.joborders.create.JobOrderCreateActivity
import com.csi.palabakosys.app.joborders.payment.JobOrderPaymentActivity.Companion.CUSTOMER_ID
import com.csi.palabakosys.app.joborders.unpaid.prompt.JobOrdersUnpaidPromptActivity
import com.csi.palabakosys.databinding.ActivitySelectCustomerBinding
import com.csi.palabakosys.util.FilterActivity
import com.csi.palabakosys.util.isToday
import com.csi.palabakosys.viewmodels.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SelectCustomerActivity : FilterActivity() {
    private val viewModel: SelectCustomerViewModel by viewModels()
    private lateinit var binding: ActivitySelectCustomerBinding
    private lateinit var customerModal: AddEditCustomerFragment
    private val customersAdapter = CustomersAdapterMinimal()

    override var filterHint = "Search Customer Name/CRN"
    override var toolbarBackground: Int = R.color.color_code_customers

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_customer)
        super.onCreate(savedInstanceState)
        title = "Search Customer"
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.recyclerCustomersMinimal.adapter = customersAdapter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        subscribeListeners()
        subscribeEvents()
    }

    override fun onResume() {
        super.onResume()
        viewModel.filter(true)
    }

    private fun subscribeListeners() {
        viewModel.dataState.observe(this, Observer {
            when(it) {
                is ListViewModel.DataState.LoadItems -> {
                    if(it.reset) {
                        customersAdapter.setData(it.items)
                    } else {
                        customersAdapter.addItems(it.items)
                    }
                }
            }
        })
    }

    private fun subscribeEvents() {
//        binding.buttonCreateNew.setOnClickListener {
//            editCustomer(null)
//            customerModal = AddEditCustomerFragment.getInstance(null)
//            customerModal.show(supportFragmentManager, "KEME")
//            customerModal.onOk = {
//                openCreateJobOrderActivity(it!!)
//            }
//        }
        customersAdapter.onItemClick = {
            selectCustomer(it.id)
//            open(it)
        }
        customersAdapter.onEdit = {
            editCustomer(it.id)
        }
        customersAdapter.onScrollAtTheBottom = {
            viewModel.loadMore()
        }
    }

    private fun selectCustomer(customerId: UUID) {
        setResult(RESULT_OK, Intent().apply {
            action = intent.action
            putExtra(CUSTOMER_ID, customerId.toString())
        })
        finish()
    }

    private fun open(customer: CustomerMinimal) {
        if(customer.unpaid != null && customer.unpaid!! > 0 && customer.lastJobOrder?.isToday() != true) {
            openUnpaidJobOrderPrompt(customer)
        } else {
            openCreateJobOrderActivity(customer)
        }
    }

    private fun editCustomer(customerId: UUID?) {
        customerModal = AddEditCustomerFragment.getInstance(customerId, searchBar?.query.toString(), false)
        customerModal.show(supportFragmentManager, "KEME")
        customerModal.onOk = {
            open(it!!)
        }
    }

    private fun openCreateJobOrderActivity(customer: CustomerMinimal) {
        val intent = Intent(this, JobOrderCreateActivity::class.java).apply {
            action = JobOrderCreateActivity.ACTION_LOAD_BY_CUSTOMER_ID
            putExtra(JobOrderCreateActivity.CUSTOMER_EXTRA, customer.id.toString())
        }
        startActivity(intent)
    }

    private fun openUnpaidJobOrderPrompt(customer: CustomerMinimal) {
        val intent = Intent(this, JobOrdersUnpaidPromptActivity::class.java).apply {
            putExtra(JobOrdersUnpaidPromptActivity.CUSTOMER_EXTRA, customer)
        }
        startActivity(intent)
    }

    override fun onQuery(keyword: String?) {
        viewModel.setKeyword(keyword)
    }
}