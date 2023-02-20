package com.csi.palabakosys.app.remote.queues

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.remote.panel.RemotePanelViewModel
import com.csi.palabakosys.databinding.FragmentRemoteQueuesBinding
import com.csi.palabakosys.fragments.BaseModalFragment
import com.csi.palabakosys.room.entities.EntityAvailableService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemoteQueuesFragment : BaseModalFragment() {
    private lateinit var binding: FragmentRemoteQueuesBinding
    private val queuesViewModel: RemoteQueuesViewModel by viewModels()
    private val viewModel: RemotePanelViewModel by activityViewModels()

    private val serviceQueuesAdapter = Adapter<EntityAvailableService>(R.layout.recycler_item_queue_service)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRemoteQueuesBinding.inflate(inflater, container, false)
        binding.recyclerAvailableServices.adapter = serviceQueuesAdapter
        binding.viewModel = queuesViewModel

        viewModel.queueService.observe(viewLifecycleOwner, Observer {
//            queuesViewModel.getAvailableServicesByCustomerId(it.customerId, it.machineType)
        })

        queuesViewModel.availableServices.observe(viewLifecycleOwner, Observer {
            serviceQueuesAdapter.setData(it)
        })

        serviceQueuesAdapter.onItemClick = {
//            viewModel.activate(it)
        }

        return binding.root
    }

    companion object {
        var instance: RemoteQueuesFragment? = null
        fun newInstance() : RemoteQueuesFragment {
            if(instance == null || instance?.dismissed == true) {
                instance = RemoteQueuesFragment()
            }
            return instance as RemoteQueuesFragment
        }
    }
}