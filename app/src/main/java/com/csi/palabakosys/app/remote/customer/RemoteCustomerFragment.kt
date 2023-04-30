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
        viewModel.machine.observe(viewLifecycleOwner, Observer {
            hookPending(it.workerId)
        })

        viewModel.customerQueues.observe(viewLifecycleOwner, Observer {
            customerQueuesAdapter.setData(it)
        })

        customerQueuesAdapter.onItemClick = {
            viewModel.selectCustomer(it)
        }

        return binding.root
    }

    private fun hookPending(workId: UUID?) {
        if(workId != null) {
            viewModel.pendingWork(workId).observe(viewLifecycleOwner, Observer { _wi ->
                updateView(_wi != null && _wi.state.isFinished)
            })
        } else {
            updateView(true)
        }
    }

    private fun updateView(isFinished: Boolean) {
        if(isFinished) {
            binding.mainContainer.visibility = View.VISIBLE
            binding.indicatorActive.visibility = View.GONE
            viewModel.getCustomersByMachineType()
        } else {
            binding.mainContainer.visibility = View.GONE
            binding.indicatorActive.visibility = View.VISIBLE
        }
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