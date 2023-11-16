package com.csi.palabakosys.app.pickup_and_deliveries

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.app.joborders.create.delivery.DeliveryVehiclesAdapter
import com.csi.palabakosys.app.pickup_and_deliveries.add_edit.AddEditDeliveryProfileActivity
import com.csi.palabakosys.databinding.ActivityPickupAndDeliveriesBinding
import com.csi.palabakosys.util.CrudActivity
import com.csi.palabakosys.viewmodels.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class PickupAndDeliveriesActivity : AppCompatActivity() {
    private val viewModel: PickupAndDeliveriesViewModel by viewModels()
    private lateinit var binding: ActivityPickupAndDeliveriesBinding
    private val adapter = DeliveryVehiclesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pickup_and_deliveries)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.recyclerDeliveryProfiles.adapter = adapter

        subscribeListeners()
        subscribeEvents()
    }

    override fun onResume() {
        super.onResume()
        viewModel.filter(true)
    }

    private fun openAddEdit(id: UUID) {
        val intent = Intent(this, AddEditDeliveryProfileActivity::class.java).apply {
            putExtra(CrudActivity.ENTITY_ID, id.toString())
        }
        startActivity(intent)
    }

    private fun subscribeEvents() {
        adapter.onItemClick = {
            openAddEdit(it.deliveryProfileRefId)
        }
    }

    private fun subscribeListeners() {
        viewModel.dataState.observe(this, Observer {
            when(it) {
                is ListViewModel.DataState.LoadItems -> {
                    if(it.reset) {
                        adapter.setData(it.items)
                    }
                }
            }
        })
    }
}