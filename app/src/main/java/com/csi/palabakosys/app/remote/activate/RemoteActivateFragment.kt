package com.csi.palabakosys.app.remote.activate

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.work.WorkInfo
import com.csi.palabakosys.R
import com.csi.palabakosys.app.remote.shared_ui.RemoteActivationViewModel
import com.csi.palabakosys.databinding.FragmentRemoteActivateBinding
import com.csi.palabakosys.fragments.BaseModalFragment
import com.csi.palabakosys.worker.RemoteWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class RemoteActivateFragment : BaseModalFragment() {
    private lateinit var binding: FragmentRemoteActivateBinding
    private val viewModel: RemoteActivationViewModel by activityViewModels()
    private var closeAll = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRemoteActivateBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.buttonActivate.setOnClickListener {
            binding.buttonActivate.visibility = View.GONE
            viewModel.activate()?.observe(viewLifecycleOwner, Observer {
                updateView(it)
                closeAll = it != null && it.state == WorkInfo.State.SUCCEEDED || it.state == WorkInfo.State.RUNNING || it.state == WorkInfo.State.ENQUEUED
            })
        }
        binding.buttonClose.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        if(closeAll) {
            close()
        }
        super.onDismiss(dialog)
    }

    fun close() {
        requireActivity().findNavController(R.id.navHostRemote).navigate(R.id.remotePanelFragment)
    }

    private fun updateView(wi: WorkInfo?) {
        if(wi != null) {
            if(wi.state == WorkInfo.State.RUNNING || wi.state == WorkInfo.State.ENQUEUED) {
                binding.textIndicator.text = "Connecting"
                binding.buttonActivate.visibility = View.GONE
                closeOnTouchOutside = false
            } else {
                binding.textIndicator.text = wi.outputData.getString(RemoteWorker.MESSAGE)
                binding.buttonActivate.visibility = View.VISIBLE
                closeOnTouchOutside = true
            }
        } else {
            binding.textIndicator.text = "Good to go"
            binding.buttonActivate.visibility = View.VISIBLE
            closeOnTouchOutside = true
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.machine.observe(viewLifecycleOwner, Observer {
            hookObserver(it.workerId)
        })
    }

    private fun hookObserver(workerId: UUID?) {
        workerId?.let { uuid ->
            viewModel.pendingWork(uuid).observe(viewLifecycleOwner, Observer { _wi ->
                updateView(_wi)
            })
        }
    }
}