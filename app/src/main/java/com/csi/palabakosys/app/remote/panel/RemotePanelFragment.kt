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
import com.csi.palabakosys.app.remote.shared_ui.RemoteActivationViewModel
import com.csi.palabakosys.databinding.FragmentRemotePanelBinding
import com.csi.palabakosys.model.MachineType
import com.google.android.material.tabs.TabLayout
import java.util.UUID

class RemotePanelFragment : Fragment() {
    private lateinit var binding: FragmentRemotePanelBinding
    private val viewModel: RemoteActivationViewModel by activityViewModels()
    private val adapter = RemotePanelAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRemotePanelBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerMachineTiles.adapter = adapter
        subscribeObservers()
        subscribeEvents()

        return binding.root
    }


    private fun subscribeObservers() {
        binding.tabMachineType.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.setMachineType(tab?.text.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        viewModel.selectedTab.observe(viewLifecycleOwner, Observer {
            viewModel.loadMachines(it)
        })
        viewModel.machines.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
            it.filter { m -> m.workerId != null }.forEach { mf ->
                mf.workerId?.let { uuid ->
                    viewModel.pendingWork(uuid).observe(viewLifecycleOwner, Observer { wi ->
                        if(wi != null) {
                            adapter.setConnection(!wi.state.isFinished, null, wi.id)
                        }
                    })
                }
            }
        })
        viewModel.dataState.observe(viewLifecycleOwner, Observer {
            when(it) {
                is RemoteActivationViewModel.DataState.InitiateConnection -> {
                    adapter.setConnection(true, it.machineId, it.workerId)
                    hook(it.machineId, it.workerId)
                }
            }
        })
    }

    private fun hook(machineId: UUID, workerId: UUID) {
        WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(workerId).observe(viewLifecycleOwner, Observer { wi ->
            if(wi != null) {
                adapter.setConnection(!wi.state.isFinished, machineId, workerId)
            }
        })
    }

    private fun subscribeEvents() {
        adapter.onItemClick = {
            viewModel.selectMachine(it.entityMachine)
        }
    }
}