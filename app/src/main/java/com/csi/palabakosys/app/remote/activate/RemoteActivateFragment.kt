package com.csi.palabakosys.app.remote.activate

import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.work.WorkInfo
import com.csi.palabakosys.R
import com.csi.palabakosys.app.remote.shared_ui.RemoteActivationViewModel
import com.csi.palabakosys.databinding.FragmentRemoteActivateBinding
import com.csi.palabakosys.fragments.BaseModalFragment
import com.csi.palabakosys.model.MachineActivationQueues
import com.csi.palabakosys.model.MachineConnectionStatus
import com.csi.palabakosys.services.MachineActivationService
//import com.csi.palabakosys.worker.RemoteWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class RemoteActivateFragment : BaseModalFragment() {
    private lateinit var binding: FragmentRemoteActivateBinding
    private val viewModel: RemoteActivationViewModel by activityViewModels()
    private var closeAll = false
    private var machineId: UUID? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRemoteActivateBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.buttonActivate.setOnClickListener {
//            viewModel.activate()
            dismiss()
        }
        binding.buttonClose.setOnClickListener {
            dismiss()
        }
//        viewModel.machine.observe(viewLifecycleOwner, Observer {
//            machineId = it.id
//            val intent = Intent(requireContext(), MachineActivationService::class.java).apply {
//                putExtra(MachineActivationService.CHECK_ONLY_EXTRA, true)
//                putExtra(MachineActivationService.MACHINE_ID_EXTRA, it.id.toString())
//            }
//            requireContext().startForegroundService(intent)
//        })
        return binding.root
    }

//    private val receiver = object: BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            val message = intent?.getStringExtra(MachineActivationService.MESSAGE_EXTRA)
//            val action = intent?.action
//            if(action == MachineActivationService.MACHINE_ACTIVATION) {
//                val queue = intent.getParcelableExtra<MachineActivationQueues>(MachineActivationService.PENDING_QUEUES_EXTRA)
//                if(queue?.machineId != machineId) return
//                binding.inclRemoteQueues.viewModel = queue
//                when(queue?.status) {
//                    MachineConnectionStatus.CONNECTING -> {
//                        binding.buttonActivate.visibility = View.GONE
//                        closeAll = true
//                    }
//                    MachineConnectionStatus.SUCCESS -> {
//                        binding.buttonActivate.visibility = View.GONE
//                        closeAll = true
//                    }
//                    MachineConnectionStatus.FAILED -> {
//                        binding.buttonActivate.visibility = View.VISIBLE
//                    }
//                    else -> {}
//                }
//                Toast.makeText(requireContext(), queue?.message ?: "", Toast.LENGTH_LONG).show()
//            }
//        }
//    }

    override fun onDismiss(dialog: DialogInterface) {
        if(closeAll) {
            close()
        }
//        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
        super.onDismiss(dialog)
    }

    fun close() {
        try {
            activity.let {
                it?.findNavController(R.id.navHostRemote)
                    ?.popBackStack(R.id.remotePanelFragment, true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
//        val intentFilter = IntentFilter().apply {
//            addAction(MachineActivationService.MACHINE_ACTIVATION)
//            addAction(MachineActivationService.INPUT_INVALID_ACTION)
//        }
//        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver, intentFilter)
    }
}