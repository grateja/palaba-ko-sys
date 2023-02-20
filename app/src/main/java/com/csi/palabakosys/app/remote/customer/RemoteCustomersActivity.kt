package com.csi.palabakosys.app.remote.customer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.remote.queues.RemoteQueuesActivity
import com.csi.palabakosys.databinding.ActivityRemoteCustomersBinding
import com.csi.palabakosys.room.entities.EntityCustomerQueueService
import com.csi.palabakosys.util.ActivityLauncher
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemoteCustomersActivity : AppCompatActivity() {
    private val viewModel: RemoteCustomerViewModel by viewModels()
    private lateinit var binding: ActivityRemoteCustomersBinding
    private var adapter = Adapter<EntityCustomerQueueService>(R.layout.recycler_item_queue_customer)

    private var launcher = ActivityLauncher(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_remote_customers)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.recyclerCustomerQueues.adapter = adapter
        viewModel.getMachine(intent.getStringExtra("machineId"))
        subscribeEvents()
    }

    private fun subscribeEvents() {
        viewModel.machine.observe(this, Observer {
            viewModel.getCustomersByMachineType(it.machineType)
        })
        viewModel.customerQueues.observe(this, Observer {
            adapter.setData(it)
        })
        adapter.onItemClick = {
            viewModel.selectCustomer(it)
        }
        viewModel.dataState.observe(this, Observer {
            when(it) {
                is RemoteCustomerViewModel.DataState.SelectCustomer -> {
                    val intent = Intent(applicationContext, RemoteQueuesActivity::class.java).apply {
                        putExtra("customer", it.customer)
                        putExtra("machine", it.machine)
                    }
                    launcher.launch(intent)
                    viewModel.resetState()
                }
            }
        })
    }
}