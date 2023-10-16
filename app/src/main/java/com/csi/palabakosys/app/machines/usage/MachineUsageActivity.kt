package com.csi.palabakosys.app.machines.usage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.dashboard.data.DateFilter
import com.csi.palabakosys.databinding.ActivityMachineUsageBinding
import com.csi.palabakosys.room.entities.EntityMachineUsageDetails
import com.csi.palabakosys.util.Constants
import com.csi.palabakosys.util.toUUID
import com.csi.palabakosys.viewmodels.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MachineUsageActivity : AppCompatActivity() {
    private val viewModel: MachineUsageViewModel by viewModels()
    private lateinit var binding: ActivityMachineUsageBinding
    private val adapter = Adapter<EntityMachineUsageDetails>(R.layout.recycler_item_machine_usage_details)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_machine_usage)
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
    }
}