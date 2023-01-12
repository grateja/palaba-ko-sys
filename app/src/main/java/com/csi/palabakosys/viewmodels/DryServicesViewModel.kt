package com.csi.palabakosys.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.R
import com.csi.palabakosys.room.repository.DryServiceRepository
import com.csi.palabakosys.adapters.Adapter
//import com.csi.palabakosys.adapters.RecyclerItem
import com.csi.palabakosys.room.entities.EntityServiceDry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DryServicesViewModel
@Inject
constructor(
    private val repository: DryServiceRepository
) : ViewModel() {

//    val adapter = Adapter<EntityServiceDry>(R.layout.recycler_item_dry_service)
    val service = MutableLiveData<EntityServiceDry>()
    val isEmpty = MutableLiveData(false);

    init {
//        adapter.onItemClick = {
//            service.value = it.getItem()
//        }
    }

    fun getAll() {
        viewModelScope.launch {
            val services = repository.getAll()
            isEmpty.value = services.isEmpty()
//            adapter.setData(services.map {
//                RecyclerItem(it)
//            })
        }
    }
}