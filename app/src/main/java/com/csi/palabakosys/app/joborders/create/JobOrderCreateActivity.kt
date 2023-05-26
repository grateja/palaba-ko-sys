package com.csi.palabakosys.app.joborders.create

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.app.auth.AuthActionDialogActivity
import com.csi.palabakosys.app.auth.LoginCredentials
import com.csi.palabakosys.app.joborders.create.delivery.DeliveryCharge
import com.csi.palabakosys.app.joborders.create.delivery.JOSelectDeliveryActivity
import com.csi.palabakosys.app.joborders.create.discount.JOSelectDiscountActivity
import com.csi.palabakosys.app.joborders.create.discount.MenuDiscount
import com.csi.palabakosys.app.joborders.create.extras.JOSelectExtrasActivity
import com.csi.palabakosys.app.joborders.create.extras.JobOrderExtrasItemAdapter
import com.csi.palabakosys.app.joborders.create.extras.MenuExtrasItem
import com.csi.palabakosys.app.joborders.create.preview.CreateJobOrderPreviewActivity
import com.csi.palabakosys.app.joborders.create.products.JOSelectProductsActivity
import com.csi.palabakosys.app.joborders.create.products.JobOrderProductsItemAdapter
import com.csi.palabakosys.app.joborders.create.products.MenuProductItem
import com.csi.palabakosys.app.joborders.create.services.JOSelectWashDryActivity
import com.csi.palabakosys.app.joborders.create.services.JobOrderServiceItemAdapter
import com.csi.palabakosys.app.joborders.create.services.MenuServiceItem
import com.csi.palabakosys.app.joborders.payment.PaymentJoPreviewActivity
import com.csi.palabakosys.databinding.ActivityJobOrderCreateBinding
import com.csi.palabakosys.util.ActivityLauncher
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class JobOrderCreateActivity : AppCompatActivity() {
    private var backPressed = false
    private lateinit var binding: ActivityJobOrderCreateBinding
    private val viewModel: CreateJobOrderViewModel by viewModels()

    private val servicesLauncher = ActivityLauncher(this)
    private val productsLauncher = ActivityLauncher(this)
    private val deliveryLauncher = ActivityLauncher(this)
    private val extrasLauncher = ActivityLauncher(this)
    private val discountLauncher = ActivityLauncher(this)
    private val previewLauncher = ActivityLauncher(this)
    private val paymentLauncher = ActivityLauncher(this)
    private val authLauncher = ActivityLauncher(this)

    private val servicesAdapter = JobOrderServiceItemAdapter()
    private val productsAdapter = JobOrderProductsItemAdapter()
    private val extrasAdapter = JobOrderExtrasItemAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Create Job Order "
        binding = DataBindingUtil.setContentView(this, R.layout.activity_job_order_create)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.setCustomer(
            intent.getParcelableExtra("customer")
        )

        binding.serviceItems.adapter = servicesAdapter
        binding.productItems.adapter = productsAdapter
        binding.extrasItems.adapter = extrasAdapter

        subscribeEvents()
    }

    private fun subscribeEvents() {
        servicesLauncher.onOk = {
            val selected = it.data?.getParcelableArrayListExtra<MenuServiceItem>("services")?.toList()
            viewModel.syncServices(selected)
        }

        productsLauncher.onOk = {
            val result = it.data?.getParcelableArrayListExtra<MenuProductItem>("products")?.toList()
            viewModel.syncProducts(result)
        }

        extrasLauncher.onOk = {
            val result = it.data?.getParcelableArrayListExtra<MenuExtrasItem>("extras")?.toList()
            viewModel.syncExtras(result)
        }

        deliveryLauncher.onOk = {
            val result = it.data?.getParcelableExtra<DeliveryCharge>("deliveryCharge")
            viewModel.setDeliveryCharge(result)
        }

        discountLauncher.onOk = {
            val result = it.data?.getParcelableExtra<MenuDiscount>("discount")
            viewModel.applyDiscount(result)
        }

        authLauncher.onOk = {
            val result = it.data?.getParcelableExtra<LoginCredentials>(AuthActionDialogActivity.RESULT)?.let {
                viewModel.save(it.userId)
            }
        }

        paymentLauncher.onOk = {
            viewModel.lock()
        }

        servicesAdapter.onItemClick = {
            viewModel.openServices(it)
        }

        productsAdapter.onItemClick = {
            viewModel.openProducts(it)
        }

        extrasAdapter.onItemClick = {
            viewModel.openExtras(it)
        }

        binding.inclServicesLegend.cardLegend.setOnClickListener {
            viewModel.openServices(null)
        }

        binding.inclProductsLegend.cardLegend.setOnClickListener {
            viewModel.openProducts(null)
        }

        binding.inclExtrasLegend.cardLegend.setOnClickListener {
            viewModel.openExtras(null)
        }

        binding.inclDeliveryLegend.cardLegend.setOnClickListener {
            viewModel.openDelivery()
        }

        binding.inclDiscountLegend.cardLegend.setOnClickListener {
            viewModel.openDiscount()
        }

        viewModel.jobOrderServices.observe(this, Observer {
            servicesAdapter.setData(it.toMutableList())
        })

        viewModel.jobOrderProducts.observe(this, Observer {
            productsAdapter.setData(it.toMutableList())
        })

        viewModel.jobOrderExtras.observe(this, Observer {
            extrasAdapter.setData(it)
        })

        binding.buttonSave.setOnClickListener {
            viewModel.validate()
        }
        binding.buttonPayment.setOnClickListener {
            viewModel.openPayment()
        }

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
                is CreateJobOrderViewModel.DataState.OpenExtras -> {
                    openExtras(it.list, it.item)
                    viewModel.resetState()
                }
                is CreateJobOrderViewModel.DataState.OpenDelivery -> {
                    openDelivery(it.deliveryCharge)
                    viewModel.resetState()
                }
                is CreateJobOrderViewModel.DataState.OpenDiscount -> {
                    openDiscount(it.discount)
                    viewModel.resetState()
                }
                is CreateJobOrderViewModel.DataState.ProceedToSaveJO -> {
                    prepareSubmit()
                    viewModel.resetState()
                }
                is CreateJobOrderViewModel.DataState.InvalidOperation -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    viewModel.resetState()
                }
                is CreateJobOrderViewModel.DataState.SaveSuccess -> {
                    Toast.makeText(this, "Job Order saved successfully!", Toast.LENGTH_LONG).show()
//                    previewJobOrder(it.jobOrderId, it.customerId)
                    viewModel.resetState()
                }
                is CreateJobOrderViewModel.DataState.OpenPayment -> {
                    openPayment(it.jobOrderId, it.customerId)
                    viewModel.resetState()
                }
                is CreateJobOrderViewModel.DataState.RequestExit -> {
                    if(!it.canExit && !backPressed) {
                        Toast.makeText(this, "Press back again to revert changes", Toast.LENGTH_LONG).show()
                        backPressed = true
                        Handler(Looper.getMainLooper()).postDelayed(Runnable {
                            backPressed = false
                        }, 2000)
                    } else {
                        finish()
                    }
                    viewModel.resetState()
                }
            }
        })
    }

    private fun prepareSubmit() {
        val intent = Intent(this, AuthActionDialogActivity::class.java).apply {
            putExtra(AuthActionDialogActivity.MESSAGE, "Authentication Required")
        }
        authLauncher.launch(intent)
    }

    private fun previewJobOrder(jobOrderId: UUID, customerId: UUID) {
        val intent = Intent(this, CreateJobOrderPreviewActivity::class.java).apply {
            putExtra("jobOrderId", jobOrderId.toString())
            putExtra("customerId", customerId.toString())
        }
        previewLauncher.launch(intent)
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

    private fun openExtras(extras: List<MenuExtrasItem>?, itemPreset: MenuExtrasItem?) {
        val intent = Intent(this, JOSelectExtrasActivity::class.java).apply {
            extras?.let {
                putParcelableArrayListExtra("extras", ArrayList(it))
                putExtra("extra", itemPreset)
            }
        }
        extrasLauncher.launch(intent)
    }

    private fun openDelivery(deliveryCharge: DeliveryCharge?) {
        val intent = Intent(this, JOSelectDeliveryActivity::class.java).apply {
            deliveryCharge?.let {
                putExtra("deliveryCharge", deliveryCharge)
            }
        }
        deliveryLauncher.launch(intent)
    }

    private fun openDiscount(discount: MenuDiscount?) {
        val intent = Intent(this, JOSelectDiscountActivity::class.java).apply {
            discount?.let {
                putExtra("discount", discount)
            }
        }
        discountLauncher.launch(intent)
    }

    private fun openPayment(jobOrderId: UUID, customerId: UUID) {
        val intent = Intent(this, PaymentJoPreviewActivity::class.java).apply {
            putExtra(PaymentJoPreviewActivity.CUSTOMER_ID, customerId.toString())
            putExtra(PaymentJoPreviewActivity.JOB_ORDER_ID, jobOrderId.toString())
        }
        paymentLauncher.launch(intent)
    }

    override fun onBackPressed() {
        // super.onBackPressed()
        viewModel.requestExit()
    }
}