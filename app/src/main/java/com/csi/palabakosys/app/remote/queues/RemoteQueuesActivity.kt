package com.csi.palabakosys.app.remote.queues

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.remote.activate.RemoteActivationPreviewActivity
import com.csi.palabakosys.databinding.ActivityRemoteQueuesBinding
import com.csi.palabakosys.room.entities.EntityAvailableService
import com.csi.palabakosys.room.entities.EntityCustomerQueueService
import com.csi.palabakosys.services.MachineActivationService
import com.csi.palabakosys.util.ActivityLauncher
import com.csi.palabakosys.util.Constants
import com.csi.palabakosys.util.toUUID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemoteQueuesActivity : AppCompatActivity() {
    companion object {
        const val CUSTOMER_QUEUE_EXTRA = "customerQueue"
    }
    private lateinit var binding: ActivityRemoteQueuesBinding
    private val viewModel: RemoteQueuesViewModel by viewModels()
    private val serviceQueuesAdapter = Adapter<EntityAvailableService>(R.layout.recycler_item_queue_service)
    private val launcher = ActivityLauncher(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_remote_queues)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.recyclerAvailableServices.adapter = serviceQueuesAdapter

        subscribeListeners()
        subscribeEvents()
        intent.getParcelableExtra<EntityCustomerQueueService>(CUSTOMER_QUEUE_EXTRA).let {
            viewModel.setCustomerQueue(it)
        }

        intent.getStringExtra(Constants.MACHINE_ID_EXTRA).toUUID()?.let {
            viewModel.setMachineId(it)
        }
    }

    private fun subscribeEvents() {
        serviceQueuesAdapter.onItemClick = {
            it.id?.let { joServiceId ->
                viewModel.openActivationPreview(joServiceId)
            }
        }
    }

    private fun subscribeListeners() {
        viewModel.availableServices.observe(this, Observer {
            it?.let {
                serviceQueuesAdapter.setData(it)
            }
        })
        viewModel.navigationState.observe(this, Observer {
            when(it) {
                is RemoteQueuesViewModel.NavigationState.OpenActivationPreview -> {
                    val intent = Intent(this, RemoteActivationPreviewActivity::class.java).apply {
//                        putExtra(MachineActivationService.JO_SERVICE_ID_EXTRA, it.joServiceId.toString())
//                        putExtra(MachineActivationService.CUSTOMER_ID_EXTRA, it.customerId.toString())
//                        putExtra(Constants.MACHINE_ID_EXTRA, it.machineId.toString())
                        putExtra(MachineActivationService.ACTIVATION_QUEUES_EXTRA, it.queue)
                    }
                    launcher.launch(intent)
                    viewModel.resetNavigationState()
                }
            }
        })
    }
}