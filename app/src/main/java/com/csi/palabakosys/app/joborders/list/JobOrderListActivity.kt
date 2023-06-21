package com.csi.palabakosys.app.joborders.list

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.joborders.create.JobOrderCreateActivity
import com.csi.palabakosys.app.shared_ui.AdvancedSearchDialogActivity
import com.csi.palabakosys.databinding.ActivityJobOrderListBinding
import com.csi.palabakosys.util.FilterActivity
import com.csi.palabakosys.viewmodels.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobOrderListActivity : FilterActivity() {
    companion object {
        const val ADVANCED_OPTION_SEARCH = "advanced_search"
    }
    private lateinit var binding: ActivityJobOrderListBinding
    private val viewModel: JobOrderListViewModel by viewModels()

    override var filterHint = "Search customer name or CRN"
    override var enableAdvancedSearch = true
    override fun onAdvancedSearchClicked(): Boolean {
//        viewModel.showAdvancedOptions()
//        val intent = Intent(this, AdvancedSearchDialogActivity::class.java).apply {
//            action = ADVANCED_OPTION_SEARCH
//            putExtra(AdvancedSearchDialogActivity.KEYWORD_EXTRA, searchBar?.query)
//        }
//        addEditLauncher.launch(intent)
        return true
    }

    //    private var searchBar: SearchView? = null
    private val adapter = Adapter<JobOrderListItem>(R.layout.recycler_item_job_order_list_item)

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_job_order_list)
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.recyclerJobOrders.adapter = adapter

        subscribeEvents()
        subscribeListeners()
        viewModel.filter(true)
    }

    override fun onQuery(keyword: String?) {
        viewModel.setKeyword(keyword)
    }

    private fun subscribeEvents() {
        adapter.onItemClick = {
            val intent = Intent(this, JobOrderCreateActivity::class.java).apply {
                action = JobOrderCreateActivity.ACTION_LOAD_BY_JOB_ORDER_ID
                putExtra(JobOrderCreateActivity.PAYLOAD_EXTRA, it)
            }
            addEditLauncher.launch(intent)
//            startActivity(intent)
        }
        addEditLauncher.onOk = {
            viewModel.filter(true)
        }
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
//        viewModel.items.observe(this, Observer {
//            adapter.setData(it)
//        })
//        binding.buttonNext.setOnClickListener {
//            viewModel.navigate(1)
//        }
//        binding.buttonPrev.setOnClickListener {
//            viewModel.navigate(-1)
//        }
//        viewModel.dataState.observe(this, Observer {
//            when (it) {
//                is JobOrderListViewModel.DataState.ShowAdvancedSearch -> {
//                    val intent = Intent(this, AdvancedSearchDialogActivity::class.java).apply {
//                        action = ADVANCED_OPTION_SEARCH
//                        putExtra(AdvancedSearchDialogActivity.KEYWORD_EXTRA, it.keyword)
//                        putExtra(AdvancedSearchDialogActivity.ORDER_BY_EXTRA, it.orderBy)
//                        putExtra(AdvancedSearchDialogActivity.SORT_DIRECTION_EXTRA, it.sortDirection)
//                        putExtra(AdvancedSearchDialogActivity.ITEM_PER_PAGE_EXTRA, it.itemPerPage)
//                        putExtra(AdvancedSearchDialogActivity.PAGE_EXTRA, it.page)
//                    }
//                    addEditLauncher.launch(intent)
//                }
//            }
//        })
    }
}