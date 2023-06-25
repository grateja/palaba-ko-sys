package com.csi.palabakosys.app.customers.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.databinding.ActivityCustomersBinding
import com.csi.palabakosys.util.FilterActivity
import com.csi.palabakosys.viewmodels.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomersActivity : FilterActivity() {
    private lateinit var binding: ActivityCustomersBinding
    private val viewModel: CustomersViewModel by viewModels()
    private val adapter = Adapter<CustomerListItem>(R.layout.recycler_item_customers_list_item)

    override fun onCreate(savedInstanceState: Bundle?) {
        title = "Customers"
        binding = DataBindingUtil.setContentView(this, R.layout.activity_customers)
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.recycler.adapter = adapter

        subscribeEvents()
        subscribeListeners()
    }

    private fun subscribeEvents() {
        adapter.onScrollAtTheBottom = {
            viewModel.loadMore()
        }
    }

    private fun subscribeListeners() {
        viewModel.dataState.observe(this, Observer {
            when(it) {
                is ListViewModel.DataState.LoadItems -> {
                    if(it.reset) {
                        adapter.setData(it.items)
                    } else {
                        adapter.addItems(it.items)
                    }
                }
            }
        })
        viewModel.orderBy.observe(this, Observer {
            viewModel.filter(true)
        })
        viewModel.sortDirection.observe(this, Observer {
            viewModel.filter(true)
        })
    }


    override var filterHint = "Search customer name of CRN"

    override fun onQuery(keyword: String?) {
        viewModel.setKeyword(keyword)
    }
}