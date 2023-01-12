package com.csi.palabakosys.app.joborders.create.services

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
//import com.csi.palabakosys.app.joborders.create.RemoveItemActivity
import com.csi.palabakosys.app.joborders.create.CreateJobOrderViewModel
import com.csi.palabakosys.app.joborders.create.DataState
import com.csi.palabakosys.databinding.FragmentMenuServicesBinding
import com.csi.palabakosys.model.MachineType

class MenuServicesFragment : Fragment(R.layout.fragment_menu_services) {
    private lateinit var binding: FragmentMenuServicesBinding
    private val viewModel: CreateJobOrderViewModel by activityViewModels()
    private val available8KGWashServices = AvailableServicesAdapter()
    private val available8KGDryServices = AvailableServicesAdapter()
    private val available12KGWashServices = AvailableServicesAdapter()
    private val available12KGDryServices = AvailableServicesAdapter()

//    private val removeItemLauncher = FragmentLauncher(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMenuServicesBinding.bind(view)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.inclMenu8KGWashers.recyclerAvailableServices.adapter = available8KGWashServices
        binding.inclMenu8KGDryers.recyclerAvailableServices.adapter = available8KGDryServices
        binding.inclMenu12KGWashers.recyclerAvailableServices.adapter = available12KGWashServices
        binding.inclMenu12KGDryers.recyclerAvailableServices.adapter = available12KGDryServices

        binding.inclMenu8KGWashers.viewModel = MachineType.REGULAR_WASHER
        binding.inclMenu8KGDryers.viewModel = MachineType.REGULAR_DRYER
        binding.inclMenu12KGWashers.viewModel = MachineType.TITAN_WASHER
        binding.inclMenu12KGDryers.viewModel = MachineType.TITAN_DRYER

        subscribeEvents()
        println("services fragment created")
        viewModel.loadServices()
    }

    override fun onStart() {
        super.onStart()
//        viewModel.loadServices()
    }

//    override fun onStart() {
//        super.onStart()
//        AsyncLayoutInflater(requireContext()).inflate(R.layout.fragment_menu_services, binding.root.findViewById(android.R.id.content)) { c, _, _ ->
//            println("Galing mo")
//        }
//    }
//
    private fun subscribeEvents() {
        viewModel.available8kWashServices.observe(viewLifecycleOwner, Observer {
            available8KGWashServices.setData(it)
        })
        viewModel.available8kDryServices.observe(viewLifecycleOwner, Observer {
            available8KGDryServices.setData(it)
        })
        viewModel.available12kWashServices.observe(viewLifecycleOwner, Observer {
            available12KGWashServices.setData(it)
        })
        viewModel.available12kDryServices.observe(viewLifecycleOwner, Observer {
            available12KGDryServices.setData(it)
        })
        viewModel.dataState().observe(viewLifecycleOwner, Observer {
            if(it is DataState.RemoveService) {
                available8KGDryServices.deselect(it.id)
                available8KGWashServices.deselect(it.id)
                available12KGDryServices.deselect(it.id)
                available12KGWashServices.deselect(it.id)
//                viewModel.resetState()
            } else if(it is DataState.PutService) {
                available8KGDryServices.select(it.serviceItem)
                available8KGWashServices.select(it.serviceItem)
                available12KGDryServices.select(it.serviceItem)
                available12KGWashServices.select(it.serviceItem)
            }
        })

//        removeItemLauncher.onOk = {
//            it.data?.getParcelableExtra<RemoveItemModel>("data")?.let { data ->
//                viewModel.removeService(data.id)
//            }
//        }

        available8KGDryServices.apply {
//            onDelete = { requestDelete(it) }
            onItemClick = { selectService(it) }
        }
        available8KGWashServices.apply {
//            onDelete = { requestDelete(it) }
            onItemClick = { selectService(it) }
        }
        available12KGDryServices.apply {
//            onDelete = { requestDelete(it) }
            onItemClick = { selectService(it) }
        }
        available12KGWashServices.apply {
//            onDelete = { requestDelete(it) }
            onItemClick = { selectService(it) }
        }
    }

//    private fun requestDelete(service: MenuServiceItem) {
//        val intent = Intent(context, RemoveItemActivity::class.java).apply {
//            putExtra("data", RemoveItemModel(
//                service.id, service.name, RemoveItemModel.TYPE_SERVICE
//            ))
//        }
//        removeItemLauncher.launch(intent)
//    }

    private fun selectService(service: MenuServiceItem) {
        if(!service.selected) {
            viewModel.putService(service.apply {
                quantity = 1
            })
        } else {
            viewModel.requestModifyServiceQuantity(service.id)
        }
    }
}