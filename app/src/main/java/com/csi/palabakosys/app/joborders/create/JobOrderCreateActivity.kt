package com.csi.palabakosys.app.joborders.create

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.app.joborders.create.delivery.DeliveryCharge
import com.csi.palabakosys.app.joborders.create.delivery.JOSelectDeliveryActivity
import com.csi.palabakosys.app.joborders.create.products.JOSelectProductsActivity
import com.csi.palabakosys.app.joborders.create.products.JobOrderProductsItemAdapter
import com.csi.palabakosys.app.joborders.create.products.MenuProductItem
import com.csi.palabakosys.app.joborders.create.services.JOSelectWashDryActivity
import com.csi.palabakosys.app.joborders.create.services.JobOrderServiceItemAdapter
import com.csi.palabakosys.app.joborders.create.services.MenuServiceItem
import com.csi.palabakosys.databinding.ActivityJobOrderCreateBinding
import com.csi.palabakosys.util.ActivityLauncher
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobOrderCreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJobOrderCreateBinding
    private val viewModel: CreateJobOrderViewModel by viewModels()

    private val servicesLauncher = ActivityLauncher(this)
    private val productsLauncher = ActivityLauncher(this)
    private val deliveryLauncher = ActivityLauncher(this)

    private val servicesAdapter = JobOrderServiceItemAdapter()
    private val productsAdapter = JobOrderProductsItemAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Create Job Order"
        binding = DataBindingUtil.setContentView(this, R.layout.activity_job_order_create)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.setCustomer(
            intent.getParcelableExtra("customer")
        )

        binding.serviceItems.adapter = servicesAdapter
        binding.productItems.adapter = productsAdapter

        subscribeEvents()
    }

    private fun subscribeEvents() {
        servicesLauncher.onOk = {
            val result = it.data?.getParcelableArrayListExtra<MenuServiceItem>("services")?.toList()
            viewModel.syncServices(result)
        }

        productsLauncher.onOk = {
            val result = it.data?.getParcelableArrayListExtra<MenuProductItem>("products")?.toList()
            viewModel.syncProducts(result)
        }

        deliveryLauncher.onOk = {
            val result = it.data?.getParcelableExtra<DeliveryCharge>("deliveryCharge")
            viewModel.setDeliveryCharge(result)
        }

        servicesAdapter.onItemClick = {
            viewModel.openServices(it)
        }

        productsAdapter.onItemClick = {
            viewModel.openProducts(it)
        }

        binding.cardLegendServices.setOnClickListener {
            viewModel.openServices(null)
        }

        binding.cardLegendProducts.setOnClickListener {
            viewModel.openProducts(null)
        }

        binding.cardLegendDelivery.setOnClickListener {
            viewModel.openDelivery()
        }

        viewModel.jobOrderServices.observe(this, Observer {
            servicesAdapter.setData(it)
        })

        viewModel.jobOrderProducts.observe(this, Observer {
            productsAdapter.setData(it)
        })

        viewModel.dataState().observe(this, Observer {
            when(it) {
                is CreateJobOrderViewModel.DataState.OpenServices -> {
                    openServices(it.list, it.item)
                    viewModel.resetState()
                }
                is CreateJobOrderViewModel.DataState.OpenProducts -> {
                    openProducts(it.list, it.item)
                    viewModel.resetState()
                }
                is CreateJobOrderViewModel.DataState.OpenDelivery -> {
                    openDelivery(it.deliveryCharge)
                    viewModel.resetState()
                }
            }
        })
    }

    private fun openServices(services: List<MenuServiceItem>?, itemPreset: MenuServiceItem?) {
        val intent = Intent(this, JOSelectWashDryActivity::class.java).apply {
            services?.let {
                putParcelableArrayListExtra("services", ArrayList(it))
                putExtra("service", itemPreset)
            }
        }
        servicesLauncher.launch(intent)
    }

    private fun openProducts(products: List<MenuProductItem>?, itemPreset: MenuProductItem?) {
        val intent = Intent(this, JOSelectProductsActivity::class.java).apply {
            products?.let {
                putParcelableArrayListExtra("products", ArrayList(it))
                putExtra("product", itemPreset)
            }
        }
        productsLauncher.launch(intent)
    }

    private fun openDelivery(deliveryCharge: DeliveryCharge?) {
        val intent = Intent(this, JOSelectDeliveryActivity::class.java).apply {
            deliveryCharge?.let {
                putExtra("deliveryCharge", deliveryCharge)
            }
        }
        deliveryLauncher.launch(intent)
    }
}