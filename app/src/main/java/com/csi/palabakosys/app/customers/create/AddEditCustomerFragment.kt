package com.csi.palabakosys.app.customers.create

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.csi.palabakosys.app.customers.CustomerMinimal
import com.csi.palabakosys.databinding.FragmentAddEditCustomerBinding
import com.csi.palabakosys.fragments.ModalFragment
import com.csi.palabakosys.util.DataState
import com.csi.palabakosys.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditCustomerFragment : ModalFragment<CustomerMinimal?>() {
    private val viewModel: AddEditCustomerViewModel by viewModels()
    private lateinit var binding: FragmentAddEditCustomerBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(false)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditCustomerBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.buttonClose.setOnClickListener {
            dismiss()
        }
        binding.buttonSave.setOnClickListener {
            binding.progressBarSaving.visibility = View.VISIBLE
            it.hideKeyboard()
            viewModel.save()
        }
        val id = arguments?.getString("data")
        viewModel.get(id)
        viewModel.dataState.observe(viewLifecycleOwner, Observer {
            if(it is DataState.Success) {
                onOk?.invoke(CustomerMinimal(
                    it.data.id.toString(),
                    it.data.name!!,
                    it.data.crn!!,
                    it.data.address
                ))
                viewModel.resetState()
                dismiss()
            } else if(it is DataState.InvalidInput) {
                binding.progressBarSaving.visibility = View.INVISIBLE
            }
            binding.progressBarSaving.visibility = View.INVISIBLE
        })
        return binding.root
    }

    companion object {
        var instance: AddEditCustomerFragment? = null
        fun getInstance(model: String?) : AddEditCustomerFragment {
            if(instance == null || instance?.dismissed == true) {
                instance = AddEditCustomerFragment().apply {
                    arguments = Bundle().apply {
                        putString("data", model)
                    }
                }
            }
            return instance as AddEditCustomerFragment
        }
    }
}