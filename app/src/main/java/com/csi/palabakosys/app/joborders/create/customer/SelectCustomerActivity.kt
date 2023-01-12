package com.csi.palabakosys.app.joborders.create.customer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.customers.CustomerMinimal
import com.csi.palabakosys.app.customers.create.AddEditCustomerFragment
import com.csi.palabakosys.app.customers.create.AddEditCustomerViewModel
import com.csi.palabakosys.app.joborders.create.CreateJobOrderActivity
import com.csi.palabakosys.databinding.ActivitySelectCustomerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectCustomerActivity : AppCompatActivity() {
    private var searchBar: SearchView? = null
    private val viewModel: SelectCustomerViewModel by viewModels()
    private lateinit var binding: ActivitySelectCustomerBinding
    private lateinit var customerModal: AddEditCustomerFragment
    private val customersAdapter = Adapter<CustomerMinimal>(R.layout.recycler_item_customer_minimal)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Search Customer"
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_customer)
        binding.recyclerCustomersMinimal.adapter = customersAdapter
        setSupportActionBar(binding.toolbar)
        subscribeEvents()
    }

    private fun subscribeEvents() {
        binding.buttonCreateNew.setOnClickListener {
            customerModal = AddEditCustomerFragment.getInstance(null)
            customerModal.show(supportFragmentManager, "KEME")
            customerModal.onOk = {
                openCreateJobOrderActivity(it!!)
            }
        }
        customersAdapter.onItemClick = {
            openCreateJobOrderActivity(it)
        }
        viewModel.customers.observe(this, Observer {
            customersAdapter.setData(it)
        })
    }

    private fun openCreateJobOrderActivity(customer: CustomerMinimal) {
        val intent = Intent(this, CreateJobOrderActivity::class.java).apply {
            putExtra("customer", customer)
        }
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        searchBar = menu?.findItem(R.id.menu_search)?.actionView as SearchView
        searchBar?.apply {
            maxWidth = Integer.MAX_VALUE
            queryHint = "Search Customer name or CRN"
            setOnQueryTextFocusChangeListener { view, b ->
                if(b) {
                    binding.toolbar.setBackgroundColor(applicationContext.getColor(R.color.white))
                } else {
                    binding.toolbar.setBackgroundColor(applicationContext.getColor(R.color.teal_700))
                }
            }
        }
        searchBar?.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchBar?.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchMinimal(newText)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
}