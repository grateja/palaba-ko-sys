package com.csi.palabakosys.app.joborders.create

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.app.joborders.create.services.JOSelectWashDryActivity
import com.csi.palabakosys.app.joborders.create.services.JobOrderServiceItemAdapter
import com.csi.palabakosys.app.joborders.create.services.MenuServiceItem
import com.csi.palabakosys.app.joborders.create.ui.ModifyQuantityModalFragment
import com.csi.palabakosys.app.joborders.create.ui.QuantityModel
import com.csi.palabakosys.app.joborders.create.ui.RemoveItemModalFragment
import com.csi.palabakosys.app.joborders.create.ui.RemoveItemModel
import com.csi.palabakosys.databinding.ActivityJobOrderCreateBinding
import com.csi.palabakosys.model.MachineType
import com.csi.palabakosys.model.WashType
import com.csi.palabakosys.util.ActivityLauncher
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobOrderCreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJobOrderCreateBinding
    private val viewModel: CreateJobOrderViewModel by viewModels()

    private val servicesLauncher = ActivityLauncher(this)

    private lateinit var removeItemModalFragment: RemoveItemModalFragment

    private val servicesAdapter = JobOrderServiceItemAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_job_order_create)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.setCustomer(
            intent.getParcelableExtra("customer")
        )

        binding.serviceItems.adapter = servicesAdapter

        subscribeEvents()
    }

    private fun subscribeEvents() {
        servicesLauncher.onOk = {
            val result = it.data?.getParcelableArrayListExtra<MenuServiceItem>("services")?.toList()
            viewModel.syncServices(result)
        }

        servicesAdapter.onItemClick = {
            viewModel.openServices(it)
        }

        binding.cardLegendServices.setOnClickListener {
            viewModel.openServices(null)
        }

        binding.cardLegendProducts.setOnClickListener {
            val list = listOf(MenuServiceItem("rw", "Regular Wash", 36, 70f, MachineType.REGULAR_WASHER, WashType.WARM, 2))
            viewModel.syncServices(list)
        }

        viewModel.jobOrderServices.observe(this, Observer {
            servicesAdapter.setData(it)
        })

        viewModel.dataState().observe(this, Observer {
            when(it) {
                is CreateJobOrderViewModel.DataState.OpenServices -> {
                    openServices(it.list, it.item)
                    viewModel.resetState()
                }
            }
        })
    }

    private fun openServices(services: List<MenuServiceItem>?, itemPreset: MenuServiceItem?) {
        val intent = Intent(this, JOSelectWashDryActivity::class.java).apply {
            services?.let {
                putParcelableArrayListExtra("services", ArrayList(it))
                putExtra("service", itemPreset)
            }
        }
        servicesLauncher.launch(intent)
    }
}