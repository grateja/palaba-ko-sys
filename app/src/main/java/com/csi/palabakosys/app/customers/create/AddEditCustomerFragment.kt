package com.csi.palabakosys.app.customers.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.csi.palabakosys.app.customers.CustomerMinimal
import com.csi.palabakosys.databinding.FragmentAddEditCustomerBinding
import com.csi.palabakosys.fragments.ModalFragment
import com.csi.palabakosys.util.DataState
import com.csi.palabakosys.util.hideKeyboard
import com.csi.palabakosys.util.toUUID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditCustomerFragment : ModalFragment<CustomerMinimal?>() {
    private val viewModel: AddEditCustomerViewModel by viewModels()
    private lateinit var binding: FragmentAddEditCustomerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        closeOnTouchOutside = false

        binding = FragmentAddEditCustomerBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        subscribeEvents()
        subscribeListeners()

        arguments?.getString("data").let {
            viewModel.get(it.toUUID())
        }
        return binding.root
    }

    private fun subscribeEvents() {
        binding.buttonClose.setOnClickListener {
            dismiss()
        }
        binding.buttonSave.setOnClickListener {
            binding.progressBarSaving.visibility = View.VISIBLE
            it.hideKeyboard()
            viewModel.save()
        }
    }

    private fun subscribeListeners() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer {
            if(it is DataState.Save) {
                onOk?.invoke(CustomerMinimal(
                    it.data.id,
                    it.data.name!!,
                    it.data.crn!!,
                    it.data.address,
                    null
                ))
                viewModel.resetState()
                dismiss()
            } else if(it is DataState.InvalidInput) {
                binding.progressBarSaving.visibility = View.INVISIBLE
            } else if(it is DataState.RequestExit) {
                if(it.promptPass) {
                    dismiss()
                } else {
                    Toast.makeText(context, "Press back again to revert changes", Toast.LENGTH_LONG).show()
                }
            }
            binding.progressBarSaving.visibility = View.INVISIBLE
        })
    }

    override fun onStart() {
        super.onStart()
        val touchOutsideView = dialog?.window?.decorView?.findViewById<View>(com.google.android.material.R.id.touch_outside)
        touchOutsideView?.setOnClickListener {
            if(!it.hideKeyboard()) {
                viewModel.requestExit()
            }
        }
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