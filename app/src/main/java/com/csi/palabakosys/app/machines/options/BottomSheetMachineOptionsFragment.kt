package com.csi.palabakosys.app.machines.options

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.csi.palabakosys.app.machines.MachineListItem
import com.csi.palabakosys.app.machines.addedit.MachinesAddEditActivity
import com.csi.palabakosys.app.machines.usage.MachineUsageActivity
import com.csi.palabakosys.databinding.FragmentBottomSheetMachineOptionsBinding
import com.csi.palabakosys.fragments.ModalFragment
import com.csi.palabakosys.util.Constants
import com.csi.palabakosys.util.Constants.Companion.MACHINE_ID_EXTRA
import com.csi.palabakosys.util.toUUID
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class BottomSheetMachineOptionsFragment : ModalFragment<MachineListItem>() {
    private lateinit var binding: FragmentBottomSheetMachineOptionsBinding
    private val viewModel: MachineOptionsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomSheetMachineOptionsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        arguments?.getString(Constants.MACHINE_ID_EXTRA)?.toUUID()?.let {
            viewModel.setMachineId(it)
        }

        subscribeEvents()
        subscribeListeners()

        return binding.root
    }

    private fun subscribeEvents() {
        binding.inclUsageHistory.card.setOnClickListener {
            viewModel.openMachineHistory()
        }
        binding.inclConfigure.card.setOnClickListener {
            viewModel.openMachineConfiguration()
        }
    }

    private fun subscribeListeners() {
        viewModel.navigationState.observe(viewLifecycleOwner, Observer {
            val intent = when(it) {
                is MachineOptionsViewModel.NavigationState.OpenMachineHistory -> {
                    Intent(context, MachineUsageActivity::class.java).apply {
                        putExtra(MACHINE_ID_EXTRA, it.machineId.toString())
                    }
                }
                is MachineOptionsViewModel.NavigationState.OpenMachineConfiguration -> {
                    Intent(context, MachinesAddEditActivity::class.java).apply {
                        putExtra(MACHINE_ID_EXTRA, it.machineId.toString())
                    }
                }
                else -> {
                    null
                }
            }
            intent?.let { _intent ->
                startActivity(_intent)
                viewModel.resetState()
            }
        })
    }

    companion object {
        var instance: BottomSheetMachineOptionsFragment? = null
        fun getInstance(machineId: UUID): BottomSheetMachineOptionsFragment {
            if(instance == null || instance?.dismissed == true) {
                instance = BottomSheetMachineOptionsFragment().apply {
                    arguments = Bundle().apply {
                        putString(Constants.MACHINE_ID_EXTRA, machineId.toString())
                    }
                }
            }
            return instance as BottomSheetMachineOptionsFragment
        }
    }
}