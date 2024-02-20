package com.csi.palabakosys.app.customers.create

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.csi.palabakosys.app.customers.CustomerMinimal
import com.csi.palabakosys.app.joborders.create.customer.SelectCustomerActivity
import com.csi.palabakosys.app.joborders.payment.JobOrderPaymentActivity.Companion.CUSTOMER_ID
import com.csi.palabakosys.databinding.FragmentAddEditCustomerBinding
import com.csi.palabakosys.fragments.ModalFragment
import com.csi.palabakosys.util.DataState
import com.csi.palabakosys.util.FragmentLauncher
import com.csi.palabakosys.util.hideKeyboard
import com.csi.palabakosys.util.toUUID
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddEditCustomerFragment : ModalFragment<CustomerMinimal?>() {
    private val viewModel: AddEditCustomerViewModel by viewModels()
    private lateinit var binding: FragmentAddEditCustomerBinding
    private val launcher = FragmentLauncher(this)

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
            val name = arguments?.getString("name")
            viewModel.get(it.toUUID(), name)
        }
        arguments?.getBoolean("showSearchButton")?.let {
            viewModel.toggleSearchVisibility(it)
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
            viewModel.validate()
        }
//        binding.cardButtonSearch.setOnClickListener {
//            val intent = Intent(context, SelectCustomerActivity::class.java)
//            launcher.launch(intent)
//        }
        launcher.onOk = {
            it?.getStringExtra(CUSTOMER_ID).toUUID()?.let {
                viewModel.replace(it)
            }
        }
    }

    private fun subscribeListeners() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer {
            when(it) {
                is DataState.ValidationPassed -> {
                    viewModel.save()
                }
                is DataState.SaveSuccess -> {
                    onOk?.invoke(CustomerMinimal(
                        it.data.id,
                        it.data.name!!,
                        it.data.crn!!,
                        it.data.address,
                        0,
                        null,
                        null
                    ))
                    viewModel.resetState()
                    dismiss()
                }
                is DataState.InvalidInput -> {
                    binding.progressBarSaving.visibility = View.INVISIBLE
                }
                is DataState.RequestExit -> {
                    if(it.promptPass) {
                        dismiss()
                    } else {
                        Toast.makeText(context, "Press back again to revert changes", Toast.LENGTH_LONG).show()
                    }
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
        const val CUSTOMER_ID_EXTRA = "data"
        var instance: AddEditCustomerFragment? = null
        fun getInstance(customerId: UUID?, presetName: String?, showSearchButton: Boolean) : AddEditCustomerFragment {
            if(instance == null || instance?.dismissed == true) {
                instance = AddEditCustomerFragment().apply {
                    arguments = Bundle().apply {
                        putString("data", customerId.toString())
                        putString("name", presetName)
                        putBoolean("showSearchButton", showSearchButton)
                    }
                }
            }
            return instance as AddEditCustomerFragment
        }
    }
}