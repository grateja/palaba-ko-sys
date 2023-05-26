package com.csi.palabakosys.app.joborders.payment.cashless

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.databinding.FragmentModalPaymentJoCashlessBinding
import com.csi.palabakosys.fragments.BaseModalFragment
import com.csi.palabakosys.room.entities.EntityCashless

class PaymentJoCashlessModalFragment : BaseModalFragment() {
    private lateinit var binding: FragmentModalPaymentJoCashlessBinding
    private val viewModel: CashlessPaymentViewModel by viewModels()

    var onSubmit: ((EntityCashless) -> Unit) ? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentModalPaymentJoCashlessBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        subscribeEvents()
        subscribeListeners()

        return binding.root
    }

    private fun subscribeEvents() {
        binding.buttonOk.setOnClickListener {
            viewModel.prepareSubmit()
        }
    }

    private fun subscribeListeners() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer {
            when(it) {
                is CashlessPaymentViewModel.DataState.Submit -> {
                    onSubmit?.invoke(it.cashless)
                    dismiss()
                }
            }
        })
    }

    companion object {
        var instance: PaymentJoCashlessModalFragment? = null
        fun getInstance(cashless: EntityCashless?) : PaymentJoCashlessModalFragment {
            if(instance == null || instance?.dismissed == true) {
                instance = PaymentJoCashlessModalFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable("data", cashless)
                    }
                }
            }
            return instance as PaymentJoCashlessModalFragment
        }
    }
}