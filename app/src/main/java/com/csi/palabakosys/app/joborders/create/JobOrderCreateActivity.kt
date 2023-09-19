package com.csi.palabakosys.app.joborders.create

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
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
import com.csi.palabakosys.app.joborders.create.products.JOSelectProductsActivity
import com.csi.palabakosys.app.joborders.create.products.JobOrderProductsItemAdapter
import com.csi.palabakosys.app.joborders.create.products.MenuProductItem
import com.csi.palabakosys.app.joborders.create.services.JOSelectWashDryActivity
import com.csi.palabakosys.app.joborders.create.services.JobOrderServiceItemAdapter
import com.csi.palabakosys.app.joborders.create.services.MenuServiceItem
import com.csi.palabakosys.app.joborders.list.JobOrderListItem
import com.csi.palabakosys.app.joborders.payment.JobOrderListPaymentAdapter
import com.csi.palabakosys.app.joborders.payment.JobOrderPaymentActivity
import com.csi.palabakosys.app.joborders.payment.JobOrderPaymentMinimal
import com.csi.palabakosys.databinding.ActivityJobOrderCreateBinding
import com.csi.palabakosys.model.EnumActionPermission
import com.csi.palabakosys.util.*
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class JobOrderCreateActivity : BaseActivity() {
    companion object {
        const val JOB_ORDER_ID = "jobOrderId"
        const val CUSTOMER_EXTRA = "customer"
        const val ACTION_LOAD_BY_CUSTOMER_ID = "loadByCustomerId"
        const val ACTION_LOAD_BY_JOB_ORDER_ID = "loadByJobOrderId"
        const val PAYLOAD_EXTRA = "payload"
        const val ITEM_PRESET_EXTRA = "itemPreset"

        const val ACTION_MODIFY_DATETIME = "modifyDateTime"
        const val ACTION_REQUEST_UNLOCK = "requestUnlock"
        const val ACTION_SYNC_PACKAGE = "package"
        const val ACTION_SYNC_SERVICES = "services"
        const val ACTION_SYNC_PRODUCTS = "products"
        const val ACTION_SYNC_EXTRAS = "extras"
        const val ACTION_SYNC_DELIVERY = "delivery"
        const val ACTION_SYNC_DISCOUNT = "discount"
        const val ACTION_SYNC_PAYMENT = "payment"
        const val ACTION_DELETE_JOB_ORDER = "deleteJobOrder"
        const val ACTION_CONFIRM_SAVE = "auth"
    }

    private lateinit var binding: ActivityJobOrderCreateBinding
    private val viewModel: CreateJobOrderViewModel by viewModels()

    private val launcher = ActivityLauncher(this)
//    private val dateTimePicker: DateTimePicker by lazy {
//        DateTimePicker(this)
//    }

    private val servicesAdapter = JobOrderServiceItemAdapter()
    private val productsAdapter = JobOrderProductsItemAdapter()
    private val extrasAdapter = JobOrderExtrasItemAdapter()
    private val unpaidJobOrdersAdapter = Adapter<JobOrderPaymentMinimal>(R.layout.recycler_item_job_order_read_only)
    private val packageAdapter = Adapter<MenuJobOrderPackage>(R.layout.recycler_item_create_job_order_package)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Create Job Order "
        binding = DataBindingUtil.setContentView(this, R.layout.activity_job_order_create)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        if(intent.action == ACTION_LOAD_BY_JOB_ORDER_ID) {
            intent.getStringExtra(JOB_ORDER_ID).toUUID()?.let {
                viewModel.setJobOrder(it)
            }
//            val payload = intent.getParcelableExtra<JobOrderListItem>(PAYLOAD_EXTRA)
//            if(payload != null) {
//                viewModel.setJobOrder(payload)
//            }
        } else if(intent.action == ACTION_LOAD_BY_CUSTOMER_ID) {
            val payload = intent.getParcelableExtra<CustomerMinimal>(CUSTOMER_EXTRA)
            if(payload != null) {
                viewModel.setCustomer(payload)
            }
        }

        binding.inclServicesLegend.recycler.adapter = servicesAdapter
        binding.inclProductsLegend.recycler.adapter = productsAdapter
        binding.inclExtrasLegend.recycler.adapter = extrasAdapter
//        binding.packageItems.adapter = packageAdapter

        subscribeEvents()
    }

