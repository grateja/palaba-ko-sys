package com.csi.palabakosys.app.remote.panel

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.work.WorkManager
import com.csi.palabakosys.app.machines.MachinesViewModel
import com.csi.palabakosys.app.remote.shared_ui.RemoteActivationViewModel
import com.csi.palabakosys.databinding.FragmentRemotePanelBinding
import com.csi.palabakosys.model.EnumMachineType
import com.csi.palabakosys.services.MachineActivationService
import com.csi.palabakosys.worker.RemoteWorker
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class RemotePanelFragment : Fragment() {
    private lateinit var binding: FragmentRemotePanelBinding
    private val remoteViewModel: RemoteActivationViewModel by activityViewModels()
    private val viewModel: MachinesViewModel by activityViewModels()
    private val adapter = RemotePanelAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRemotePanelBinding.inflate(inflater, container, false)
        binding.viewModel = remoteViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerMachineTiles.adapter = adapter
        subscribeObservers()
        subscribeEvents()

        return binding.root
    }


    private fun subscribeObservers() {
        binding.inclTab.tabMachineType.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.setMachineType(tab?.text.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        viewModel.machines.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })
    }

    private fun subscribeEvents() {
        adapter.onItemClick = {
            remoteViewModel.selectMachine(it.entityMachine)
        }
    }
}