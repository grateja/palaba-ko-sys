package com.csi.palabakosys.app.services

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.services.edit.AddEditServiceActivity
import com.csi.palabakosys.databinding.ActivityServicesBinding
import com.csi.palabakosys.model.EnumMachineType
import com.csi.palabakosys.room.entities.EntityService
import com.csi.palabakosys.util.ActivityLauncher
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ServicesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityServicesBinding
    private val viewModel: ServicesViewModel by viewModels()
    private val adapter = Adapter<EntityService>(R.layout.recycler_item_service)
    private val launcher = ActivityLauncher(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_services)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.inclMachines.recyclerAvailableServices.adapter = adapter

        subscribeEvents()
        subscribeListeners()
    }

    private fun subscribeEvents() {
        binding.tabMachineType.tabMachineType.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.selectTab(tab?.text.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) { }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                viewModel.selectTab(tab?.text.toString())
            }
        })
        adapter.onItemClick = {
            viewModel.openAddEdit(it.id)
        }
        binding.inclMachines.buttonAdd.setOnClickListener {
            viewModel.openAddEdit(null)
        }
        launcher.onOk = {
            val machineType = it.data?.getParcelableExtra<EnumMachineType>(AddEditServiceActivity.MACHINE_TYPE_EXTRA)
            val index = EnumMachineType.values().indexOf(
                machineType ?: EnumMachineType.REGULAR_WASHER
            )

            binding.tabMachineType.tabMachineType.getTabAt(index)?.select()
        }
    }

    private fun openAddEdit(serviceId: UUID?, machineType: EnumMachineType) {
        val intent = Intent(this, AddEditServiceActivity::class.java).apply {
            putExtra(AddEditServiceActivity.SERVICE_ID_EXTRA, serviceId.toString())
            putExtra(AddEditServiceActivity.MACHINE_TYPE_EXTRA, machineType as Parcelable)
        }
        launcher.launch(intent)
    }

    private fun subscribeListeners() {
        viewModel.services.observe(this, Observer {
            adapter.setData(it)
        })
        viewModel.selectedTab.observe(this, Observer {
            binding.inclMachines.viewModel = it
        })
        viewModel.dataState.observe(this, Observer {
            when(it) {
                is ServicesViewModel.DataState.OpenAddEdit -> {
                    openAddEdit(it.serviceId, it.machineType)
                    viewModel.resetState()
                }
            }
        })
    }
}