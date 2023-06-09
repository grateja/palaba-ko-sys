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
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.auth.AuthActionDialogActivity
import com.csi.palabakosys.app.auth.LoginCredentials
import com.csi.palabakosys.app.customers.CustomerMinimal
import com.csi.palabakosys.app.joborders.cancel.JobOrderCancelActivity
import com.csi.palabakosys.app.joborders.create.delivery.DeliveryCharge
import com.csi.palabakosys.app.joborders.create.delivery.JOSelectDeliveryActivity
import com.csi.palabakosys.app.joborders.create.discount.JOSelectDiscountActivity
import com.csi.palabakosys.app.joborders.create.discount.MenuDiscount
import com.csi.palabakosys.app.joborders.create.extras.JOSelectExtrasActivity
import com.csi.palabakosys.app.joborders.create.extras.JobOrderExtrasItemAdapter
import com.csi.palabakosys.app.joborders.create.extras.MenuExtrasItem
import com.csi.palabakosys.app.joborders.create.packages.JOSelectPackageActivity
import com.csi.palabakosys.app.joborders.create.packages.MenuJobOrderPackage
import com.csi.palabakosys.app.joborders.preview.JobOrderPreviewActivity
import com.csi.palabakosys.app.joborders.create.products.JOSelectProductsActivity
import com.csi.palabakosys.app.joborders.create.products.JobOrderProductsItemAdapter
import com.csi.palabakosys.app.joborders.create.products.MenuProductItem
import com.csi.palabakosys.app.joborders.create.services.JOSelectWashDryActivity
import com.csi.palabakosys.app.joborders.create.services.JobOrderServiceItemAdapter
import com.csi.palabakosys.app.joborders.create.services.MenuServiceItem
import com.csi.palabakosys.app.joborders.list.JobOrderListItem
import com.csi.palabakosys.app.joborders.payment.JobOrderPaymentActivity
import com.csi.palabakosys.databinding.ActivityJobOrderCreateBinding
import com.csi.palabakosys.util.ActivityLauncher
import com.csi.palabakosys.util.BaseActivity
import com.csi.palabakosys.util.toUUID
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class JobOrderCreateActivity : BaseActivity() {
    companion object {
        const val ACTION_LOAD_BY_CUSTOMER_ID = "loadByCustomerId"
        const val ACTION_LOAD_BY_JOB_ORDER_ID = "loadByJobOrderId"
        const val PAYLOAD_EXTRA = "payload"
        const val ITEM_PRESET_EXTRA = "itemPreset"

        const val ACTION_SYNC_PACKAGE = "package"
        const val ACTION_SYNC_SERVICES = "services"
        const val ACTION_SYNC_PRODUCTS = "products"
        const val ACTION_SYNC_EXTRAS = "extras"
        const val ACTION_SYNC_DELIVERY = "delivery"
        const val ACTION_SYNC_DISCOUNT = "discount"
        const val ACTION_SYNC_PAYMENT = "payment"
        const val ACTION_DELETE_JOB_ORDER = "deleteJobOrder"
        const val ACTION_AUTH = "auth"
    }

    private lateinit var binding: ActivityJobOrderCreateBinding
    private val viewModel: CreateJobOrderViewModel by viewModels()

    private val launcher = ActivityLauncher(this)

//    private val packageLauncher = ActivityLauncher(this)
//    private val servicesLauncher = ActivityLauncher(this)
//    private val productsLauncher = ActivityLauncher(this)
//    private val extrasLauncher = ActivityLauncher(this)
//    private val deliveryLauncher = ActivityLauncher(this)
//    private val discountLauncher = ActivityLauncher(this)
//    private val paymentLauncher = ActivityLauncher(this)
//    private val jobOrderCancelLauncher = ActivityLauncher(this)
//    private val authLauncher = ActivityLauncher(this)

    private val servicesAdapter = JobOrderServiceItemAdapter()
    private val productsAdapter = JobOrderProductsItemAdapter()
    private val extrasAdapter = JobOrderExtrasItemAdapter()
    private val packageAdapter = Adapter<MenuJobOrderPackage>(R.layout.recycler_item_create_job_order_package)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Create Job Order "
        binding = DataBindingUtil.setContentView(this, R.layout.activity_job_order_create)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        if(intent.action == ACTION_LOAD_BY_JOB_ORDER_ID) {
            val payload = intent.getParcelableExtra<JobOrderListItem>(PAYLOAD_EXTRA)
            if(payload != null) {
                viewModel.setJobOrder(payload)
            }
        } else if(intent.action == ACTION_LOAD_BY_CUSTOMER_ID) {
            val payload = intent.getParcelableExtra<CustomerMinimal>(PAYLOAD_EXTRA)
            if(payload != null) {
                viewModel.setCustomer(payload)
            }
        }

        binding.serviceItems.adapter = servicesAdapter
        binding.productItems.adapter = productsAdapter
        binding.extrasItems.adapter = extrasAdapter
//        binding.packageItems.adapter = packageAdapter

        subscribeEvents()
    }

    private fun subscribeEvents() {
//        packageLauncher.onOk = { result ->
//            result.data?.getParcelableArrayListExtra<MenuServiceItem>(JOSelectPackageActivity.SERVICES)?.toList().let {
//                viewModel.syncServices(it)
//            }
//            result.data?.getParcelableArrayListExtra<MenuExtrasItem>(JOSelectPackageActivity.EXTRAS)?.toList().let {
//                viewModel.syncExtras(it)
//            }
//            result.data?.getParcelableArrayListExtra<MenuProductItem>(JOSelectPackageActivity.PRODUCTS)?.toList().let {
//                viewModel.syncProducts(it)
//            }
//        }

        launcher.onOk = { result ->
            val data = result.data
            when(data?.action) {
                ACTION_SYNC_PACKAGE -> {
                    data.getParcelableArrayListExtra<MenuServiceItem>(JOSelectPackageActivity.SERVICES)?.toList().let {
                        viewModel.syncServices(it)
                    }
                    data.getParcelableArrayListExtra<MenuExtrasItem>(JOSelectPackageActivity.EXTRAS)?.toList().let {
                        viewModel.syncExtras(it)
                    }
                    data.getParcelableArrayListExtra<MenuProductItem>(JOSelectPackageActivity.PRODUCTS)?.toList().let {
                        viewModel.syncProducts(it)
                    }
                }
                ACTION_SYNC_SERVICES -> {
                    data.getParcelableArrayListExtra<MenuServiceItem>(PAYLOAD_EXTRA)?.toList().let {
                        viewModel.syncServices(it)
                    }
                }
                ACTION_SYNC_PRODUCTS -> {
                    data.getParcelableArrayListExtra<MenuProductItem>(PAYLOAD_EXTRA)?.toList().let {
                        viewModel.syncProducts(it)
                    }
                }
                ACTION_SYNC_EXTRAS -> {
                    data.getParcelableArrayListExtra<MenuExtrasItem>(PAYLOAD_EXTRA)?.toList().let {
                        viewModel.syncExtras(it)
                    }
                }
                ACTION_SYNC_DELIVERY -> {
                    data.getParcelableExtra<DeliveryCharge>(PAYLOAD_EXTRA).let {
                        viewModel.setDeliveryCharge(it)
                    }
                }
                ACTION_SYNC_DISCOUNT -> {
                    data.getParcelableExtra<MenuDiscount>(PAYLOAD_EXTRA).let {
                        viewModel.applyDiscount(it)
                    }
                }
                ACTION_SYNC_PAYMENT -> {
                    val paymentId = data.getStringExtra(JobOrderPaymentActivity.PAYMENT_ID).toUUID()
                    viewModel.loadPayment(paymentId)
                }
                ACTION_DELETE_JOB_ORDER -> {
                    finish()
                }
                ACTION_AUTH -> {
                    data.getParcelableExtra<LoginCredentials>(AuthActionDialogActivity.RESULT)?.let {
                        viewModel.save(it.userId)
                    }
                }
            }
//            if(result.data?.action == ACTION_SYNC_SERVICES) {
//                result.data?.getParcelableArrayListExtra<MenuServiceItem>(PAYLOAD_EXTRA)?.toList().let {
//                    viewModel.syncServices(it)
//                }
//            }
        }

//        servicesLauncher.onOk = {
//            val selected = it.data?.getParcelableArrayListExtra<MenuServiceItem>("services")?.toList()
//            viewModel.syncServices(selected)
//        }

//        productsLauncher.onOk = {
//            val result = it.data?.getParcelableArrayListExtra<MenuProductItem>("products")?.toList()
//            viewModel.syncProducts(result)
//        }

//        extrasLauncher.onOk = {
//            val result = it.data?.getParcelableArrayListExtra<MenuExtrasItem>("extras")?.toList()
//            viewModel.syncExtras(result)
//        }

//        deliveryLauncher.onOk = {
//            val result = it.data?.getParcelableExtra<DeliveryCharge>("deliveryCharge")
//            viewModel.setDeliveryCharge(result)
//        }

//        discountLauncher.onOk = {
//            val result = it.data?.getParcelableExtra<MenuDiscount>("discount")
//            viewModel.applyDiscount(result)
//        }

//        authLauncher.onOk = {
//            val result = it.data.getParcelableExtra<LoginCredentials>(AuthActionDialogActivity.RESULT)?.let {
//                viewModel.save(it.userId)
//            }
//        }

//        paymentLauncher.onOk = {
//            val paymentId = it.data?.getStringExtra(JobOrderPaymentActivity.PAYMENT_ID).toUUID()
//            viewModel.loadPayment(paymentId)
//        }

//        jobOrderCancelLauncher.onOk = {
//            if(it.data?.action == JobOrderCancelActivity.ACTION_DELETE_JOB_ORDER) {
//                finish()
//            }
//        }

//        packageAdapter.onItemClick = {
//            viewModel.openPackages(it)
//        }

        servicesAdapter.onItemClick = {
            viewModel.openServices(it)
        }

        productsAdapter.onItemClick = {
            viewModel.openProducts(it)
        }

        extrasAdapter.onItemClick = {
            viewModel.openExtras(it)
        }

//        binding.inclPackagesLegend.cardLegend.setOnClickListener {
//            viewModel.openPackages(null)
//        }

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

//        viewModel.jobOrderPackage.observe(this, Observer {
//            packageAdapter.setData(it)
//        })

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
        binding.buttonVoid.setOnClickListener {
            viewModel.requestCancel()
        }

        viewModel.dataState().observe(this, Observer {
            when(it) {
                is CreateJobOrderViewModel.DataState.OpenPackages -> {
                    openPackages()
                    viewModel.resetState()
                }
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
                    openPayment(it.customerId, it.paymentId)
                    viewModel.resetState()
                }
                is CreateJobOrderViewModel.DataState.RequestCancel -> {
                    val intent = Intent(this, JobOrderCancelActivity::class.java).apply {
                        action = ACTION_DELETE_JOB_ORDER
                        putExtra(JobOrderCancelActivity.JOB_ORDER_ID, it.jobOrderId.toString())
                    }
                    launcher.launch(intent)
                    viewModel.resetState()
                }
                is CreateJobOrderViewModel.DataState.RequestExit -> {
                    confirmExit(it.canExit)
//                    if(!it.canExit && !backPressed) {
//                        Toast.makeText(this, "Press back again to revert changes", Toast.LENGTH_LONG).show()
//                        backPressed = true
//                        Handler(Looper.getMainLooper()).postDelayed(Runnable {
//                            backPressed = false
//                        }, 2000)
//                    } else {
//                        finish()
//                    }
                    viewModel.resetState()
                }
            }
        })
    }

    private fun prepareSubmit() {
        val intent = Intent(this, AuthActionDialogActivity::class.java).apply {
            action = ACTION_AUTH
            putExtra(AuthActionDialogActivity.MESSAGE, "Authentication Required")
        }
        launcher.launch(intent)
    }

    private fun openPackages() {
        val intent = Intent(this, JOSelectPackageActivity::class.java).apply {
            action = ACTION_SYNC_PACKAGE
        }
        launcher.launch(intent)
    }

    private fun openServices(services: List<MenuServiceItem>?, itemPreset: MenuServiceItem?) {
        val intent = Intent(this, JOSelectWashDryActivity::class.java).apply {
            action = ACTION_SYNC_SERVICES
            services?.let {
                putParcelableArrayListExtra(PAYLOAD_EXTRA, ArrayList(it))
                putExtra(ITEM_PRESET_EXTRA, itemPreset)
            }
        }
        launcher.launch(intent)
    }

    private fun openProducts(products: List<MenuProductItem>?, itemPreset: MenuProductItem?) {
        val intent = Intent(this, JOSelectProductsActivity::class.java).apply {
            action = ACTION_SYNC_PRODUCTS
            products?.let {
                putParcelableArrayListExtra(PAYLOAD_EXTRA, ArrayList(it))
                putExtra(ITEM_PRESET_EXTRA, itemPreset)
            }
        }
        launcher.launch(intent)
    }

    private fun openExtras(extras: List<MenuExtrasItem>?, itemPreset: MenuExtrasItem?) {
        val intent = Intent(this, JOSelectExtrasActivity::class.java).apply {
            action = ACTION_SYNC_EXTRAS
            extras?.let {
                putParcelableArrayListExtra(PAYLOAD_EXTRA, ArrayList(it))
                putExtra(ITEM_PRESET_EXTRA, itemPreset)
            }
        }
        launcher.launch(intent)
    }

    private fun openDelivery(deliveryCharge: DeliveryCharge?) {
        val intent = Intent(this, JOSelectDeliveryActivity::class.java).apply {
            action = ACTION_SYNC_DELIVERY
            deliveryCharge?.let {
                putExtra(PAYLOAD_EXTRA, deliveryCharge)
            }
        }
        launcher.launch(intent)
    }

    private fun openDiscount(discount: MenuDiscount?) {
        val intent = Intent(this, JOSelectDiscountActivity::class.java).apply {
            action = ACTION_SYNC_DISCOUNT
            discount?.let {
                putExtra(PAYLOAD_EXTRA, discount)
            }
        }
        launcher.launch(intent)
    }

    private fun openPayment(customerId: UUID, paymentId: UUID?) {
        val intent = Intent(this, JobOrderPaymentActivity::class.java).apply {
            action = ACTION_SYNC_PAYMENT
            putExtra(JobOrderPaymentActivity.CUSTOMER_ID, customerId.toString())
            putExtra(JobOrderPaymentActivity.PAYMENT_ID, paymentId.toString())
        }
        launcher.launch(intent)
    }

    override fun onBackPressed() {
        viewModel.requestExit()
    }
}