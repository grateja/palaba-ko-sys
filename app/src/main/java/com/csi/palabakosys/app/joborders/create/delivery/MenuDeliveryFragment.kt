package com.csi.palabakosys.app.joborders.create.delivery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.app.joborders.create.CreateJobOrderViewModel
import com.csi.palabakosys.databinding.FragmentMenuDeliveryBinding

class MenuDeliveryFragment : Fragment(R.layout.fragment_menu_delivery) {

    private lateinit var binding: FragmentMenuDeliveryBinding
    private val viewModel: CreateJobOrderViewModel by activityViewModels()
    private val deliveryViewModel: DeliveryViewModel by activityViewModels()

    private val deliveryVehiclesAdapter = DeliveryVehiclesAdapter()

    private lateinit var deliveryProfileModal: DeliveryModalFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMenuDeliveryBinding.bind(view)
        binding.viewModel = viewModel
        binding.recyclerDeliveryVehicles.adapter = deliveryVehiclesAdapter
        subscribeEvents()

        // TODO: 05/12/2022
        // on delivery profile select, show bottom sheet;
        // show input distance/km
        // on ok, apply selected vehicle profile with distance
    }

    private fun subscribeEvents() {
        deliveryVehiclesAdapter.setData(deliveryViewModel.deliveryProfiles)
        deliveryVehiclesAdapter.onItemClick = {
            deliveryViewModel.setDeliveryProfile(it)
            showOptions()
        }
        viewModel.deliveryCharge.observe(viewLifecycleOwner, Observer {
            deliveryVehiclesAdapter.notifySelection(it?.deliveryProfile)
        })
    }

    private fun showOptions() {
        deliveryProfileModal = DeliveryModalFragment.newInstance().apply {
            onOk = {
                viewModel.selectDeliveryProfile(it)
            }
        }
        deliveryProfileModal.show(childFragmentManager, this.toString())
    }
}