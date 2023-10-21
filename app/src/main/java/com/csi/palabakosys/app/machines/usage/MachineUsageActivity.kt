package com.csi.palabakosys.app.machines.usage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.dashboard.data.DateFilter
import com.csi.palabakosys.app.shared_ui.BottomSheetDateRangePickerFragment
import com.csi.palabakosys.databinding.ActivityMachineUsageBinding
import com.csi.palabakosys.room.entities.EntityMachineUsageDetails
import com.csi.palabakosys.util.Constants
import com.csi.palabakosys.util.FilterActivity
import com.csi.palabakosys.util.toUUID
import com.csi.palabakosys.viewmodels.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MachineUsageActivity : FilterActivity() {
    private val viewModel: MachineUsageViewModel by viewModels()
    private lateinit var binding: ActivityMachineUsageBinding
    private val adapter = Adapter<EntityMachineUsageDetails>(R.layout.recycler_item_machine_usage_details)
    private lateinit var dateRangeDialog: BottomSheetDateRangePickerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_machine_usage)

        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.recyclerMachineUsage.adapter = adapter

        subscribeEvents()
        subscribeListeners()
    }

    override fun onResume() {
        super.onResume()

        intent.getStringExtra(Constants.MACHINE_ID_EXTRA)?.toUUID()?.let {
            viewModel.setMachineId(it)
        }
        intent.getParcelableExtra<DateFilter>(Constants.DATE_RANGE_FILTER)?.let {
            viewModel.setDateFilter(it)
        }

        viewModel.filter(true)
    }

    override var filterHint: String = "Search customer name"

    override fun onQuery(keyword: String?) {
        viewModel.setKeyword(keyword)
    }

    private fun openDateFilter(dateFilter: DateFilter) {
        dateRangeDialog = BottomSheetDateRangePickerFragment.getInstance(dateFilter)
        dateRangeDialog.show(supportFragmentManager, null)
        dateRangeDialog.onOk = {
            viewModel.setDateFilter(it)
            viewModel.filter(true)
        }
    }

    private fun subscribeEvents() {
        adapter.onScrollAtTheBottom = {
            viewModel.loadMore()
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
                }
            }
        })
        viewModel.navigationState.observe(this, Observer {
            when(it) {
                is MachineUsageViewModel.NavigationState.OpenDateFilter -> {
                    openDateFilter(it.dateFilter)
                    viewModel.clearState()
                }
            }
        })
        viewModel.dateFilter.observe(this, Observer {
            viewModel.filter(true)
        })
    }
}