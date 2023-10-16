package com.csi.palabakosys.app.customers.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.csi.palabakosys.databinding.FragmentCustomerDetailsBinding

class CustomerDetailsFragment : Fragment() {
    private lateinit var binding: FragmentCustomerDetailsBinding
    private val viewModel: CustomerPreviewViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomerDetailsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}