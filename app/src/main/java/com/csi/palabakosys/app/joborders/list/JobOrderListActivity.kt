package com.csi.palabakosys.app.joborders.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.dashboard.data.DateFilter
import com.csi.palabakosys.app.joborders.create.JobOrderCreateActivity
import com.csi.palabakosys.app.joborders.create.customer.SelectCustomerActivity
import com.csi.palabakosys.app.shared_ui.AdvancedSearchDialogActivity
import com.csi.palabakosys.databinding.ActivityJobOrderListBinding
import com.csi.palabakosys.model.EnumJoFilterBy
import com.csi.palabakosys.model.EnumPaymentStatus
import com.csi.palabakosys.util.Constants
import com.csi.palabakosys.util.FilterActivity
import com.csi.palabakosys.viewmodels.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobOrderListActivity : FilterActivity() {
    companion object {
        const val FILTER_BY = "filterBy"
    }

    private lateinit var binding: ActivityJobOrderListBinding
    private val viewModel: JobOrderListViewModel by viewModels()

    override var filterHint = "Search customer name or CRN"

    private val adapter = Adapter<JobOrderListItem>(R.layout.recycler_item_job_order_list_item)

    override fun onCreate(savedInstanceState: Bundle?) {
        title = "Job Orders"
        binding = DataBindingUtil.setContentView(this, R.layout.activity_job_order_list)
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.recyclerJobOrderList.adapter = adapter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        subscribeEvents()
        subscribeListeners()

        intent.getParcelableExtra<DateFilter>(Constants.DATE_RANGE_FILTER)?.let {
            viewModel.setDateRange(it)
        }
        intent.getParcelableExtra<EnumJoFilterBy>(FILTER_BY)?.let {
            viewModel.setFilterBy(it)
            println("filter by")
            println(it)
        }
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
            selectCustomer()
        }
    }

    private fun selectCustomer() {
        val intent = Intent(this, SelectCustomerActivity::class.java)
        startActivity(intent)
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

        viewModel.sortDirection.observe(this, Observer {
            viewModel.filter(true)
        })

        viewModel.orderBy.observe(this, Observer {
            viewModel.filter(true)
        })

        viewModel.paymentStatus.observe(this, Observer {
            viewModel.filter(true)
        })
    }
}