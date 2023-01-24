package com.csi.palabakosys.app.joborders.create

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.app.customers.CustomerMinimal
import com.csi.palabakosys.app.joborders.create.delivery.DeliveryCharge
import com.csi.palabakosys.app.joborders.create.extras.MenuExtrasItem
import com.csi.palabakosys.app.joborders.create.products.MenuProductItem
import com.csi.palabakosys.app.joborders.create.services.MenuServiceItem
import com.csi.palabakosys.app.joborders.create.ui.QuantityModel
import com.csi.palabakosys.model.MachineType
import com.csi.palabakosys.model.ProductType
import com.csi.palabakosys.model.WashType
import com.csi.palabakosys.util.MeasureUnit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateJobOrderViewModel

@Inject
constructor() : ViewModel() {
    sealed class DataState {
        object StateLess: DataState()
        data class OpenServices(val list: List<MenuServiceItem>?, val item: MenuServiceItem?): DataState()
    }
    private val _dataState = MutableLiveData<DataState>()
    fun dataState(): MutableLiveData<DataState> {
        return _dataState
    }
    fun resetState() {
        _dataState.value = DataState.StateLess
    }

    val activeNavigation = MutableLiveData<Int>()

    val jobOrderNumber = MutableLiveData("#0909776")
    val currentCustomer = MutableLiveData<CustomerMinimal>()
    val deliveryCharge = MutableLiveData<DeliveryCharge?>()
    val jobOrderServices = MutableLiveData<MutableList<MenuServiceItem>>()
    val hasServices = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = jobOrderServices.value?.size!! > 0
        }
        addSource(jobOrderServices) { update() }
    }
    val hasProducts = MutableLiveData(false)
    val hasDelivery = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = deliveryCharge.value != null
        }
        addSource(deliveryCharge) { update() }
    }
    val hasAny = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = hasServices.value!! || hasProducts.value!!
        }
        addSource(hasServices) { update() }
        addSource(hasProducts) { update() }
    }

    val available8kWashServices = MutableLiveData<List<MenuServiceItem>>()
    val available8kDryServices = MutableLiveData<List<MenuServiceItem>>()
    val available12kWashServices = MutableLiveData<List<MenuServiceItem>>()
    val available12kDryServices = MutableLiveData<List<MenuServiceItem>>()
    val availableProducts = MutableLiveData<List<MenuProductItem>>()
    val availableOtherServices = MutableLiveData<List<MenuExtrasItem>>()

    val jobOrderProducts: MutableList<MenuProductItem> = mutableListOf()
    val jobOrderOtherServices: MutableList<MenuExtrasItem> = mutableListOf()

    val subtotal = MutableLiveData(0f)

    init {
        viewModelScope.launch {
            currentCustomer.value = CustomerMinimal("current-customer", "Chris Pratt", "009092", "baduy")
        }
    }

    fun loadServices() {
        viewModelScope.launch {
            val services = listOf(
                MenuServiceItem("rw-hw", "Hot Wash", 46, 90f, MachineType.REGULAR_WASHER, WashType.HOT),
                MenuServiceItem("rw-ww", "Warm Wash", 36, 70f, MachineType.REGULAR_WASHER, WashType.WARM),
                MenuServiceItem("rw-dw", "Delicate Wash", 21, 40f, MachineType.REGULAR_WASHER, WashType.DELICATE),
                MenuServiceItem("rw-sw", "Super Wash", 47, 100f, MachineType.REGULAR_WASHER, WashType.SUPER_WASH),
                MenuServiceItem("rd-rd", "Regular Dry", 40, 70f, MachineType.REGULAR_DRYER, null),
                MenuServiceItem("rd-ad", "Additional Dry", 10, 20f, MachineType.REGULAR_DRYER, null),
                MenuServiceItem("tw-hw", "Hot Wash", 46, 100f, MachineType.TITAN_WASHER, WashType.HOT),
                MenuServiceItem("tw-ww", "Warm Wash", 36, 90f, MachineType.TITAN_WASHER, WashType.WARM),
                MenuServiceItem("tw-dw", "Delicate Wash", 21, 60f, MachineType.TITAN_WASHER, WashType.DELICATE),
                MenuServiceItem("tw-sw", "Super Wash", 47, 120f, MachineType.TITAN_WASHER, WashType.SUPER_WASH),
                MenuServiceItem("td-rd", "Regular Dry", 40, 90f, MachineType.TITAN_DRYER, null),
                MenuServiceItem("td-ad", "Additional Dry", 10, 25f, MachineType.TITAN_DRYER, null),
            ).onEach { menuItem ->
//                jobOrderServices.find { s -> s.id == menuItem.id }?.let { joItem ->
//                    menuItem.selected = joItem.selected
//                    menuItem.quantity = joItem.quantity
//                    println("Iterate: " + joItem.id)
//                }
            }
            available8kWashServices.value = services.filter {
                it.machineType == MachineType.REGULAR_WASHER
            }
            available8kDryServices.value = services.filter {
                it.machineType == MachineType.REGULAR_DRYER
            }
            available12kWashServices.value = services.filter {
                it.machineType == MachineType.TITAN_WASHER
            }
            available12kDryServices.value = services.filter {
                it.machineType == MachineType.TITAN_DRYER
            }
            println("Services Loaded")
        }
    }

    fun loadOtherServices() {
        val otherServices = listOf(
            MenuExtrasItem("8kg-os-fold", "8KG Fold", 30f, "fold"),
            MenuExtrasItem("12kg-os-fold", "12KG Fold", 40f, "fold"),
        )
        availableOtherServices.value = otherServices
        otherServices.onEach { menuItem ->
            jobOrderOtherServices.find { s -> s.id == menuItem.id }?.let { joItem ->
                menuItem.selected = joItem.selected
                menuItem.quantity = joItem.quantity
            }
        }
    }

    fun loadProducts() {
        viewModelScope.launch {
            availableProducts.value = listOf(
                MenuProductItem("ariel", "Ariel", 15.0f, MeasureUnit.SACHET, 1f, ProductType.DETERGENT),
                MenuProductItem("breeze", "Breeze", 15.0f, MeasureUnit.SACHET, 1f, ProductType.DETERGENT),
                MenuProductItem("downy", "Downy", 15.0f, MeasureUnit.MILLILITER,  80f, ProductType.FAB_CON),
                MenuProductItem("del", "Del", 15.0f, MeasureUnit.MILLILITER,  80f, ProductType.FAB_CON),
                MenuProductItem("plastic-s", "Plastic (S)", 3.0f, MeasureUnit.PCS,  1f, ProductType.OTHER),
                MenuProductItem("plastic-m", "Plastic (M)", 5.0f, MeasureUnit.PCS,  1f, ProductType.OTHER),
                MenuProductItem("plastic-l", "Plastic (L)", 7.0f, MeasureUnit.PCS,  1f, ProductType.OTHER),
                MenuProductItem("hanger", "Hanger", 25.0f, MeasureUnit.PCS, 1f, ProductType.OTHER),
            ).onEach { menuItem ->
                jobOrderProducts.find { p -> p.id == menuItem.id }?.let { joItem ->
                    menuItem.selected = joItem.selected
                    menuItem.quantity = joItem.quantity
                }
            }
            println("Products loaded")
        }
    }

    fun syncServices(services: List<MenuServiceItem>?) {
        jobOrderServices.value = services?.toMutableList()
    }

    fun openServices(itemPreset: MenuServiceItem?) {
        jobOrderServices.value.let {
            _dataState.value = DataState.OpenServices(it, itemPreset)
        }
    }

    fun navigate(layout: Int) {
        activeNavigation.value = layout
    }

    fun putService(service: MenuServiceItem) {
//        jobOrderServices.apply {
//            find { s -> s.id == service.id }.let {
//                if(it == null) {
//                    add(service)
//                } else {
//                    this[indexOf(it)] = service
//                }
//            }
//        }
//        _dataState.value = DataState.PutService(service)
//        computeSubtotal()
    }
    fun putProduct(product: MenuProductItem) {
//        jobOrderProducts.apply {
//            find { s -> s.id == product.id }.let {
//                if(it == null) {
//                    add(product)
//                } else {
//                    this[indexOf(it)] = product
//                }
//            }
//        }
//        _dataState.value = DataState.PutProduct(product)
//        computeSubtotal()
    }

    fun applyServiceQuantity(data: QuantityModel) {
//        jobOrderServices.let {
//            it.find { s -> s.id == data.id }?.apply {
//                quantity = data.quantity
//            }?.let { service ->
//                putService(service)
//            }
//        }
    }
    fun applyProductQuantity(data: QuantityModel) {
        jobOrderProducts.let {
            it.find { s -> s.id == data.id }?.apply {
                quantity = data.quantity
            }?.let { product ->
                putProduct(product)
            }
        }
    }

    fun removeService(serviceId: String) {
//        val index = jobOrderServices.let {
//            it.indexOf(it.find { s -> s.id == serviceId })
//        }
//        jobOrderServices.removeAt(index)
//        _dataState.value = DataState.RemoveService(index, serviceId)
//        computeSubtotal()
    }
    fun removeProduct(productId: String) {
//        val index = jobOrderProducts.let {
//            it.indexOf(it.find { s -> s.id == productId })
//        }
//        jobOrderProducts.removeAt(index)
//        _dataState.value = DataState.RemoveProduct(index, productId)
//        computeSubtotal()
    }

    fun requestModifyServiceQuantity(id: String) {
//        jobOrderServices.find{ s -> s.id == id }?.let {
//            _dataState.value = DataState.RequestEditServiceQuantity(
//                QuantityModel(it.id, it.abbr(), it.quantity, QuantityModel.TYPE_SERVICE)
//            )
//        }
    }
    fun requestModifyProductsQuantity(id: String) {
//        jobOrderProducts.find {p -> p.id == id}?.let {
//            _dataState.value = DataState.RequestEditProductQuantity(
//                QuantityModel(it.id, it.name, it.quantity, QuantityModel.TYPE_PRODUCT)
//            )
//        }
    }

    private fun computeSubtotal() {
//        val serviceSubtotal = jobOrderServices.let {
//            var result = 0f
//            if(it.size > 0) {
//                result = it.map { s -> s.price * s.quantity } .reduce { sum, element ->
//                    sum + element
//                }
//            }
//            result
//        }
//        val productSubtotal = jobOrderProducts.let {
//            var result = 0f
//            if(it.size > 0) {
//                result = it.map { s -> s.price * s.quantity } .reduce { sum, element ->
//                    sum + element
//                }
//            }
//            result
//        }
//        subtotal.value = productSubtotal + serviceSubtotal + (deliveryCharge.value?.price() ?: 0f)
//        hasServices.value = jobOrderServices.size > 0
//        hasProducts.value = jobOrderProducts.size > 0
    }

    fun selectDeliveryProfile(dCharge: DeliveryCharge?) {
        deliveryCharge.value = dCharge
        computeSubtotal()
    }

    fun setCustomer(customer: CustomerMinimal?) {
        currentCustomer.value = customer!!
    }
}