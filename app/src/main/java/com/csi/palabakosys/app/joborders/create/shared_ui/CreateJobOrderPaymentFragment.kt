package com.csi.palabakosys.app.joborders.create.shared_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.joborders.create.CreateJobOrderViewModel
import com.csi.palabakosys.app.joborders.list.JobOrderListItem
import com.csi.palabakosys.app.joborders.payment.preview.PaymentPreviewViewModel
import com.csi.palabakosys.databinding.FragmentCreateJobOrderPaymentBinding

class CreateJobOrderPaymentFragment : Fragment() {
    private val viewModel: PaymentPreviewViewModel by activityViewModels()
    private lateinit var binding: FragmentCreateJobOrderPaymentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateJobOrderPaymentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
}