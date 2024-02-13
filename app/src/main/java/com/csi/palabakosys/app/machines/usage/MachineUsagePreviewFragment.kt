package com.csi.palabakosys.app.machines.usage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.csi.palabakosys.databinding.FragmentMachineUsagePreviewBinding
import com.csi.palabakosys.fragments.BaseModalFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MachineUsagePreviewFragment : BaseModalFragment() {
    private val viewModel: MachineUsageViewModel by activityViewModels()
    private lateinit var binding: FragmentMachineUsagePreviewBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMachineUsagePreviewBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
}