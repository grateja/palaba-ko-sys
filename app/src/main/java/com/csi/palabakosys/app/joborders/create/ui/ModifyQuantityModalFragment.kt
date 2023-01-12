package com.csi.palabakosys.app.joborders.create.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.csi.palabakosys.databinding.FragmentModifyQuantityModalBinding
import com.csi.palabakosys.fragments.ModalFragment

class ModifyQuantityModalFragment : ModalFragment<QuantityModel>() {

    private lateinit var binding: FragmentModifyQuantityModalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentModifyQuantityModalBinding.inflate(inflater, container, false)
        binding.viewModel = arguments?.getParcelable("data")

        binding.buttonConfirm.setOnClickListener {
            onOk?.invoke(binding.viewModel!!)
            dismiss()
        }

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }

        binding.buttonMinus.setOnClickListener {
            binding.textQuantity.apply {
                setText(
                    (text.toString().toInt().minus(1).also {
                        binding.buttonMinus.isEnabled = it > 1
                    }).toString()
                )
            }
        }
        binding.buttonAdd.setOnClickListener {
            binding.buttonMinus.isEnabled = true
            binding.textQuantity.apply {
                setText(
                    (text.toString().toInt().plus(1)).toString()
                )
            }
        }

        return binding.root
    }

    companion object {
        var instance: ModifyQuantityModalFragment? = null
        fun getInstance(model: QuantityModel) : ModifyQuantityModalFragment {
            if(instance == null || instance?.dismissed == true) {
                instance = ModifyQuantityModalFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable("data", model)
                    }
                }
            }
            return instance as ModifyQuantityModalFragment
        }
    }
}