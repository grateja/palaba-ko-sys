package com.csi.palabakosys.app.remote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.machines.MachineListItem
import com.csi.palabakosys.app.remote.activate.RemoteActivationPreviewActivity
import com.csi.palabakosys.app.remote.customer.RemoteCustomerActivity
import com.csi.palabakosys.app.remote.panel.RemotePanelAdapter
import com.csi.palabakosys.app.remote.running.RemoteRunningActivity
import com.csi.palabakosys.databinding.ActivityRemotePanelBinding
import com.csi.palabakosys.databinding.ActivityRemotePanelCopyBinding
import com.csi.palabakosys.model.EnumMachineType
import com.csi.palabakosys.util.ActivityLauncher
import com.csi.palabakosys.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemotePanelActivityCopy : AppCompatActivity() {
    private lateinit var binding: ActivityRemotePanelCopyBinding
    private val viewModel: RemotePanelViewModel by viewModels()

    private val regularWashersAdapter = RemotePanelAdapter() //Adapter<MachineListItem>(R.layout.recycler_item_machine_tile)
    private val regularDryersAdapter = RemotePanelAdapter() //Adapter<MachineListItem>(R.layout.recycler_item_machine_tile)
    private val titanWashersAdapter = RemotePanelAdapter() //Adapter<MachineListItem>(R.layout.recycler_item_machine_tile)
    private val titanDryersAdapter = RemotePanelAdapter() //Adapter<MachineListItem>(R.layout.recycler_item_machine_tile)

    private val launcher = ActivityLauncher(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_remote_panel)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.inclRegularWashers.recycler.layoutManager = GridLayoutManager(this, spanCount)
        binding.inclRegularWashers.recycler.adapter = regularWashersAdapter

        binding.inclRegularDryers.recycler.layoutManager = GridLayoutManager(this, spanCount)
        binding.inclRegularDryers.recycler.adapter = regularDryersAdapter

        binding.inclTitanWashers.recycler.layoutManager = GridLayoutManager(this, spanCount)
        binding.inclTitanWashers.recycler.adapter = titanWashersAdapter

        binding.inclTitanDryers.recycler.layoutManager = GridLayoutManager(this, spanCount)
        binding.inclTitanDryers.recycler.adapter = titanDryersAdapter

        subscribeEvents()
        subscribeListeners()
    }

    private fun subscribeEvents() {
        regularWashersAdapter.onItemClick = { selectMachine(it) }
        regularDryersAdapter.onItemClick = { selectMachine(it) }
        titanWashersAdapter.onItemClick = { selectMachine(it) }
        titanDryersAdapter.onItemClick = { selectMachine(it) }
    }

    private fun subscribeListeners() {
        viewModel.machines.observe(this, Observer {
            val regularDryers = it.filter { it.machine.machineType == EnumMachineType.REGULAR_DRYER }
            regularWashersAdapter.setData(it.filter { it.machine.machineType == EnumMachineType.REGULAR_WASHER })
            regularDryersAdapter.setData(it.filter { it.machine.machineType == EnumMachineType.REGULAR_DRYER })
            titanWashersAdapter.setData(it.filter { it.machine.machineType == EnumMachineType.TITAN_WASHER })
            titanDryersAdapter.setData(it.filter { it.machine.machineType == EnumMachineType.TITAN_DRYER })
            println("items")
            println(regularDryers.size)
        })
    }

    private fun selectMachine(item: MachineListItem) {
        val intent = if(item.machine.activationRef?.running() == true) {
            Intent(this, RemoteRunningActivity::class.java).apply {
                putExtra(Constants.MACHINE_ID_EXTRA, item.machine.id.toString())
            }
        } else if(item.machine.serviceActivationId != null) {
            Intent(this, RemoteActivationPreviewActivity::class.java).apply {
                putExtra(Constants.MACHINE_ID_EXTRA, item.machine.id.toString())
            }
        } else {
            Intent(this, RemoteCustomerActivity::class.java).apply {
                putExtra(Constants.MACHINE_ID_EXTRA, item.machine.id.toString())
            }
        }
        launcher.launch(intent)
    }

    private val spanCount: Int by lazy {
        val columnWidth = resources.getDimensionPixelSize(R.dimen.machine_tile_width)
        val margin = resources.getDimension(R.dimen.activity_horizontal_margin)
        val displayMetrics = resources.displayMetrics
        val parentWidth = displayMetrics.widthPixels - (margin * 2).toInt()

        val spanCount = parentWidth / columnWidth
        if (spanCount > 0) spanCount else 1
    }
}