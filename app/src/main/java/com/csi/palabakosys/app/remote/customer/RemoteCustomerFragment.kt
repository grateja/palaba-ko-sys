package com.csi.palabakosys.app.remote.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.remote.shared_ui.RemoteActivationViewModel
import com.csi.palabakosys.databinding.FragmentRemoteCustomerBinding
import com.csi.palabakosys.fragments.BaseModalFragment
import com.csi.palabakosys.room.entities.EntityAvailableService
import com.csi.palabakosys.room.entities.EntityCustomerQueueService
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class RemoteCustomerFragment : BaseModalFragment() {

    private lateinit var binding: FragmentRemoteCustomerBinding
    private val viewModel: RemoteActivationViewModel by activityViewModels()

    private val customerQueuesAdapter = Adapter<EntityCustomerQueueService>(R.layout.recycler_item_queue_customer)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRemoteCustomerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.recyclerCustomerQueues.adapter = customerQueuesAdapter
        binding.buttonClose.setOnClickListener {
            dismiss()
        }

        viewModel.customerQueues.observe(viewLifecycleOwner, Observer {
            customerQueuesAdapter.setData(it)
        })

        customerQueuesAdapter.onItemClick = {
            viewModel.selectCustomer(it)
        }

        return binding.root
    }

    companion object {
        var instance: RemoteCustomerFragment? = null
        fun newInstance() : RemoteCustomerFragment {
            if(instance == null || instance?.dismissed == true) {
                instance = RemoteCustomerFragment()
            }
            return instance as RemoteCustomerFragment
        }
    }
}