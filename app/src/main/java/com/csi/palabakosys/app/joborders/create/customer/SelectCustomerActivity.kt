package com.csi.palabakosys.app.joborders.create.customer

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.app.customers.CustomerMinimal
import com.csi.palabakosys.app.customers.create.AddEditCustomerFragment
import com.csi.palabakosys.app.joborders.create.JobOrderCreateActivity
import com.csi.palabakosys.databinding.ActivitySelectCustomerBinding
import com.csi.palabakosys.util.FilterActivity
import com.csi.palabakosys.viewmodels.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectCustomerActivity : FilterActivity() {
//    private var searchBar: SearchView? = null
    private val viewModel: SelectCustomerViewModel by viewModels()
    private lateinit var binding: ActivitySelectCustomerBinding
    private lateinit var customerModal: AddEditCustomerFragment
    private val customersAdapter = CustomersAdapterMinimal()

    override var filterHint = "Search Customer Name/CRN"
    override var enableAdvancedSearch = true
    override fun onAdvancedSearchClicked(): Boolean { return true }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_customer)
        super.onCreate(savedInstanceState)
        title = "Search Customer"
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.recyclerCustomersMinimal.adapter = customersAdapter

//        setSupportActionBar(binding.toolbar)

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
        binding.buttonCreateNew.setOnClickListener {
            editCustomer(null)
//            customerModal = AddEditCustomerFragment.getInstance(null)
//            customerModal.show(supportFragmentManager, "KEME")
//            customerModal.onOk = {
//                openCreateJobOrderActivity(it!!)
//            }
        }
        customersAdapter.onItemClick = {
            openCreateJobOrderActivity(it)
        }
        customersAdapter.onEdit = {
            editCustomer(it.id.toString())
        }
        customersAdapter.onScrollAtTheBottom = {
            viewModel.loadMore()
        }
//        viewModel.items.observe(this, Observer {
//            customersAdapter.setData(it)
//        })
    }

    private fun editCustomer(customerId: String?) {
        customerModal = AddEditCustomerFragment.getInstance(customerId)
        customerModal.show(supportFragmentManager, "KEME")
        customerModal.onOk = {
            openCreateJobOrderActivity(it!!)
        }
    }

    private fun openCreateJobOrderActivity(customer: CustomerMinimal) {
        val intent = Intent(this, JobOrderCreateActivity::class.java).apply {
            action = JobOrderCreateActivity.ACTION_LOAD_BY_CUSTOMER_ID
            putExtra(JobOrderCreateActivity.PAYLOAD_EXTRA, customer)
//            putExtra("customer", customer)
        }
        startActivity(intent)
    }

    override fun onQuery(keyword: String?) {
        viewModel.setKeyword(keyword)
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_search, menu)
//        searchBar = menu?.findItem(R.id.menu_search)?.actionView as SearchView
//        searchBar?.apply {
//            maxWidth = Integer.MAX_VALUE
//            queryHint = "Search Customer name or CRN"
//            setOnQueryTextFocusChangeListener { view, b ->
//                if(b) {
//                    binding.toolbar.setBackgroundColor(applicationContext.getColor(R.color.white))
//                } else {
//                    binding.toolbar.setBackgroundColor(applicationContext.getColor(R.color.teal_700))
//                }
//            }
//        }
//        searchBar?.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                searchBar?.clearFocus()
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                viewModel.setKeyword(newText)
//                return true
//            }
//        })
//        return super.onCreateOptionsMenu(menu)
//    }
}