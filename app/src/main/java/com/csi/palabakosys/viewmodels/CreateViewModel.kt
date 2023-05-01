package com.csi.palabakosys.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.room.entities.BaseEntity
import com.csi.palabakosys.room.repository.IRepository
import com.csi.palabakosys.util.CRUDActionEnum
import com.csi.palabakosys.util.DataState
import com.csi.palabakosys.util.InputValidation
import com.google.gson.Gson
import kotlinx.coroutines.launch

open class CreateViewModel<T : BaseEntity> (private val iRepository: IRepository<T>) : ViewModel() {
    val validation = MutableLiveData(InputValidation())
    val crudActionEnum = MutableLiveData(CRUDActionEnum.CREATE)
    val dataState = MutableLiveData<DataState<T>>()
    val model = MutableLiveData<T?>()

    fun getId() : String? {
        return model.value?.id?.toString() // this.modelId?.toString()
    }

    fun clearError(key: String = "") {
        validation.value = validation.value?.removeError(key)
    }
    
    fun resetState() {
        dataState.value = DataState.StateLess
    }

    protected suspend fun get(id: String?, initialModel: T) : T {
        model.value.let { it ->
            if(it != null) return it
            if(id == null) {
                return initialModel
            }
//            viewModelScope.launch {
                iRepository.get(id).let { t->
                    if(t == null) {
                        crudActionEnum.value = CRUDActionEnum.CREATE
                        model.value = initialModel
                    } else {
                        crudActionEnum.value = CRUDActionEnum.UPDATE
                        model.value = t
                    }

                    return t?:initialModel
                }
//            }
        }
    }
}