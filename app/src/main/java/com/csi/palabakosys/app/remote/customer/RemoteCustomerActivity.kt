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
import com.csi.palabakosys.databinding.ActivityRemoteCustomerBinding
import com.csi.palabakosys.room.entities.EntityCustomerQueueService
import com.csi.palabakosys.services.MachineActivationService
import com.csi.palabakosys.util.ActivityLauncher
import com.csi.palabakosys.util.Constants
import com.csi.palabakosys.util.toUUID
import com.sangcomz.fishbun.util.setStatusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemoteCustomerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRemoteCustomerBinding
    private val viewModel: RemoteCustomerViewModel by viewModels()
    private val customerQueuesAdapter = Adapter<EntityCustomerQueueService>(R.layout.recycler_item_queue_customer)
    private val launcher = ActivityLauncher(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_remote_customer)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.recyclerCustomerQueues.adapter = customerQueuesAdapter

        subscribeListeners()
        subscribeEvents()
        intent.getStringExtra(Constants.MACHINE_ID_EXTRA).toUUID()?.let {
            viewModel.setMachineId(it)
        }

        setStatusBarColor(resources.getColor(R.color.color_code_machines, null))
    }

    private fun subscribeEvents() {
        customerQueuesAdapter.onItemClick = {
            viewModel.openQueueServices(it)
        }

        launcher.onOk = {
            if(it.data?.action == Constants.CASCADE_CLOSE) {
                setResult(RESULT_OK, Intent(Constants.CASCADE_CLOSE))
                finish()
            }
        }
    }

    private fun subscribeListeners() {
        viewModel.machine.observe(this, Observer {
//            title = "Select customer for  ${it?.machineName()}"
            if(it?.activationRef?.running() == true) {
                finish()
            }
        })

        viewModel.customerQueues.observe(this, Observer {
            it?.let {
                customerQueuesAdapter.setData(it)
            }
        })

        viewModel.navigationState.observe(this, Observer {
            when(it) {
                is RemoteCustomerViewModel.NavigationState.OpenQueuesServices -> {
                    val intent = Intent(this, RemoteQueuesActivity::class.java).apply {
                        putExtra(RemoteQueuesActivity.CUSTOMER_QUEUE_EXTRA, it.customerQueueService)
                        putExtra(Constants.MACHINE_ID_EXTRA, it.machineId.toString())
                    }
                    launcher.launch(intent)
                    viewModel.resetNavigationState()
                }
            }
        })
    }
}