//package com.csi.palabakosys.viewmodels
//
//import androidx.lifecycle.viewModelScope
//import com.csi.palabakosys.model.Rule
//import com.csi.palabakosys.room.entities.EntityCashlessProvider
//import com.csi.palabakosys.room.repository.CashlessProvidersRepository
//import com.csi.palabakosys.util.DataState
//import com.csi.palabakosys.util.InputValidation
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import java.util.UUID
//import javax.inject.Inject
//
//@HiltViewModel
//class AddEditCashlessProviderViewModel
//@Inject
//constructor(
//    private val repository: CashlessProvidersRepository
//) : CreateViewModel<EntityCashlessProvider>(repository)
//{
//    fun get(id: UUID?) {
//        model.value.let {
//            if(it != null) return
//            viewModelScope.launch {
//                super.get(id, EntityCashlessProvider())
//            }
//        }
//    }
//
//    override fun save() {
//        model.value?.let {
//            val inputValidation = InputValidation()
//            inputValidation.addRules("name", it.name.toString(), arrayOf(Rule.Required))
//            if(inputValidation.isInvalid()) {
//                validation.value = inputValidation
//                return@let
//            }
//
//            super.save()
//
////            viewModelScope.launch {
////                repository.save(it)?.let { customer ->
////                    model.value = customer
////                    dataState.value = DataState.Save(customer)
////                }
////            }
//        }
//    }
//}