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
        data class OpenProducts(val list: List<MenuProductItem>?, val item: MenuProductItem?): DataState()
        data class OpenDelivery(val deliveryCharge: DeliveryCharge?): DataState()
    }
    private val _dataState = MutableLiveData<DataState>()
    fun dataState(): MutableLiveData<DataState> {
        return _dataState
    }
    fun resetState() {
        _dataState.value = DataState.StateLess
    }

    val jobOrderNumber = MutableLiveData("#0909776")
    val currentCustomer = MutableLiveData<CustomerMinimal>()
    val deliveryCharge = MutableLiveData<DeliveryCharge?>()
    val jobOrderServices = MutableLiveData<MutableList<MenuServiceItem>>()
    val jobOrderProducts = MutableLiveData<MutableList<MenuProductItem>>()
    val jobOrderExtras = MutableLiveData<MutableList<MenuExtrasItem>>()
//    val discount = MutableLiveData<MenuItemDiscount>()

    val hasServices = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = jobOrderServices.value?.size!! > 0
        }
        addSource(jobOrderServices) { update() }
    }
    val hasProducts = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = jobOrderProducts.value?.size!! > 0
        }
        addSource(jobOrderProducts) { update() }
    }
    val hasExtras = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = jobOrderExtras.value?.size!! > 0
        }
        addSource(jobOrderExtras) { update() }
    }
    val hasDelivery = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = deliveryCharge.value != null
        }
        addSource(deliveryCharge) { update() }
    }
    val hasDiscount = MediatorLiveData<Boolean>().apply {
//        fun update() {
//            value = discount.value != null
//        }
//        addSource(discount) { update() }
    }
    val hasAny = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = hasServices.value!! || hasProducts.value!!
        }
        addSource(hasServices) { update() }
        addSource(hasProducts) { update() }
    }

    val subtotal = MediatorLiveData<Float>().apply {
        fun update() {
            val serviceSubtotal = jobOrderServices.value?.let {
                var result = 0f
                if(it.size > 0) {
                    result = it.map { s -> s.price * s.quantity } .reduce { sum, element ->
                        sum + element
                    }
                }
                result
            } ?: 0f
            val productSubtotal = jobOrderProducts.value?.let {
                var result = 0f
                if(it.size > 0) {
                    result = it.map { s -> s.price * s.quantity } .reduce { sum, element ->
                        sum + element
                    }
                }
                result
            } ?: 0f
            value = productSubtotal + serviceSubtotal + (deliveryCharge.value?.price() ?: 0f)
        }
        addSource(jobOrderServices) {update()}
        addSource(jobOrderProducts) {update()}
    }

    fun syncServices(services: List<MenuServiceItem>?) {
        jobOrderServices.value = services?.toMutableList()
    }

    fun syncProducts(products: List<MenuProductItem>?) {
        jobOrderProducts.value = products?.toMutableList()
    }

    fun openServices(itemPreset: MenuServiceItem?) {
        jobOrderServices.value.let {
            _dataState.value = DataState.OpenServices(it, itemPreset)
        }
    }

    fun openProducts(itemPreset: MenuProductItem?) {
        jobOrderProducts.value.let {
            _dataState.value = DataState.OpenProducts(it, itemPreset)
        }
    }

    fun openDelivery() {
        deliveryCharge.value.let {
            _dataState.value = DataState.OpenDelivery(it)
        }
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

//    fun selectDeliveryProfile(dCharge: DeliveryCharge?) {
//        deliveryCharge.value = dCharge
//        computeSubtotal()
//    }

    fun setCustomer(customer: CustomerMinimal?) {
        currentCustomer.value = customer!!
    }

    fun setDeliveryCharge(deliveryCharge: DeliveryCharge?) {
        this.deliveryCharge.value = deliveryCharge
    }
}