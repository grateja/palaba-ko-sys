package com.csi.palabakosys.app.remote.panel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.databinding.FragmentRemoteSelectCustomerBinding
import com.csi.palabakosys.fragments.BaseModalFragment
import com.csi.palabakosys.room.entities.EntityAvailableService
import com.csi.palabakosys.room.entities.EntityCustomerQueueService

class RemoteSelectCustomerFragment : BaseModalFragment() {

    private lateinit var binding: FragmentRemoteSelectCustomerBinding
    private val viewModel: RemotePanelViewModel by activityViewModels()
    private val customerQueuesAdapter = Adapter<EntityCustomerQueueService>(R.layout.recycler_item_queue_customer)
    private val serviceQueuesAdapter = Adapter<EntityAvailableService>(R.layout.recycler_item_queue_service)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRemoteSelectCustomerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.recyclerCustomerQueues.adapter = customerQueuesAdapter
        binding.recyclerServiceQueues.adapter = serviceQueuesAdapter
        binding.buttonClose.setOnClickListener {
            dismiss()
        }
        binding.buttonBack.setOnClickListener {
            viewModel.clearCustomer()
        }

        viewModel.customerQueues.observe(viewLifecycleOwner, Observer {
            customerQueuesAdapter.setData(it)
        })

        viewModel.availableServices.observe(viewLifecycleOwner, Observer {
            serviceQueuesAdapter.setData(it)
        })

        customerQueuesAdapter.onItemClick = {
            viewModel.selectJobOrder(it)
        }

        serviceQueuesAdapter.onItemClick = {
            viewModel.activate(it).observe(viewLifecycleOwner, Observer {
                println("state")
                println(it.state)
            })
        }

        return binding.root
    }

    companion object {
        var instance: RemoteSelectCustomerFragment? = null
        fun newInstance() : RemoteSelectCustomerFragment {
            if(instance == null || instance?.dismissed == true) {
                instance = RemoteSelectCustomerFragment()
            }
            return instance as RemoteSelectCustomerFragment
        }
    }
}