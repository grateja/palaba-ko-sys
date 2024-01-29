package com.csi.palabakosys.app.joborders.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.dashboard.data.DateFilter
import com.csi.palabakosys.app.joborders.create.JobOrderCreateActivity
import com.csi.palabakosys.app.joborders.create.customer.SelectCustomerActivity
import com.csi.palabakosys.app.shared_ui.AdvancedSearchDialogActivity
import com.csi.palabakosys.app.shared_ui.BottomSheetDateRangePickerFragment
import com.csi.palabakosys.databinding.ActivityJobOrderListBinding
import com.csi.palabakosys.model.EnumJoFilterBy
import com.csi.palabakosys.model.EnumPaymentStatus
import com.csi.palabakosys.model.JobOrderAdvancedFilter
import com.csi.palabakosys.util.Constants
import com.csi.palabakosys.util.FilterActivity
import com.csi.palabakosys.viewmodels.ListViewModel
import com.google.android.material.tabs.TabLayout
import com.sangcomz.fishbun.util.setStatusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobOrderListActivity : FilterActivity() {
    companion object {
        const val ADVANCED_FILTER = "advanced_filter"
    }

    private lateinit var binding: ActivityJobOrderListBinding
    private val viewModel: JobOrderListViewModel by viewModels()

    override var filterHint = "Search customer name or CRN"
    override var toolbarBackground: Int = R.color.color_code_job_order

    private val adapter = Adapter<JobOrderListItem>(R.layout.recycler_item_job_order_list_item)
    private lateinit var dateRangeDialog: BottomSheetDateRangePickerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        title = "Job Orders"
        binding = DataBindingUtil.setContentView(this, R.layout.activity_job_order_list)
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.recyclerJobOrderList.adapter = adapter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.tabRegularAndTrashed.apply {
            this.addTab(this.newTab().setText("Active"))
            this.addTab(this.newTab().setText("Deleted"))
            this.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    viewModel.setView(tab?.position == 0)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) { }

                override fun onTabReselected(tab: TabLayout.Tab?) { }
            })
        }

        subscribeEvents()
        subscribeListeners()

//        intent.getParcelableExtra<DateFilter>(Constants.DATE_RANGE_FILTER)?.let {
//            viewModel.setDateRange(it)
//        }
//        intent.getParcelableExtra<EnumJoFilterBy>(FILTER_BY)?.let {
//            viewModel.setFilterBy(it)
//        }

        intent.extras?.getParcelable<JobOrderAdvancedFilter>(ADVANCED_FILTER).let {
            Toast.makeText(this, it?.dateFilter.toString(), Toast.LENGTH_LONG).show()
            println("advanced filter")
            println(it?.dateFilter)
            viewModel.setAdvancedFilter(it ?: JobOrderAdvancedFilter())
        }
    }

    override fun onAdvancedSearchClick() {
        viewModel.showAdvancedFilter()
    }

    override fun onQuery(keyword: String?) {
        viewModel.setKeyword(keyword)
    }

    private fun subscribeEvents() {
        adapter.onItemClick = {
            val intent = Intent(this, JobOrderCreateActivity::class.java).apply {
                action = JobOrderCreateActivity.ACTION_LOAD_BY_JOB_ORDER_ID
                putExtra(JobOrderCreateActivity.JOB_ORDER_ID, it.id.toString())
            }
            addEditLauncher.launch(intent)
        }
        addEditLauncher.onOk = {
            viewModel.filter(true)
        }
        adapter.onScrollAtTheBottom = {
            viewModel.loadMore()
        }
        binding.cardAddNew.setOnClickListener {
            createNewJo()
//            selectCustomer()
        }
        binding.cardAdvancedOptions.setOnClickListener {
            viewModel.showAdvancedFilter()
        }
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.filter(true)
        }
    }

    private fun createNewJo() {
        val intent = Intent(this, JobOrderCreateActivity::class.java)
        startActivity(intent)
    }

    private fun selectCustomer() {
        val intent = Intent(this, SelectCustomerActivity::class.java)
        startActivity(intent)
    }

    private fun openDateFilter(dateFilter: DateFilter) {
        dateRangeDialog = BottomSheetDateRangePickerFragment.getInstance(dateFilter)
        dateRangeDialog.show(supportFragmentManager, null)
        dateRangeDialog.onOk = {
//            viewModel.setDateRange(it)
        }
    }

    private fun subscribeListeners() {
        viewModel.total.observe(this, Observer {
            println(it)
        })
        viewModel.dataState.observe(this, Observer {
            when(it) {
                is ListViewModel.DataState.LoadItems -> {
                    if(it.reset) {
                        adapter.setData(it.items)
                    } else {
                        adapter.addItems(it.items)
                    }
                    binding.swipeRefresh.isRefreshing = false
                    viewModel.clearState()
                }
            }
        })

        viewModel.navigationState.observe(this, Observer {
            when(it) {
                is JobOrderListViewModel.NavigationState.OpenDateFilter -> {
                    openDateFilter(it.dateFilter)
                    viewModel.clearState()
                }
                is JobOrderListViewModel.NavigationState.ShowAdvancedFilter -> {
                    println("advanced filter date")
                    println(it.advancedFilter.dateFilter)
                    val fragment = JobOrderListAdvancedFilterFragment.getInstance(it.advancedFilter)
                    fragment.show(supportFragmentManager, null)
                    fragment.onOk = {
                        viewModel.setAdvancedFilter(it)
                    }
                    viewModel.clearState()
                }
            }
        })

        viewModel.filterParams.observe(this, Observer {
            viewModel.filter(true)
            println("awesome")
        })

//        viewModel.sortDirection.observe(this, Observer {
//            viewModel.filter(true)
//        })
//
//        viewModel.orderBy.observe(this, Observer {
//            viewModel.filter(true)
//        })
//
//        viewModel.paymentStatus.observe(this, Observer {
//            viewModel.filter(true)
//        })
//
//        viewModel.filterBy.observe(this, Observer {
//            viewModel.filter(true)
//        })
//
//        viewModel.dateFilter.observe(this, Observer {
//            viewModel.filter(true)
//        })
//        viewModel.includeVoid.observe(this, Observer {
//            viewModel.filter(true)
//        })
    }
}