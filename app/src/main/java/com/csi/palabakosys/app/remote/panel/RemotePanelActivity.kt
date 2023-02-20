package com.csi.palabakosys.app.remote.panel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.remote.customer.RemoteCustomerFragment
import com.csi.palabakosys.app.remote.customer.RemoteCustomersActivity
import com.csi.palabakosys.app.remote.queues.RemoteQueuesFragment
import com.csi.palabakosys.databinding.ActivityRemotePanelBinding
import com.csi.palabakosys.room.entities.EntityMachine
import com.csi.palabakosys.util.ActivityLauncher
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemotePanelActivity : AppCompatActivity() {

    private val viewModel: RemotePanelViewModel by viewModels()
    private val machinesAdapter = Adapter<EntityMachine>(R.layout.recycler_item_machine_tile)
    private lateinit var binding: ActivityRemotePanelBinding
//    private lateinit var selectCustomerModal: RemoteCustomerFragment
//    private lateinit var selectServiceModal: RemoteQueuesFragment
    private var launcher = ActivityLauncher(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_remote_panel)
        binding.recyclerMachineTiles.adapter = machinesAdapter

        subscribeEvents()

        viewModel.loadMachines()
    }

    private fun subscribeEvents() {
        viewModel.machines.observe(this, Observer {
            machinesAdapter.setData(it)
        })

//        viewModel.machine.observe(this, Observer {
//            selectCustomerModal = RemoteCustomerFragment.newInstance()
//            selectCustomerModal.show(supportFragmentManager, it.machineName())
//        })
//
//        viewModel.queueService.observe(this, Observer {
//            selectServiceModal = RemoteQueuesFragment.newInstance()
//            selectServiceModal.show(supportFragmentManager, it.jobOrderId.toString())
//        })

        machinesAdapter.onItemClick = {
            val intent = Intent(applicationContext, RemoteCustomersActivity::class.java).apply {
                putExtra("machineId", it.id.toString())
            }
            launcher.launch(intent)
//            viewModel.selectMachine(it)
        }
    }
}