package com.csi.palabakosys.app.remote.panel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.csi.palabakosys.R
import com.csi.palabakosys.app.remote.shared_ui.RemoteActivationViewModel
import com.csi.palabakosys.databinding.FragmentRemotePanelBinding
import com.csi.palabakosys.model.MachineType
import com.csi.palabakosys.room.entities.EntityMachine
import java.util.UUID

class RemotePanelFragment : Fragment() {
    private lateinit var binding: FragmentRemotePanelBinding
    private val viewModel: RemoteActivationViewModel by activityViewModels()
    private val regularWashersAdapter = RemotePanelAdapter()
    private val regularDryersAdapter = RemotePanelAdapter()
    private val titanWashersAdapter = RemotePanelAdapter()
    private val titanDryersAdapter = RemotePanelAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRemotePanelBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerMachineTiles8kgWashers.adapter = regularWashersAdapter
        binding.recyclerMachineTiles8kgDryers.adapter = regularDryersAdapter
        binding.recyclerMachineTiles12kgWashers.adapter = titanWashersAdapter
        binding.recyclerMachineTiles12kgDryers.adapter = titanDryersAdapter

        subscribeObservers()
        subscribeEvents()

        viewModel.loadMachines()
        return binding.root
    }

    private fun subscribeObservers() {
        viewModel.machines.observe(viewLifecycleOwner, Observer {
            regularWashersAdapter.setData(it.filter { it.machineType == MachineType.REGULAR_WASHER })
            regularDryersAdapter.setData(it.filter { it.machineType == MachineType.REGULAR_DRYER })
            titanWashersAdapter.setData(it.filter { it.machineType == MachineType.TITAN_WASHER })
            titanDryersAdapter.setData(it.filter { it.machineType == MachineType.TITAN_DRYER })
        })
//        viewModel.dataState.observe(viewLifecycleOwner, Observer {
//            when(it) {
//                is RemoteActivationViewModel.DataState.ActivateRequest -> {
//                    hook(it.machine, it.workerId)
//                }
//            }
//        })
    }

    private fun hook(machine: EntityMachine, workerId: UUID) {
        WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(workerId).observe(viewLifecycleOwner, Observer { wi ->
            if(machine.machineType == MachineType.REGULAR_WASHER) {
                refreshList(regularWashersAdapter, machine, wi)
            }
        })
    }

    private fun refreshList(adapter: RemotePanelAdapter, machine: EntityMachine, workInfo: WorkInfo?) {
        adapter.update(machine, workInfo)
    }

    private fun subscribeEvents() {
        regularWashersAdapter.onItemClick = {
            viewModel.selectMachine(it.entityMachine)
        }
        regularDryersAdapter.onItemClick = {
            viewModel.selectMachine(it.entityMachine)
        }
        titanWashersAdapter.onItemClick = {
            viewModel.selectMachine(it.entityMachine)
        }
        titanDryersAdapter.onItemClick = {
            viewModel.selectMachine(it.entityMachine)
        }
    }
}