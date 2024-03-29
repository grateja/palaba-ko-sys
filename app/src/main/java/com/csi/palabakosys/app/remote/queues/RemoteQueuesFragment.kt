package com.csi.palabakosys.app.remote.queues

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.remote.shared_ui.RemoteActivationViewModel
import com.csi.palabakosys.databinding.FragmentRemoteQueuesBinding
import com.csi.palabakosys.fragments.BaseModalFragment
import com.csi.palabakosys.room.entities.EntityAvailableService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemoteQueuesFragment : BaseModalFragment() {
    private lateinit var binding: FragmentRemoteQueuesBinding
    private val viewModel: RemoteActivationViewModel by activityViewModels()

    private val serviceQueuesAdapter = Adapter<EntityAvailableService>(R.layout.recycler_item_queue_service)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRemoteQueuesBinding.inflate(inflater, container, false)
        binding.recyclerAvailableServices.adapter = serviceQueuesAdapter
        binding.viewModel = viewModel

        viewModel.availableServices.observe(viewLifecycleOwner, Observer {
            serviceQueuesAdapter.setData(it)
        })

        serviceQueuesAdapter.onItemClick = {
            viewModel.selectService(it)
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