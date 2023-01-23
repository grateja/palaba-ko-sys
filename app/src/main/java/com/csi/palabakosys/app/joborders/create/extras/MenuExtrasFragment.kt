package com.csi.palabakosys.app.joborders.create.extras

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.app.joborders.create.CreateJobOrderViewModel
import com.csi.palabakosys.databinding.FragmentMenuExtrasBinding

class MenuExtrasFragment : Fragment(R.layout.fragment_menu_extras) {
    private lateinit var binding: FragmentMenuExtrasBinding
    private val viewModel: CreateJobOrderViewModel by activityViewModels()

    private val adapter = AvailableExtrasAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentMenuExtrasBinding.bind(view)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.recyclerOtherServices.adapter = adapter

        subscribeEvents()
        viewModel.loadOtherServices()
    }

    fun subscribeEvents() {
        viewModel.availableOtherServices.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })
    }
}