package com.csi.palabakosys.app.remote.panel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.databinding.ActivityRemotePanelBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemotePanelActivity : AppCompatActivity() {

    private val viewModel: RemotePanelViewModel by viewModels()
    private val machinesAdapter = Adapter<MachineTile>(R.layout.recycler_item_machine_tile)
    private lateinit var binding: ActivityRemotePanelBinding
    private lateinit var selectCustomerModal: RemoteSelectCustomerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_remote_panel)

        binding.recyclerMachineTiles.adapter = machinesAdapter

        subscribeEvents()

        viewModel.loadAll()
    }

    private fun subscribeEvents() {
        viewModel.machines.observe(this, Observer {
            machinesAdapter.setData(it)
        })

        machinesAdapter.onItemClick = {
            openSelectCustomerModal(it)
        }
    }

    private fun openSelectCustomerModal(machineTile: MachineTile) {
        viewModel.selectMachine(machineTile)
        selectCustomerModal = RemoteSelectCustomerFragment.newInstance()
        selectCustomerModal.show(supportFragmentManager, machineTile.toString())
    }
}