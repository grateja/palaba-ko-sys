package com.csi.palabakosys.app.joborders.create

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.csi.palabakosys.R
import com.csi.palabakosys.app.joborders.create.ui.NavigationAdapter
import com.csi.palabakosys.databinding.ActivityCreateJobOrderBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED

class CreateJobOrderActivity : AppCompatActivity() {
    private var jobOrderPanelBehavior: BottomSheetBehavior<View>? = null
    private lateinit var binding: ActivityCreateJobOrderBinding
    private val viewModel: CreateJobOrderViewModel by viewModels()

    private val navigationAdapter = NavigationAdapter()
//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Create Job Order"
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_job_order)

        binding.bottomSheetJobOrderPanel?.let {
            jobOrderPanelBehavior = BottomSheetBehavior.from(it)
        }

        viewModel.setCustomer(
            intent.getParcelableExtra("customer")
        )

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.inclJobOrderNavigation?.recyclerJobOrderNavigation?.adapter = navigationAdapter

        binding.bottomNavCreateJobOrder?.setupWithNavController(Navigation.findNavController(this, R.id.nav_host_services).apply {
            addOnDestinationChangedListener {controller, destination, arguments ->
                jobOrderPanelBehavior?.state = STATE_COLLAPSED
            }
        })

        binding.cardBottomSheetExpander?.setOnClickListener {
            jobOrderPanelBehavior?.apply {
                state = if(state == BottomSheetBehavior.STATE_COLLAPSED) {
                    BottomSheetBehavior.STATE_EXPANDED
                } else {
                    BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        }

        subscribeEvents()
    }

    private fun subscribeEvents() {
        viewModel.dataState().observe(this, Observer {
            if(it is DataState.PutService) {
//                jobOrderServicesAdapter.addItem(it.serviceItem)
            } else if(it is DataState.PutProduct) {
//                jobOrderProductsAdapter.addItem(it.product)
            } else if(it is DataState.RequestEditServiceQuantity) {
//                requestModifyQuantity(QuantityModel(
//                    it.serviceItem.id, it.serviceItem.name, it.serviceItem.quantity, QuantityModel.TYPE_SERVICE
//                ))
                viewModel.resetState()
            } else if(it is DataState.RequestEditProductQuantity) {
//                requestModifyQuantity(QuantityModel(
//                    it.serviceItem.id, it.serviceItem.name, it.serviceItem.quantity, QuantityModel.TYPE_PRODUCT
//                ))
                viewModel.resetState()
            } else if(it is DataState.RemoveService) {
//                jobOrderServicesAdapter.removeItem(it.index)
//                viewModel.resetState()
            } else if(it is DataState.RemoveProduct) {
//                jobOrderProductsAdapter.removeItem(it.index)
//                viewModel.resetState()
            }
        })
        viewModel.activeNavigation.observe(this, Observer {
            Navigation.findNavController(this, R.id.nav_host_services).navigate(it)
        })
        navigationAdapter.onItemClick = {
            viewModel.navigate(it.destination)
        }
//        binding.inclJobOrderNavigation.apply {
//            buttonServices.setOnClickListener { navigate(R.id.menuServicesFragment) }
//            buttonProducts.setOnClickListener { navigate(R.id.menuProductsFragment) }
//            buttonDelivery.setOnClickListener { navigate(R.id.menuDeliveryFragment) }
//        }

//        jobOrderServicesAdapter.apply {
//            onSelect = {
//                viewModel.requestModifyServiceQuantity(it.id)
//            }
//            onDeleteRequest = {
//                requestRemoveItem(RemoveItemModel(it.id, it.abbr(), RemoveItemModel.TYPE_SERVICE))
//            }
//        }
//        jobOrderProductsAdapter.apply {
//            onSelect = {
//                viewModel.requestModifyProductsQuantity(it.id)
//            }
//            onDeleteRequest = {
//                requestRemoveItem(RemoveItemModel(it.id, it.name, RemoveItemModel.TYPE_PRODUCT))
//            }
//        }

//        quantityLauncher.onOk = {
//            it.data?.getParcelableExtra<QuantityModel>("data")?.let { data ->
//                if(data.type == QuantityModel.TYPE_SERVICE) {
//                    viewModel.applyServiceQuantity(data)
//                } else if(data.type == QuantityModel.TYPE_PRODUCT) {
//                    viewModel.applyProductQuantity(data)
//                }
//            }
//        }
//        removeItemLauncher.onOk = {
//            it.data?.getParcelableExtra<RemoveItemModel>("data")?.let { data ->
//                if(data.type == QuantityModel.TYPE_SERVICE) {
//                    viewModel.removeService(data.id)
//                } else if(data.type == QuantityModel.TYPE_PRODUCT) {
//                    viewModel.removeProduct(data.id)
//                }
//            }
//        }
    }

//    private fun requestModifyQuantity(quantityModel: QuantityModel) {
//        val intent = Intent(applicationContext, ModifyQuantityActivity::class.java).apply {
//            putExtra("data", quantityModel)
//        }
//        quantityLauncher.launch(intent)
//        viewModel.resetState()
//    }
//
//    private fun requestRemoveItem(removeItemModel: RemoveItemModel) {
//        val intent = Intent(applicationContext, RemoveItemActivity::class.java).apply {
//            putExtra("data", removeItemModel)
//        }
//        removeItemLauncher.launch(intent)
//    }

    private var doubleclick = false
    override fun onBackPressed() {
        if(viewModel.jobOrderServices.size == 0) {
            finish()
        } else if(jobOrderPanelBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
            jobOrderPanelBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        } else if(doubleclick) {
            finish()
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
            doubleclick = true
        }
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleclick = false
        }, 2000)
    }
}