//    private fun showDateTimePickerDialog(currentDateTime: Instant) {
//        dateTimePicker.show(currentDateTime)
//
////        val _currentDateTime = currentDateTime.atZone(ZoneId.systemDefault())
////
////        val datePickerDialog = DatePickerDialog(
////            this,
////            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
////                val timePickerDialog = TimePickerDialog(
////                    this,
////                    { _, selectedHourOfDay, selectedMinute ->
////                        val localDateTime = LocalDateTime.of(selectedYear, selectedMonth + 1, selectedDayOfMonth, selectedHourOfDay, selectedMinute)
////                        val instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant()
////
////                        val dateTime = DateTimeFormatter.ofPattern("d/M/yyyy HH:mm").format(localDateTime)
////
////                        Toast.makeText(this, "Selected date and time: $dateTime", Toast.LENGTH_LONG).show()
////
////                        viewModel.applyDateTime(instant)
////                    },
////                    _currentDateTime.hour,
////                    _currentDateTime.minute,
////                    false
////                )
////
////                timePickerDialog.show()
////            },
////            _currentDateTime.year,
////            _currentDateTime.monthValue - 1,
////            _currentDateTime.dayOfMonth
////        )
////
////        datePickerDialog.setCanceledOnTouchOutside(false)
////        datePickerDialog.show()
//    }

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
//        dateTimePicker.setOnDateTimeSelectedListener {
//            viewModel.applyDateTime(it)
//        }
        binding.buttonPackages.setOnClickListener {
            viewModel.openPackages()
        }
//        binding.textCreatedAt.setOnClickListener {
//            openAuthRequestModifyDateTime()
//        }
        binding.lockedPrompt.setOnClickListener {
            openAuthRequestUnlock()
        }

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
                    data.getParcelableArrayListExtra<MenuJobOrderPackage>(JOSelectPackageActivity.PACKAGES)?.toList().let {
                        viewModel.syncPackages(it)
                    }
                }
//                ACTION_MODIFY_DATETIME -> {
//                    viewModel.requestModifyDateTime()
//                }
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
//                    val paymentId = data.getStringExtra(JobOrderPaymentActivity.PAYMENT_ID).toUUID()
//                    val paidJobOrderIds = data.getStringArrayListExtra(JobOrderPaymentActivity.SELECTED_JOB_ORDER_IDS)
                    viewModel.loadPayment()
                }
                ACTION_DELETE_JOB_ORDER -> {
                    finish()
                }
                ACTION_CONFIRM_SAVE -> {
                    data.getParcelableExtra<LoginCredentials>(AuthActionDialogActivity.RESULT)?.let {
                        viewModel.save(it.userId)
                    }
                }
                ACTION_REQUEST_UNLOCK -> {
                    viewModel.unlock()
                }
            }
