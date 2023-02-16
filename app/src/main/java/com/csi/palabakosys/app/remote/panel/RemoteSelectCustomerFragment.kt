package com.csi.palabakosys.app.remote.panel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.databinding.FragmentRemoteSelectCustomerBinding
import com.csi.palabakosys.fragments.BaseModalFragment
import com.csi.palabakosys.room.entities.EntityAvailableService
import com.csi.palabakosys.room.entities.EntityCustomerQueueService
import com.csi.palabakosys.worker.RemoteWorker

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

        serviceQueuesAdapter.onItemClick = { service ->
            viewModel.activate(service).observe(viewLifecycleOwner, Observer {
                checkResult(it)
            })
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.pendingResult()?.observe(viewLifecycleOwner, Observer {
            if(!it.state.isFinished) {
                checkResult(it)
                println("It's already finished")
            }
            println("this is hooked")
        })
    }

    private fun checkResult(workInfo: WorkInfo) {
        val message = workInfo.outputData.getString(RemoteWorker.MESSAGE)

        if(workInfo.state == WorkInfo.State.FAILED) {
            binding.cardCover.visibility = View.GONE
        } else if(workInfo.state == WorkInfo.State.ENQUEUED) {
            binding.cardCover.visibility = View.VISIBLE
        } else if(workInfo.state == WorkInfo.State.SUCCEEDED) {
            binding.cardCover.visibility = View.GONE
            dismiss()
        }
        Toast.makeText(context, message + workInfo.state.toString(), Toast.LENGTH_LONG).show()
        println("state")
        println(workInfo.state)
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