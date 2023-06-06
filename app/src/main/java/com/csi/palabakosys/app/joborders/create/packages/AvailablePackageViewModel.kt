package com.csi.palabakosys.app.joborders.create.packages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.app.joborders.create.delivery.DeliveryCharge
import com.csi.palabakosys.app.joborders.create.discount.MenuDiscount
import com.csi.palabakosys.app.joborders.create.extras.MenuExtrasItem
import com.csi.palabakosys.app.joborders.create.products.MenuProductItem
import com.csi.palabakosys.app.joborders.create.services.MenuServiceItem
import com.csi.palabakosys.app.joborders.create.shared_ui.QuantityModel
import com.csi.palabakosys.room.entities.EntityPackageServiceWithService
import com.csi.palabakosys.room.repository.JobOrderPackageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AvailablePackageViewModel
@Inject
constructor(
    private val repository: JobOrderPackageRepository
): ViewModel() {
    private val _availablePackages = MutableLiveData<List<MenuJobOrderPackage>>()
    val availablePackages: LiveData<List<MenuJobOrderPackage>> = _availablePackages

    private val _dataState = MutableLiveData<DataState>()
    val dataState: LiveData<DataState> = _dataState

    init {
        loadServices()
    }

    private fun loadServices() {
        viewModelScope.launch {
            _availablePackages.value = repository.getAll("")
        }
    }

//    fun setPreselectedPackages(packages: List<MenuJobOrderPackage>?) {
//        packages?.forEach { mjp ->
//            availablePackages.value?.find {mjp.packageRefId == it.packageRefId}?.apply {
//                this.selected = true
//                this.quantity = mjp.quantity
//                this.deletedAt = mjp.deletedAt
//            }
//        }
//    }

    fun putPackage(quantityModel: QuantityModel) {
        val packageItem = availablePackages.value?.find {it.packageRefId == quantityModel.id}?.apply {
            this.selected = true
            this.quantity = quantityModel.quantity
            this.deletedAt = null
        }
        _dataState.value = DataState.UpdatePackage(packageItem!!)
    }

    fun removePackage(quantityModel: QuantityModel) {
        availablePackages.value?.find{it.packageRefId == quantityModel.id}?.apply {
            this.selected = false
            this.deletedAt = Instant.now()
            _dataState.value = DataState.UpdatePackage(this)
        }
    }

    private fun menuServiceItem(service: EntityPackageServiceWithService) : MenuServiceItem {
        return MenuServiceItem(
            UUID.randomUUID(),
            service.service.id,
            service.service.name!!,
            service.service.serviceRef.minutes,
            service.service.price,
            service.service.serviceRef.machineType,
            service.service.serviceRef.washType,
            service.serviceCrossRef.quantity,
            0
        )
    }

    fun prepareSubmit(packageId: UUID) {
        viewModelScope.launch {
            repository.getById(packageId)?.let { _package ->
                val services = mutableListOf<MenuServiceItem>()
                val products = mutableListOf<MenuProductItem>()
                val extras = mutableListOf<MenuExtrasItem>()
                var discount: MenuDiscount? = null
                var deliveryCharge: DeliveryCharge? = null

                _package.services?.forEach { _packageService ->
                    val exists = services.find { it.serviceRefId == _packageService.service.id }
                    if(exists != null) {
                        exists.quantity += _packageService.serviceCrossRef.quantity
                        services.add(exists)
                    } else {
                        services.add(menuServiceItem(_packageService))
                    }
                }

                _dataState.value = DataState.Submit(
                    services,
                    products,
                    extras,
                    discount,
                    deliveryCharge
                )
            }
        }
    }

    fun prepareSubmit() {
        viewModelScope.launch {
            val list = availablePackages.value?.filter {it.selected && it.deletedAt == null}
            val services = mutableListOf<MenuServiceItem>()
            val products = mutableListOf<MenuProductItem>()
            val extras = mutableListOf<MenuExtrasItem>()
            var discount: MenuDiscount? = null
            var deliveryCharge: DeliveryCharge? = null

            val ids = list?.map { it.packageRefId }
            repository.getByIds(ids).forEach { _entityPackage ->
                list?.find {it.packageRefId == _entityPackage.prePackage.id}?.let { _menuPackage ->
                    _entityPackage.services?.forEach { _services ->
                        val exists = services.find { it.serviceRefId == _services.serviceCrossRef.serviceId }
                        if(exists != null) {
                            exists.quantity += (_services.serviceCrossRef.quantity * _menuPackage.quantity)
                        } else {
                            services.add(menuServiceItem(_services).apply {
                                this.quantity = _services.serviceCrossRef.quantity * _menuPackage.quantity
                            })
                        }
                    }
                }
            }

//            println("before loop")
//            list?.forEach { _menuPackage ->
//                println("during loop")
//                repository.getById(_menuPackage.packageRefId)?.let { _entityPackage ->
//                    _entityPackage.services?.forEach { _services ->
//                        val exists = services.find { it.serviceRefId == _services.serviceCrossRef.serviceId }
//                        if(exists != null) {
//                            exists.quantity += (_services.serviceCrossRef.quantity * _menuPackage.quantity)
//                        } else {
//                            services.add(menuServiceItem(_services).apply {
//                                this.quantity = _services.serviceCrossRef.quantity * _menuPackage.quantity
//                            })
//                        }
//                    }
//                }
//            }
//            println("after loop")
            _dataState.value = DataState.Submit(
                services,
                products,
                extras,
                discount,
                deliveryCharge
            )

//            list?.let { menuPackages ->
//                val ids = menuPackages.map { it.packageRefId }
//                repository.getByIds(ids).let { packages ->
//                    val services = mutableListOf<MenuServiceItem>()
//                    val products = mutableListOf<MenuProductItem>()
//                    val extras = mutableListOf<MenuExtrasItem>()
//                    var discount: MenuDiscount? = null
//                    var deliveryCharge: DeliveryCharge? = null
//
//                    packages.forEach { _package ->
//                        _package.services?.forEach { _packageService ->
//                            val exists = services.find { it.serviceRefId == _packageService.service.id }
//                            if(exists != null) {
//                                exists.quantity += _packageService.serviceCrossRef.quantity
//                                services.add(exists)
//                            } else {
//                                services.add(menuServiceItem(_packageService))
//                            }
//                        }
//                    }
//
//                    _dataState.value = DataState.Submit(
//                        services,
//                        products,
//                        extras,
//                        discount,
//                        deliveryCharge
//                    )
//                }
//            }
        }
    }

    fun resetState() {
        _dataState.value = DataState.StateLess
    }

    sealed class DataState {
        object StateLess: DataState()
        data class UpdatePackage(val packageItem: MenuJobOrderPackage) : DataState()
        data class Submit(
            val services: List<MenuServiceItem>?,
            val products: List<MenuProductItem>?,
            val extras: List<MenuExtrasItem>?,
            val discount: MenuDiscount?,
            val deliveryCharge: DeliveryCharge?,
        ) : DataState()
    }
}