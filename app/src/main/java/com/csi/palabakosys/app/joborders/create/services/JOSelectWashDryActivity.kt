package com.csi.palabakosys.app.joborders.create.services

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.app.joborders.create.shared_ui.ModifyQuantityModalFragment
import com.csi.palabakosys.app.joborders.create.shared_ui.QuantityModel
import com.csi.palabakosys.databinding.ActivityJoSelectWashDryBinding
import com.csi.palabakosys.model.EnumMachineType
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JOSelectWashDryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJoSelectWashDryBinding

    private val viewModel: AvailableServicesViewModel by viewModels()

    private lateinit var modifyQuantityDialog: ModifyQuantityModalFragment

//    private val rwAdapter = AvailableServicesAdapter()
//    private val rdAdapter = AvailableServicesAdapter()
//    private val twAdapter = AvailableServicesAdapter()
//    private val tdAdapter = AvailableServicesAdapter()

    private val adapter = AvailableServicesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_jo_select_wash_dry)
        binding.lifecycleOwner = this

        binding.inclMachines.recyclerAvailableServices.adapter = adapter

//        binding.inclMenu8KGWashers.viewModel = MachineType.REGULAR_WASHER
//        binding.inclMenu8KGDryers.viewModel = MachineType.REGULAR_DRYER
//        binding.inclMenu12KGWashers.viewModel = MachineType.TITAN_WASHER
//        binding.inclMenu12KGDryers.viewModel = MachineType.TITAN_DRYER
//
//        binding.inclMenu8KGWashers.recyclerAvailableServices.adapter = rwAdapter
//        binding.inclMenu8KGDryers.recyclerAvailableServices.adapter = rdAdapter
//        binding.inclMenu12KGWashers.recyclerAvailableServices.adapter = twAdapter
//        binding.inclMenu12KGDryers.recyclerAvailableServices.adapter = tdAdapter

        subscribeEvents()

    }

    private fun openSelectedItem() {
        val tab = binding.tabMachineType.tabMachineType
        intent.getParcelableExtra<MenuServiceItem>("service").let {
            if(it != null) {
                itemClick(it)
            } else {
                viewModel.setMachineType(EnumMachineType.REGULAR_WASHER.toString())
            }

            val index = EnumMachineType.values().indexOf(
                it?.machineType ?: EnumMachineType.REGULAR_WASHER
            )

//            if(tab.selectedTabPosition == 1 && index == 0) {
//
//            }

            tab.getTabAt(index)?.select()
            println(index)
            println("index")
        }
    }

    private fun itemClick(serviceItem: MenuServiceItem) {
        requestModifyQuantity(
            QuantityModel(serviceItem.serviceRefId, serviceItem.abbr(), serviceItem.quantity, QuantityModel.TYPE_SERVICE)
        )
    }

    private fun requestModifyQuantity(quantityModel: QuantityModel) {
        modifyQuantityDialog = ModifyQuantityModalFragment.getInstance(quantityModel).apply {
            onOk = {
                viewModel.putService(it)
            }
            onItemRemove = {
                viewModel.removeService(it)
            }
        }
        modifyQuantityDialog.show(supportFragmentManager, this.toString())
    }

//    private fun refreshList(adapter: AvailableServicesAdapter, service: MenuServiceItem) {
//        adapter.updateItem(service)
//    }

    private fun subscribeEvents() {
//        rwAdapter.onItemClick = { itemClick(it) }
//        rdAdapter.onItemClick = { itemClick(it) }
//        twAdapter.onItemClick = { itemClick(it) }
//        tdAdapter.onItemClick = { itemClick(it) }
        adapter.onItemClick = { itemClick(it) }

        binding.tabMachineType.tabMachineType.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.setMachineType(tab?.text.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) { }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                viewModel.setMachineType(tab?.text.toString())
                println("Reselect tab $tab")
            }
        })
        binding.buttonOk.setOnClickListener {
            viewModel.prepareSubmit()
        }
        binding.buttonCancel.setOnClickListener {
            finish()
        }
        viewModel.selectedTab.observe(this, Observer {
            binding.inclMachines.viewModel = it
            adapter.setData(viewModel.getServices(it))
        })
        viewModel.availableServices.observe(this, Observer {
//            rwAdapter.setData(it.filter { avs -> avs.machineType == MachineType.REGULAR_WASHER })
//            rdAdapter.setData(it.filter { avs -> avs.machineType == MachineType.REGULAR_DRYER })
//            twAdapter.setData(it.filter { avs -> avs.machineType == MachineType.TITAN_WASHER })
//            tdAdapter.setData(it.filter { avs -> avs.machineType == MachineType.TITAN_DRYER })
//            adapter.setData(it.filter {avs -> avs.machineType == })
            viewModel.setPreSelectedServices(intent.getParcelableArrayListExtra<MenuServiceItem>("services")?.toList())
            openSelectedItem()
//            viewModel.setMachineType(
//                intent.getStringExtra("machineType")
//            )
        })

        viewModel.dataState.observe(this, Observer {
            when(it) {
                is AvailableServicesViewModel.DataState.UpdateService -> {
//                    if(it.serviceItem.machineType == MachineType.REGULAR_WASHER) {
//                        refreshList(rwAdapter, it.serviceItem)
//                    } else if(it.serviceItem.machineType == MachineType.REGULAR_DRYER) {
//                        refreshList(rdAdapter, it.serviceItem)
//                    } else if(it.serviceItem.machineType == MachineType.TITAN_WASHER) {
//                        refreshList(twAdapter, it.serviceItem)
//                    } else if(it.serviceItem.machineType == MachineType.TITAN_DRYER) {
//                        refreshList(tdAdapter, it.serviceItem)
//                    }
                    adapter.updateItem(it.serviceItem)
                    viewModel.resetState()
                }
                is AvailableServicesViewModel.DataState.Submit -> {
                    submit(it.selectedItems)
                    viewModel.resetState()
                }
                is AvailableServicesViewModel.DataState.InvalidOperation -> {
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                    viewModel.resetState()
                }
            }
        })
    }

    private fun submit(list: List<MenuServiceItem>) {
        setResult(RESULT_OK, Intent().apply {
            putParcelableArrayListExtra("services", ArrayList(list))
        })
        finish()
    }
}