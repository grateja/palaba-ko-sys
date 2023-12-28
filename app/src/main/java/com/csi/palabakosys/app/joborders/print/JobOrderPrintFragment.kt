package com.csi.palabakosys.app.joborders.print

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.databinding.FragmentJobOrderPrintBinding
import com.csi.palabakosys.fragments.BaseModalFragment
import com.csi.palabakosys.model.PrintItem
import com.csi.palabakosys.util.Constants.Companion.ID
import com.csi.palabakosys.util.toUUID
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class JobOrderPrintFragment : BaseModalFragment() {
    private val viewModel: JobOrderPrintViewModel by viewModels()
    private lateinit var binding: FragmentJobOrderPrintBinding

    private val servicesAdapter = Adapter<PrintItem>(R.layout.recycler_item_print_item)
    private val productsAdapter = Adapter<PrintItem>(R.layout.recycler_item_print_item)
    private val extrasAdapter = Adapter<PrintItem>(R.layout.recycler_item_print_item)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobOrderPrintBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        subscribeEvents()
        subscribeListeners()

        setupTab()

        arguments?.getString(ID)?.toUUID()?.let {
            viewModel.setJobOrderId(it)
        }

//        binding.printItemsServices.items.adapter = servicesAdapter
//        binding.printItemsProducts.items.adapter = productsAdapter
//        binding.printItemsExtras.items.adapter = extrasAdapter

        return binding.root
    }

    private fun setupTab() {
        binding.printTab.apply {
            addTab(newTab().setText("Claim stub"))
            addTab(newTab().setText("Machine stub"))
            addTab(newTab().setText("Job Order"))
        }
    }

    private fun subscribeEvents() {
    }

    private fun subscribeListeners() {
        viewModel.jobOrderWithItems.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.services?.let {
                servicesAdapter.setData(it.map {
                    PrintItem(it.quantity, it.serviceName, it.price)
                })
            }
            it?.products?.let {
                productsAdapter.setData(it.map {
                    PrintItem(it.quantity, it.productName, it.price)
                })
            }
            it?.extras?.let {
                extrasAdapter.setData(it.map {
                    PrintItem(it.quantity, it.extrasName, it.price)
                })
            }
        })
    }
}