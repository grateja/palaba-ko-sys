package com.csi.palabakosys.app.joborders.create.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.csi.palabakosys.app.joborders.create.CreateJobOrderViewModel
import com.csi.palabakosys.databinding.FragmentSelectCustomerOptionsBinding
import com.csi.palabakosys.fragments.BaseModalFragment
import com.csi.palabakosys.util.DataState
import com.csi.palabakosys.util.toUUID
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SelectCustomerOptionsFragment : BaseModalFragment() {
    private lateinit var binding: FragmentSelectCustomerOptionsBinding
//    private val viewModel: SelectCustomerOptionsViewModel by viewModels()
    private val viewModel: CreateJobOrderViewModel by activityViewModels()

//    var onOk: ((String, UUID?) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectCustomerOptionsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

//        binding.cardAddNew.setOnClickListener {
//            viewModel.editCustomer(true)
//            dismiss()
//        }
//        binding.cardEdit.setOnClickListener {
//            viewModel.editCustomer(false)
//            dismiss()
//        }
        binding.cardSearch.setOnClickListener {
//            viewModel.searchCustomer()
            dismiss()
        }

        return binding.root
    }
}