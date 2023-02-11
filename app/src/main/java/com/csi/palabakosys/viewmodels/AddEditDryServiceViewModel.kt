//package com.csi.palabakosys.viewmodels
//
//import androidx.lifecycle.viewModelScope
//import com.csi.palabakosys.model.MachineType
//import com.csi.palabakosys.model.Rule
//import com.csi.palabakosys.room.entities.EntityServiceDry
//import com.csi.palabakosys.util.DataState
//import com.csi.palabakosys.util.InputValidation
//import com.csi.palabakosys.room.repository.DryServiceRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class AddEditDryServiceViewModel
//@Inject
//constructor(
//    private val repository: DryServiceRepository
//) : CreateViewModel<EntityServiceDry>(repository)
//{
//
//    fun get(id: String?) {
//        super.get(id, EntityServiceDry())
//    }
//
//    fun setType(type: MachineType) {
//        model.value?.machineType = type
//        println(model.value?.machineType)
//    }
//
//    fun save() {
//        model.value?.let {
//            val inputValidation = InputValidation()
//            inputValidation.addRules("name", it.name.toString(), arrayOf(Rule.REQUIRED))
//            if(inputValidation.isInvalid()) {
//                validation.value = inputValidation
//                return@let
//            }
//
//            viewModelScope.launch {
//                repository.save(it)?.let { service ->
//                    model.value = service
//                    dataState.value = DataState.Success(service)
//                }
//            }
//        }
//    }
//}