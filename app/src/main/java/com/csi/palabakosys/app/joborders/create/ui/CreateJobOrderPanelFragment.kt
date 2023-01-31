package com.csi.palabakosys.app.joborders.create.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.app.joborders.create.products.JobOrderProductsItemAdapter
import com.csi.palabakosys.app.joborders.create.services.JobOrderServiceItemAdapter
import com.csi.palabakosys.app.joborders.create.CreateJobOrderViewModel
//import com.csi.palabakosys.app.joborders.create.DataState
import com.csi.palabakosys.databinding.FragmentCreateJobOrderPanelBinding

class CreateJobOrderPanelFragment : Fragment(R.layout.fragment_create_job_order_panel) {
    private val viewModel: CreateJobOrderViewModel by activityViewModels()
    private lateinit var binding: FragmentCreateJobOrderPanelBinding

    private val jobOrderServicesAdapter = JobOrderServiceItemAdapter()
    private val jobOrderProductsAdapter = JobOrderProductsItemAdapter()

    private lateinit var modifyQuantityDialog: ModifyQuantityModalFragment
    private lateinit var removeItemDialog: RemoveItemModalFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateJobOrderPanelBinding.bind(view)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.serviceItems.adapter = jobOrderServicesAdapter
        binding.productItems.adapter = jobOrderProductsAdapter

//        jobOrderServicesAdapter.setData(viewModel.jobOrderServices)
//        jobOrderProductsAdapter.setData(viewModel.jobOrderProducts)

        subscribeEvents()
    }
    private fun subscribeEvents() {
//        viewModel.dataState().observe(viewLifecycleOwner, Observer {
//            if(it is DataState.PutService) {
//                jobOrderServicesAdapter.addItem(it.serviceItem)
////                binding.serviceItems.scheduleLayoutAnimation()
//            } else if(it is DataState.PutProduct) {
//                jobOrderProductsAdapter.addItem(it.product)
//            } else if(it is DataState.RequestEditServiceQuantity) {
//                requestModifyQuantity(
//                    QuantityModel(
//                        it.serviceItem.id, it.serviceItem.name, it.serviceItem.quantity, QuantityModel.TYPE_SERVICE
//                    )
//                )
//                viewModel.resetState()
//            } else if(it is DataState.RequestEditProductQuantity) {
//                requestModifyQuantity(
//                    QuantityModel(
//                        it.serviceItem.id, it.serviceItem.name, it.serviceItem.quantity, QuantityModel.TYPE_PRODUCT
//                    )
//                )
//                viewModel.resetState()
//            } else if(it is DataState.RemoveService) {
//                jobOrderServicesAdapter.removeItem(it.index)
//            } else if(it is DataState.RemoveProduct) {
//                jobOrderProductsAdapter.removeItem(it.index)
//            }
//        })

        jobOrderServicesAdapter.apply {
            onItemClick = {
                viewModel.requestModifyServiceQuantity(it.id)
            }
            onDeleteRequest = {
                requestRemoveItem(RemoveItemModel(it.id, it.abbr(), RemoveItemModel.TYPE_SERVICE, it.quantity))
            }
        }
        jobOrderProductsAdapter.apply {
            onItemClick = {
                viewModel.requestModifyProductsQuantity(it.id)
            }
            onDeleteRequest = {
                requestRemoveItem(RemoveItemModel(it.id, it.name, RemoveItemModel.TYPE_PRODUCT, it.quantity))
            }
        }

        binding.buttonDelete.setOnClickListener {
            viewModel.selectDeliveryProfile(null)
        }
    }

    private fun requestModifyQuantity(quantityModel: QuantityModel) {
        modifyQuantityDialog = ModifyQuantityModalFragment.getInstance(quantityModel).apply {
            onOk = {
                if (it.type == QuantityModel.TYPE_SERVICE) {
                    viewModel.applyServiceQuantity(it)
                } else if (it.type == QuantityModel.TYPE_PRODUCT) {
                    viewModel.applyProductQuantity(it)
                }
            }
        }
        modifyQuantityDialog.show(childFragmentManager, this.toString())
    }

    private fun requestRemoveItem(removeItemModel: RemoveItemModel) {
        removeItemDialog = RemoveItemModalFragment.getInstance(removeItemModel).apply {
            onOk = {
                if(it.type == QuantityModel.TYPE_SERVICE) {
                    viewModel.removeService(it.id)
                } else if(it.type == QuantityModel.TYPE_PRODUCT) {
                    viewModel.removeProduct(it.id)
                }
            }
        }
        removeItemDialog.show(childFragmentManager, this.toString())
    }
}