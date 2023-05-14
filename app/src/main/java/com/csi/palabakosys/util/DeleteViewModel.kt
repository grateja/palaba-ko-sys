package com.csi.palabakosys.util

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.room.repository.IRepository
import kotlinx.coroutines.launch
import java.util.UUID

open class DeleteViewModel<T>(private val iRepository: IRepository<T>) : ViewModel() {
    val model = MutableLiveData<T>()
    val dataState = MutableLiveData<DataState<T>>()

    fun get(id: UUID?) {
        model.value.let { it ->
            if(it != null) return
            viewModelScope.launch {
                iRepository.get(id)?.let { t->
                    model.value = t
                    return@launch
                }
                dataState.value = DataState.Invalidate("Item not found or Deleted!")
            }
        }
    }

    fun confirmDelete() {
        viewModelScope.launch {
            try {
                model.value?.let {
                    if(iRepository.delete(it)) {
                        dataState.value = DataState.Save(it)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}