//            if(result.data?.action == ACTION_SYNC_SERVICES) {
//                result.data?.getParcelableArrayListExtra<MenuServiceItem>(PAYLOAD_EXTRA)?.toList().let {
//                    viewModel.syncServices(it)
//                }
//            }
        }

        servicesAdapter.apply {
            onItemClick = {
                viewModel.openServices(it)
            }
            onDeleteRequest = {
                showDeleteConfirmationDialog("Remove this item?", "Are you sure you want to remove this item from the list?") {
                    viewModel.removeService(it.serviceRefId)
                }
            }
        }


        productsAdapter.apply {
            onItemClick = {
                viewModel.openProducts(it)
            }
            onDeleteRequest = {
                showDeleteConfirmationDialog("Remove this item?", "Are you sure you want to remove this item from the list?") {
                    viewModel.removeProduct(it.productRefId)
                }
            }
        }

        extrasAdapter.apply {
            onItemClick = {
                viewModel.openExtras(it)
            }
            onDeleteRequest = {
                showDeleteConfirmationDialog("Remove this item?", "Are you sure you want to remove this item from the list?") {
                    viewModel.removeExtras(it.extrasRefId)
                }
            }
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

//        binding.inclDeliveryLegend.cardLegend.setOnClickListener {
//            viewModel.openDelivery()
//        }

//        binding.inclDiscountLegend.cardLegend.setOnClickListener {
//            viewModel.openDiscount()
//        }

//        viewModel.jobOrderPackage.observe(this, Observer {
//            packageAdapter.setData(it)
//        })

        viewModel.locked.observe(this, Observer {
            servicesAdapter.lock(it)
            productsAdapter.lock(it)
            extrasAdapter.lock(it)
        })

        viewModel.jobOrderServices.observe(this, Observer {
            servicesAdapter.setData(it.toMutableList())
        })

        viewModel.jobOrderProducts.observe(this, Observer {
            productsAdapter.setData(it.toMutableList())
        })

        viewModel.jobOrderExtras.observe(this, Observer {
            extrasAdapter.setData(it.toMutableList())
        })

        viewModel.unpaidJobOrders.observe(this, Observer {
            unpaidJobOrdersAdapter.setData(it)
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
                    openPackages(it.list)
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
                    binding.root.showSnackBar(it.message)
//                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    viewModel.resetState()
                }
                is CreateJobOrderViewModel.DataState.SaveSuccess -> {
                    binding.root.showSnackBar("Job Order saved successfully!")
//                    Toast.makeText(this, "Job Order saved successfully!", Toast.LENGTH_LONG).show()
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
                    confirmExit(it.canExit, it.resultCode, it.jobOrderId)
                    viewModel.resetState()
                }
//                is CreateJobOrderViewModel.DataState.ModifyDateTime -> {
//                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
//                        showDateTimePickerDialog(it.createdAt)
//                        viewModel.resetState()
//                    }, 100)
//                }
            }
        })
    }

    private fun prepareSubmit() {
        val intent = Intent(this, AuthActionDialogActivity::class.java).apply {
            action = ACTION_CONFIRM_SAVE
            putExtra(AuthActionDialogActivity.MESSAGE, "Authentication Required")
        }
        launcher.launch(intent)
    }

    private fun openPackages(packages: List<MenuJobOrderPackage>?) {
        val intent = Intent(this, JOSelectPackageActivity::class.java).apply {
            action = ACTION_SYNC_PACKAGE
            packages?.let {
                putParcelableArrayListExtra(PAYLOAD_EXTRA, ArrayList(it))
            }
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

//    private fun openAuthRequestModifyDateTime() {
//        val intent = Intent(this, AuthActionDialogActivity::class.java).apply {
//            action = ACTION_MODIFY_DATETIME
//            putExtra(AuthActionDialogActivity.PERMISSIONS_EXTRA, arrayListOf(
//                EnumActionPermission.MODIFY_JOB_ORDERS,
//                EnumActionPermission.MODIFY_SERVICES
//            ))
//        }
//        launcher.launch(intent)
//    }

    private fun openAuthRequestUnlock() {
        val intent = Intent(this, AuthActionDialogActivity::class.java).apply {
            action = ACTION_REQUEST_UNLOCK
        }
        launcher.launch(intent)
    }

    override fun onBackPressed() {
        viewModel.requestExit()
    }

    private fun confirmExit(promptPass: Boolean, resultCode: Int, jobOrderId: UUID?) {
        if((backPressed && !promptPass) || promptPass) {
            setResult(resultCode, Intent(intent.action).apply {
                putExtra(JOB_ORDER_ID, jobOrderId.toString())
            })
            finish()
        } else {
            Toast.makeText(this, "Press back again to revert changes", Toast.LENGTH_LONG).show()
            backPressed = true
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                backPressed = false
            }, 2000)
        }
    }
}