package com.csi.palabakosys.app.joborders.create.products

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.csi.palabakosys.R
//import com.csi.palabakosys.app.joborders.create.RemoveItemActivity
import com.csi.palabakosys.app.joborders.create.CreateJobOrderViewModel
//import com.csi.palabakosys.app.joborders.create.DataState
import com.csi.palabakosys.databinding.FragmentMenuProductsBinding

class MenuProductsFragment : Fragment(R.layout.fragment_menu_products) {
    private lateinit var binding: FragmentMenuProductsBinding
    private val viewModel: CreateJobOrderViewModel by activityViewModels()

    private val availableProductsAdapter = AvailableProductsAdapter()

//    private val removeItemLauncher = FragmentLauncher(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMenuProductsBinding.bind(view)
        binding.recyclerAvailableProducts.adapter = availableProductsAdapter
        binding.recyclerAvailableProducts.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)

        subscribeEvents()
        viewModel.loadProducts()
    }

    private fun subscribeEvents() {
        viewModel.availableProducts.observe(viewLifecycleOwner, Observer {
            availableProductsAdapter.setData(it)
        })
        availableProductsAdapter.apply {
            onItemClick = { selectProduct(it) }
//            onDelete = { requestDelete(it) }
        }
//        viewModel.dataState().observe(viewLifecycleOwner, Observer {
//            when(it) {
//                is DataState.PutProduct -> {
//                    availableProductsAdapter.select(it.product)
//                }
//                is DataState.RemoveProduct -> {
//                    availableProductsAdapter.deselect(it.id)
//                } else -> {
//                }
//            }
//        })
//        removeItemLauncher.onOk = {
//            it.data?.getParcelableExtra<RemoveItemModel>("data")?.let { data ->
//                viewModel.removeProduct(data.id)
//            }
//        }
    }

    override fun onStart() {
        super.onStart()
//        viewModel.loadProducts()
    }

//    private fun requestDelete(product: MenuProductItem) {
//        val intent = Intent(context, RemoveItemActivity::class.java).apply {
//            putExtra("data", RemoveItemModel(
//                product.id, product.name, RemoveItemModel.TYPE_PRODUCT
//            ))
//        }
//        removeItemLauncher.launch(intent)
//    }

    private fun selectProduct(product: MenuProductItem) {
        if(!product.selected) {
            viewModel.putProduct(product.apply {
                quantity = 1
            })
        } else {
            viewModel.requestModifyProductsQuantity(product.id)
        }
    }
}