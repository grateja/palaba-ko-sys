package com.csi.palabakosys.app.remote.customer

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
import com.csi.palabakosys.databinding.FragmentRemoteCustomerBinding
import com.csi.palabakosys.fragments.BaseModalFragment
import com.csi.palabakosys.room.entities.EntityAvailableService
import com.csi.palabakosys.room.entities.EntityCustomerQueueService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemoteCustomerFragment : BaseModalFragment() {

    private lateinit var binding: FragmentRemoteCustomerBinding
    private val viewModel: RemotePanelViewModel by activityViewModels()
    private val remoteCustomerViewModel: RemoteCustomerViewModel by viewModels()

    private val customerQueuesAdapter = Adapter<EntityCustomerQueueService>(R.layout.recycler_item_queue_customer)
    private val serviceQueuesAdapter = Adapter<EntityAvailableService>(R.layout.recycler_item_queue_service)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRemoteCustomerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.recyclerCustomerQueues.adapter = customerQueuesAdapter
//        binding.recyclerServiceQueues.adapter = serviceQueuesAdapter
        binding.buttonClose.setOnClickListener {
            dismiss()
        }
//        binding.buttonBack.setOnClickListener {
//            viewModel.clearCustomer()
//        }

        viewModel.machine.observe(viewLifecycleOwner, Observer {
//            remoteCustomerViewModel.getCustomersByMachineType(it)
        })

        remoteCustomerViewModel.customerQueues.observe(viewLifecycleOwner, Observer {
            customerQueuesAdapter.setData(it)
        })

//        remoteCustomerViewModel .availableServices.observe(viewLifecycleOwner, Observer {
//            serviceQueuesAdapter.setData(it)
//        })

        customerQueuesAdapter.onItemClick = {
            viewModel.selectCustomer(it)
        }

//        serviceQueuesAdapter.onItemClick = { service ->
//            viewModel.activate(service)
//            dismiss()
//            viewModel.activate(service).observe(viewLifecycleOwner, Observer {
//                checkResult(it)
//            })
//        }
//        viewModel.machine.observe(viewLifecycleOwner, Observer { _machine ->
//            _machine.workerId?.let { workerId ->
//                viewModel.pendingResult(workerId).observe(viewLifecycleOwner, Observer {
//                    println("SET COVER $workerId")
//                })
//            }
//        })

        return binding.root
    }

//    private fun checkResult(workInfo: WorkInfo) {
//        val message = workInfo.outputData.getString(RemoteWorker.MESSAGE)
//
//        if(workInfo.state == WorkInfo.State.FAILED) {
//            binding.cardCover.visibility = View.GONE
//        } else if(workInfo.state == WorkInfo.State.ENQUEUED) {
//            binding.cardCover.visibility = View.VISIBLE
//        } else if(workInfo.state == WorkInfo.State.SUCCEEDED) {
//            binding.cardCover.visibility = View.GONE
//            dismiss()
//        }
//        Toast.makeText(context, message + workInfo.state.toString(), Toast.LENGTH_LONG).show()
//        println("state")
//        println(workInfo.state)
//    }

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