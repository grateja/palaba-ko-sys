package com.csi.palabakosys.app.customers.preview

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.csi.palabakosys.app.joborders.create.JobOrderCreateActivity
import com.csi.palabakosys.app.joborders.create.JobOrderCreateActivity.Companion.ACTION_LOAD_BY_CUSTOMER_ID
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

        binding.buttonEdit.setOnClickListener {
            viewModel.showCustomer()
        }
        binding.buttonCreateNewJobOrder.setOnClickListener {
            if(activity?.callingActivity?.className == JobOrderCreateActivity::class.java.name) {
                activity?.finish()
            } else {
                viewModel.prepareNewJobOrder()
            }
        }

        return binding.root
    }
}