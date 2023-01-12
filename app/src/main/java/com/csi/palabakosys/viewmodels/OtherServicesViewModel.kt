package com.csi.palabakosys.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.R
import com.csi.palabakosys.room.repository.OtherServiceRepository
import com.csi.palabakosys.adapters.Adapter
//import com.csi.palabakosys.adapters.RecyclerItem
import com.csi.palabakosys.room.entities.EntityServiceOther
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtherServicesViewModel
@Inject
constructor(
    private val repository: OtherServiceRepository
) : ViewModel() {

//    val adapter = Adapter<EntityServiceOther>(R.layout.recycler_item_other_service)
    val service = MutableLiveData<EntityServiceOther>()
    val isEmpty = MutableLiveData(false);

    init {
//        adapter.onItemClick = {
//            service.value = it.getItem()
//        }
    }

    fun getAll() {
        viewModelScope.launch {
            val services = repository.getAll()
//            adapter.setData(services.map { _item -> RecyclerItem(_item) })
            isEmpty.value = services.isEmpty()
        }
    }
}