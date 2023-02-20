package com.csi.palabakosys.app.remote.queues

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.customers.CustomerMinimal
import com.csi.palabakosys.app.machines.MachineMinimal
import com.csi.palabakosys.databinding.ActivityRemoteQueuesBinding
import com.csi.palabakosys.room.entities.EntityAvailableService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemoteQueuesActivity : AppCompatActivity() {
    private val viewModel: RemoteQueuesViewModel by viewModels()
    private lateinit var binding: ActivityRemoteQueuesBinding
    private val adapter = Adapter<EntityAvailableService>(R.layout.recycler_item_queue_service)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_remote_queues)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.recyclerAvailableServices.adapter = adapter

        val customer = intent.getParcelableExtra<CustomerMinimal>("customer")
        val machine = intent.getParcelableExtra<MachineMinimal>("machine")

        viewModel.setCurrentCustomer(customer)
        viewModel.setCurrentMachine(machine)
        viewModel.getAvailableServicesByCustomerId()

        subscribeEvents()
    }

    private fun subscribeEvents() {
        viewModel.availableServices.observe(this, Observer {
            adapter.setData(it)
        })
    }
}