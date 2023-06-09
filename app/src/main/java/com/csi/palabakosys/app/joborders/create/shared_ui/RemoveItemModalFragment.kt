//package com.csi.palabakosys.app.joborders.create.shared_ui
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.csi.palabakosys.databinding.FragmentModalRemoveItemBinding
//import com.csi.palabakosys.fragments.ModalFragment
//
//class RemoveItemModalFragment() : ModalFragment<RemoveItemModel>() {
//
//    private lateinit var binding: FragmentModalRemoveItemBinding
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        // Inflate the layout for this fragment
//        binding = FragmentModalRemoveItemBinding.inflate(inflater, container, false)
//        binding.viewModel = arguments?.getParcelable("data")
//
//        binding.buttonConfirm.setOnClickListener {
//            onOk?.invoke(binding.viewModel!!)
//            dismiss()
//        }
//
//        binding.buttonCancel.setOnClickListener {
//            dismiss()
//        }
//
//        return binding.root
//    }
//
//    companion object {
//        var instance: RemoveItemModalFragment? = null
//        fun getInstance(model: RemoveItemModel) : RemoveItemModalFragment {
//            if(instance == null || instance?.dismissed == true) {
//                instance = RemoveItemModalFragment().apply {
//                    arguments = Bundle().apply {
//                        putParcelable("data", model)
//                    }
//                }
//            }
//            return instance as RemoveItemModalFragment
//        }
//    }
//}