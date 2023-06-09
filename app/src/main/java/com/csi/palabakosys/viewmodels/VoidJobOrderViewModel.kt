//package com.csi.palabakosys.viewmodels
//
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.csi.palabakosys.model.Rule
//import com.csi.palabakosys.preferences.AppPreferenceRepository
//import com.csi.palabakosys.room.entities.EntityJobOrderVoid
//import com.csi.palabakosys.room.entities.EntityJobOrderWithItems
//import com.csi.palabakosys.room.repository.JobOrderRepository
//import com.csi.palabakosys.util.DataState
//import com.csi.palabakosys.util.InputValidation
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import java.util.*
//import javax.inject.Inject
//
//@HiltViewModel
//class VoidJobOrderViewModel
//@Inject
//constructor(
//    private val appPreferenceRepository: AppPreferenceRepository,
//    private val repository: JobOrderRepository
//) : ViewModel() {
//    val jobOrder = MutableLiveData<EntityJobOrderWithItems>()
//    val validation = MutableLiveData(InputValidation())
//    val dataState = MutableLiveData<DataState<EntityJobOrderVoid?>>()
//    val model = MutableLiveData<EntityJobOrderVoid>()
//
//    fun clearError(key: String = "") {
//        validation.value = validation.value?.removeError(key)
//    }
//    fun get(jobOrderId: String) {
//        viewModelScope.launch {
//            repository.getJobOrderWithItems(UUID.fromString(jobOrderId)).let {
//                if(it != null) {
//                    jobOrder.value = it
////                    model.value = it.jobOrder.entityJobOrderVoid ?: EntityJobOrderVoid(appPreferenceRepository.getUser()?.name)
//                } else {
//                    dataState.value = DataState.Invalidate("Job order not found or deleted!")
//                }
//            }
//        }
//    }
//
//    fun save() {
//        viewModelScope.launch {
//            val inputValidation = InputValidation()
//            inputValidation.addRules("remarks", model.value?.remarks, arrayOf(Rule.Required))
//            if(inputValidation.isInvalid()) {
//                validation.value = inputValidation
//                return@launch
//            }
//
//            jobOrder.value?.let {
//                it.jobOrder.entityJobOrderVoid = model.value
////                repository.void(it.jobOrder)
//                dataState.value = DataState.Save(it.jobOrder.entityJobOrderVoid)
//            }
//        }
//    }
//}