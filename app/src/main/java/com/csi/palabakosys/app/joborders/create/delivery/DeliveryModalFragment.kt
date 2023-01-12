package com.csi.palabakosys.app.joborders.create.delivery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.csi.palabakosys.databinding.FragmentDeliveryModalBinding
import com.csi.palabakosys.fragments.ModalFragment

class DeliveryModalFragment : ModalFragment<DeliveryCharge>() {
    private lateinit var binding: FragmentDeliveryModalBinding
    private val viewModel: DeliveryViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeliveryModalBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.buttonConfirm.setOnClickListener {
            onOk?.invoke(viewModel.prepareDeliveryCharge())
            dismiss()
        }

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }

        binding.buttonMinus.setOnClickListener {
            binding.textQuantity.apply {
                setText(
                    (text.toString().toFloat().minus(1).also {
                        binding.buttonMinus.isEnabled = it > 1
                    }).toString()
                )
            }
        }
        binding.buttonAdd.setOnClickListener {
            binding.buttonMinus.isEnabled = true
            binding.textQuantity.apply {
                setText(
                    (text.toString().toFloat().plus(1)).toString()
                )
            }
        }

        return binding.root
    }
    companion object {
        var instance: DeliveryModalFragment? = null
        fun newInstance() : DeliveryModalFragment {
            if(instance == null || instance?.dismissed == true) {
                instance = DeliveryModalFragment()
            }
            return instance as DeliveryModalFragment
        }
    }
}