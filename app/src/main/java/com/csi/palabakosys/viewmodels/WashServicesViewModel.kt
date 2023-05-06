package com.csi.palabakosys.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.room.repository.WashServiceRepository
import com.csi.palabakosys.room.entities.EntityService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WashServicesViewModel
@Inject
constructor(
    private val repository: WashServiceRepository
) : ViewModel() {

//    val adapter = Adapter<EntityServiceWash>(R.layout.recycler_item_wash_service)
    val service = MutableLiveData<EntityService>()
    val isEmpty = MutableLiveData(false);

    init {
//        adapter.onItemClick = {
//            service.value = it.getItem()
//        }
    }

    fun getAll() {
        viewModelScope.launch {
//            val services = repository.getAll()
//            adapter.setData(services.map { _item -> RecyclerItem(_item) })
//            isEmpty.value = services.isEmpty()
        }
    }
}