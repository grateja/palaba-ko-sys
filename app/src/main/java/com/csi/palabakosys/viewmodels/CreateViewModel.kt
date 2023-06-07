package com.csi.palabakosys.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.model.Rule
import com.csi.palabakosys.room.entities.BaseEntity
import com.csi.palabakosys.room.repository.IRepository
import com.csi.palabakosys.util.*
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.util.UUID

open class CreateViewModel<T : BaseEntity> (private val iRepository: IRepository<T>) : ViewModel() {
    val validation = MutableLiveData(InputValidation())
    val crudActionEnum = MutableLiveData(CRUDActionEnum.CREATE)
    val dataState = MutableLiveData<DataState<T>>()
    val model = MutableLiveData<T?>()
    var promptPass = true

    fun getId() : String? {
        return model.value?.id?.toString() // this.modelId?.toString()
    }

    fun clearError(key: String = "") {
        validation.value = validation.value?.removeError(key)
    }
    
    fun resetState() {
        dataState.value = DataState.StateLess
    }

    fun requestExit() {
        dataState.value = DataState.RequestExit(promptPass)
    }

    open fun save() {
        model.value?.let {
            viewModelScope.launch {
                if(validation.value?.isInvalid() == true) {
                    return@launch
                }
                iRepository.save(it).let {
                    model.value = it
                }
                dataState.value = DataState.Save(it)
            }
        }
    }

    protected suspend fun get(id: UUID?, initialModel: T) : T {
        model.value.let { it ->
            if(it != null) return it
            if(id == null) {
                model.value = initialModel
                return initialModel
            }

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
        }
    }
}