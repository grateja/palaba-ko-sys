package com.csi.palabakosys.app.packages.preview

import androidx.lifecycle.*
import com.csi.palabakosys.app.joborders.create.extras.MenuExtrasItem
import com.csi.palabakosys.app.joborders.create.products.MenuProductItem
import com.csi.palabakosys.app.joborders.create.services.MenuServiceItem
import com.csi.palabakosys.app.packages.PackageItem
import com.csi.palabakosys.model.EnumProductType
import com.csi.palabakosys.model.Rule
import com.csi.palabakosys.room.entities.*
import com.csi.palabakosys.room.repository.JobOrderPackageRepository
import com.csi.palabakosys.util.DataState
import com.csi.palabakosys.util.InputValidation
import com.csi.palabakosys.viewmodels.CreateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PackagesPreviewViewModel

@Inject
constructor(
    private val packageRepository: JobOrderPackageRepository
) : ViewModel() {
    private val _navigationState = MutableLiveData<NavigationState>()
    val navigationState: LiveData<NavigationState> = _navigationState
    private val entityId = MutableLiveData<UUID?>()
    val readOnlyModel = entityId.switchMap { packageRepository.getPackageWithItemsAsLiveData(it) }

//    val services = MutableLiveData<List<EntityPackageServiceWithService>?>()
//    val extras = MutableLiveData<List<EntityPackageExtrasWithExtras>?>()
//    val products = MutableLiveData<List<EntityPackageProductWithProduct>?>()

//    val services: LiveData<List<EntityPackageServiceWithService>> = entityId.switchMap {
//        packageRepository.getPackageServicesAsLiveData(it)
//    }
//
//    val products: LiveData<List<EntityPackageProductWithProduct>> = entityId.switchMap {
//        packageRepository.getPackageProductsAsLiveData(it)
//    }
//
//    val extras: LiveData<List<EntityPackageExtrasWithExtras>> = entityId.switchMap {
//        packageRepository.getPackageExtrasAsLiveData(it)
//    }
//
//    val items = MediatorLiveData<List<PackageItem>>().apply {
//        fun update() {
//            val services = (services.value ?: listOf()).map {
//                PackageItem(it.service.id, it.service.name.toString(), it.service.price, it.serviceCrossRef.quantity, null)
//            }
//            val products = (products.value ?: listOf()).map {
//                PackageItem(it.product.id, it.product.name.toString(), it.product.price, it.productCrossRef.quantity, null)
//            }
//            val extras = (extras.value ?: listOf()).map {
//                PackageItem(it.extras.id, it.extras.name.toString(), it.extras.price, it.extrasCrossRef.quantity, null)
//            }
//            value = services + products + extras
//        }
//        addSource(services) {update()}
//        addSource(products) {update()}
//        addSource(extras) {update()}
//    }

    fun get(id: UUID?) {
        entityId.value = id
//        viewModelScope.launch {
//            packageRepository.getById(id).let {
//                model.value = it?.prePackage ?: EntityPackage()
//                if(it != null) {
//                    services.value = it.services
//                    extras.value = it.extras
//                    products.value = it.products
//                }
//            }
//        }
    }

    fun syncServices(list: List<MenuServiceItem>?) {
        val packageId = entityId.value ?: return
        list?.map {
            EntityPackageService(
                packageId,
                it.serviceRefId,
                it.quantity,
                it.joServiceItemId ?: UUID.randomUUID(),
                it.deletedAt
            )
        }?.let {
            viewModelScope.launch {
                packageRepository.syncServices(it)
            }
        }
    }

    fun syncProducts(list: List<MenuProductItem>?) {
        val packageId = entityId.value ?: return
        list?.map {
            EntityPackageProduct(
                packageId,
                it.productRefId,
                it.quantity,
                it.joProductItemId ?: UUID.randomUUID(),
                it.deletedAt
            )
        }?.let {
            viewModelScope.launch {
                packageRepository.syncProducts(it)
            }
        }
    }

    fun syncExtras(list: List<MenuExtrasItem>?) {
        val packageId = entityId.value ?: return
        list?.map {
            EntityPackageExtras(
                packageId,
                it.extrasRefId,
                it.quantity,
                it.joExtrasItemId ?: UUID.randomUUID(),
                it.deletedAt
            )
        }?.let {
            viewModelScope.launch {
                packageRepository.syncExtras(it)
            }
        }
    }

    fun openWashDry() {
        val services = readOnlyModel.value?.services?.map {
            MenuServiceItem(
                it.serviceCrossRef.id,
                it.service.id,
                it.service.name.toString(),
                it.service.serviceRef.minutes,
                it.service.price,
                it.service.serviceRef.machineType,
                it.service.serviceRef.washType,
                it.serviceCrossRef.quantity,
                0,
                it.serviceCrossRef.deletedAt
            )
        }
        _navigationState.value = NavigationState.OpenWashDry(services)
    }

    fun openProducts() {
        val products = readOnlyModel.value?.products?.map {
            MenuProductItem(
                it.productCrossRef.id,
                it.product.id,
                it.product.name.toString(),
                it.product.price,
                it.product.measureUnit,
                it.product.unitPerServe,
                it.productCrossRef.quantity,
                it.product.currentStock,
                it.product.productType ?: EnumProductType.OTHER,
                it.productCrossRef.deletedAt
            )
        }
        _navigationState.value = NavigationState.OpenProducts(products)
    }

    fun openExtras() {
        val extras = readOnlyModel.value?.extras?.map {
            MenuExtrasItem(
                it.extrasCrossRef.id,
                it.extras.id,
                it.extras.name.toString(),
                it.extras.price,
                it.extras.category,
                it.extrasCrossRef.quantity,
                it.extrasCrossRef.deletedAt
            )
        }
        _navigationState.value = NavigationState.OpenExtras(extras)
    }

    fun delete() {
        viewModelScope.launch {
            readOnlyModel.value?.let {
                packageRepository.delete(it.prePackage, true)
            }
        }
    }

    fun openAddEdit() {
        _navigationState.value = NavigationState.OpenAddEdit(entityId.value)
    }

    fun resetState() {
        _navigationState.value = NavigationState.StateLess
    }

    sealed class NavigationState {
        object StateLess: NavigationState()
        class OpenAddEdit(val packageId: UUID?) : NavigationState()
        class OpenWashDry(val items: List<MenuServiceItem>?) : NavigationState()
        class OpenProducts(val items: List<MenuProductItem>?) : NavigationState()
        class OpenExtras(val items: List<MenuExtrasItem>?) : NavigationState()
    }
}