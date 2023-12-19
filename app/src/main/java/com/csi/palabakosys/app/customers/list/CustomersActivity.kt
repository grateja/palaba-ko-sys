package com.csi.palabakosys.app.customers.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.customers.preview.CustomerPreviewActivity
import com.csi.palabakosys.app.dashboard.data.DateFilter
import com.csi.palabakosys.app.shared_ui.BottomSheetDateRangePickerFragment
import com.csi.palabakosys.databinding.ActivityCustomersBinding
import com.csi.palabakosys.util.Constants
import com.csi.palabakosys.util.FilterActivity
import com.csi.palabakosys.viewmodels.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomersActivity : FilterActivity() {
    private lateinit var binding: ActivityCustomersBinding
    private val viewModel: CustomersViewModel by viewModels()
    private val adapter = Adapter<CustomerListItem>(R.layout.recycler_item_customers_list_item)
    private lateinit var dateRangeDialog: BottomSheetDateRangePickerFragment

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

    override fun onResume() {
        super.onResume()
        intent.getParcelableExtra<DateFilter>(Constants.DATE_RANGE_FILTER)?.let { dateFilter ->
            viewModel.setDateRange(dateFilter)
        } ?: viewModel.filter(true)
    }

    private fun openDateFilter(dateFilter: DateFilter) {
        dateRangeDialog = BottomSheetDateRangePickerFragment.getInstance(dateFilter)
        dateRangeDialog.show(supportFragmentManager, null)
        dateRangeDialog.onOk = {
            viewModel.setDateRange(it)
        }
    }

    private fun subscribeEvents() {
        adapter.onScrollAtTheBottom = {
            viewModel.loadMore()
        }
        adapter.onItemClick = {
            val intent = Intent(this, CustomerPreviewActivity::class.java).apply {
                putExtra(CustomerPreviewActivity.CUSTOMER_ID_EXTRA, it.customer.id.toString())
            }
            addEditLauncher.launch(intent)
        }
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.filter(true)
        }
        binding.cardDateRange.setOnClickListener {
            viewModel.showDatePicker()
        }
        binding.buttonClearDateFilter.setOnClickListener {
            viewModel.clearDates()
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
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        })
        viewModel.navigationState.observe(this, Observer {
            when(it) {
                is CustomersViewModel.NavigationState.OpenDateFilter -> {
                    openDateFilter(it.dateFilter)
                    viewModel.clearState()
                }
            }
        })
//        viewModel.orderBy.observe(this, Observer {
//            viewModel.filter(true)
//        })
//        viewModel.sortDirection.observe(this, Observer {
//            viewModel.filter(true)
//        })
        viewModel.dateFilter.observe(this, Observer {
            viewModel.filter(true)
        })
    }


    override var filterHint = "Search customer name or CRN"
    override var toolbarBackground: Int = R.color.color_code_customers

    override fun onQuery(keyword: String?) {
        viewModel.setKeyword(keyword)
    }
}