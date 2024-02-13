package com.csi.palabakosys.app.joborders.create.delivery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.OptionsAdapter
import com.csi.palabakosys.databinding.FragmentModalDeliveryBinding
import com.csi.palabakosys.fragments.BaseModalFragment
import com.csi.palabakosys.model.EnumDeliveryOption
import com.csi.palabakosys.util.DataState

class DeliveryModalFragment : BaseModalFragment() {
    private lateinit var binding: FragmentModalDeliveryBinding
    private val viewModel: DeliveryViewModel by activityViewModels()
    private val deliveryOptionAdapter = OptionsAdapter<EnumDeliveryOption>(
        R.layout.recycler_item_delivery_option,
        EnumDeliveryOption.values()
    ) //DeliveryOptionAdapter()

    var onOk: (() -> Unit) ? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentModalDeliveryBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.deliveryOption.observe(viewLifecycleOwner, Observer {
            deliveryOptionAdapter.selectOption(it)
        })

        binding.recyclerDeliveryOptions.adapter = deliveryOptionAdapter
        binding.quantity.maxValue = 100
        binding.quantity.minValue = 1

        binding.recyclerDeliveryOptions.layoutManager = GridLayoutManager(context, 3)

        binding.buttonConfirm.setOnClickListener {
//            try {
//                val distance = binding.textQuantity.text.toString()
//                if(distance.toFloat() == 0f) {
//                    binding.textQuantity.setText("1")
//                }
//            } catch (e: Exception) {
//                binding.textQuantity.setText("1")
//            }
            onOk?.invoke()
        }

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }

//        binding.buttonMinus.setOnClickListener {
//            binding.textQuantity.apply {
//                setText(
//                    (text.toString().toFloat().minus(1).also {
//                        binding.buttonMinus.isEnabled = it > 1
//                    }).toString()
//                )
//            }
//        }

//        binding.buttonAdd.setOnClickListener {
//            binding.buttonMinus.isEnabled = true
//            binding.textQuantity.apply {
//                setText(
//                    (text.toString().toFloat().plus(1)).toString()
//                )
//            }
//        }
        deliveryOptionAdapter.onSelect = {
            viewModel.setDeliveryOption(it)
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