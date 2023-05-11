package com.csi.palabakosys.app.joborders.create.shared_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.csi.palabakosys.databinding.FragmentModalModifyQuantityBinding
import com.csi.palabakosys.fragments.ModalFragment

class ModifyQuantityModalFragment : ModalFragment<QuantityModel>() {

    private lateinit var binding: FragmentModalModifyQuantityBinding
    private val quantityViewModel: QuantityViewModel by viewModels()

    var onItemRemove: ((QuantityModel) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentModalModifyQuantityBinding.inflate(inflater, container, false)
        val data = arguments?.getParcelable<QuantityModel>("data")
        binding.viewModel = data

        if(data?.quantity == 0) {
            binding.buttonRemove.visibility = View.GONE
        }

        binding.buttonConfirm.setOnClickListener {
            if(binding.textQuantity.text.toString().toInt() > 0) {
                onOk?.invoke(binding.viewModel!!)
                dismiss()
            }
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

        binding.buttonRemove.setOnClickListener {
            onItemRemove?.invoke(binding.viewModel!!)
            dismiss()
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