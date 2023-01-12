package com.csi.palabakosys.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.room.entities.EntityJobOrderWithItems
import com.csi.palabakosys.room.repository.JobOrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DialogJobOrderViewModel
@Inject constructor(
    private val repository: JobOrderRepository
) : ViewModel() {
    val model = MutableLiveData<EntityJobOrderWithItems>()
    fun getId() : UUID? {
        return model.value?.jobOrder?.id
    }
    fun get(id: String?) {
        viewModelScope.launch {
            repository.get(id).let {
                model.value = it
            }
        }
    }
}