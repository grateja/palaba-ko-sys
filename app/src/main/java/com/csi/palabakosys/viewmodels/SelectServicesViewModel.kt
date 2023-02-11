package com.csi.palabakosys.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
//import com.csi.palabakosys.room.repository.DryServiceRepository
import com.csi.palabakosys.room.repository.OtherServiceRepository
import com.csi.palabakosys.room.repository.WashServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectServicesViewModel
@Inject
constructor(
    private val washServiceRepository: WashServiceRepository,
//    private val dryServiceRepository: DryServiceRepository,
    private val otherServiceRepository: OtherServiceRepository
) : ViewModel() {
//    val washAdapter = Adapter<EntityServiceWash>(R.layout.recycler_item_wash_service)
//    val dryAdapter = Adapter<EntityServiceDry>(R.layout.recycler_item_dry_service)
//    val otherAdapter = Adapter<EntityServiceOther>(R.layout.recycler_item_other_service)

//    var onServiceItemSelected: ((EntityJobOrderService) -> Unit) ? = null

//    init {
//        washAdapter.onItemClick = {
//            onServiceItemSelected?.invoke(EntityJobOrderService(null, ServiceType.WASH, it.id).apply {
//                serviceName = it.name
//                price = it.price
//            })
//        }
//        dryAdapter.onItemClick = {
//            onServiceItemSelected?.invoke(EntityJobOrderService(null, ServiceType.DRY, it.id).apply {
//                serviceName = it.name
//                price = it.price
//            })
//        }
//        otherAdapter.onItemClick = {
//            onServiceItemSelected?.invoke(EntityJobOrderService(null, ServiceType.OTHER, it.id).apply {
//                serviceName = it.name
//                price = it.price
//            })
//        }
//    }

    fun getAll() {
        viewModelScope.launch {
            washServiceRepository.getAll().let {
//                washAdapter.setData(it.map { _item -> RecyclerItem(_item) })
            }
//            dryServiceRepository.getAll().let {
//                dryAdapter.setData(it.map { _item -> RecyclerItem(_item) })
//            }
            otherServiceRepository.getAll().let {
//                otherAdapter.setData(it.map { _item -> RecyclerItem(_item) })
            }
        }
    }
}