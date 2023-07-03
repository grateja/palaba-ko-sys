package com.csi.palabakosys.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.model.CRUDActionEnum
import com.csi.palabakosys.room.entities.BaseEntity
import com.csi.palabakosys.room.repository.IRepository
import com.csi.palabakosys.util.*
import kotlinx.coroutines.launch
import java.util.UUID

open class CreateViewModel<T : BaseEntity> (private val iRepository: IRepository<T>) : ViewModel() {
    val validation = MutableLiveData(InputValidation())
    val crudActionEnum = MutableLiveData(CRUDActionEnum.CREATE)
    val dataState = MutableLiveData<DataState<T>>()
    val model = MutableLiveData<T?>()

    protected val entityId = MutableLiveData<UUID?>()

    var promptPass = false

    fun getId() : String? {
        return entityId.value?.toString() // this.modelId?.toString()
    }

    fun clearError(key: String = "") {
        validation.value = validation.value?.removeError(key)
    }
    
    open fun resetState() {
        dataState.value = DataState.StateLess
    }

    fun requestExit() {
        dataState.value = DataState.RequestExit(promptPass)
    }

    protected fun validate(inputValidation: InputValidation) : Boolean {
        val isInvalid = inputValidation.isInvalid()

        dataState.value = if (isInvalid)
            DataState.InvalidInput(inputValidation)
        else
            DataState.ValidationPassed

        validation.value = inputValidation

        return isInvalid
    }

    open fun save() {
        model.value?.let {
            viewModelScope.launch {
                iRepository.save(it).let {
                    model.value = it
                }
                dataState.value = DataState.SaveSuccess(it)
            }
        }
    }

    open fun confirmDelete(deletedBy: UUID?, permanent: Boolean = false) {
        viewModelScope.launch {
            model.value?.let {
                it.deletedBy = deletedBy
                if(iRepository.delete(it, permanent)) {
                    dataState.value = DataState.DeleteSuccess(it)
                }
            }
        }
    }

    protected suspend fun get(id: UUID?, initialModel: T) : T {
        entityId.value = id

        model.value.let {
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