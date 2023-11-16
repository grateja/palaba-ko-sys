package com.csi.palabakosys.app.customers.create

import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.model.Rule
import com.csi.palabakosys.room.entities.EntityCustomer
import com.csi.palabakosys.room.repository.CustomerRepository
import com.csi.palabakosys.util.InputValidation
import com.csi.palabakosys.viewmodels.CreateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditCustomerViewModel
@Inject
constructor(
    private val repository: CustomerRepository
) : CreateViewModel<EntityCustomer>(repository)
{
    private var originalName: String? = null
    private var originalCRN: String? = null
    fun get(id: UUID?, presetName: String?) {
        model.value.let {
            if(it != null) return
            viewModelScope.launch {
                val crn = repository.getNextJONumber()
                super.get(id, EntityCustomer(crn, presetName)).let { customer ->
                    originalName = customer.name
                    originalCRN = customer.crn
                }
            }
        }
    }

    fun validate() {
        viewModelScope.launch {
            val customer = model.value
            val inputValidation = InputValidation().apply {
                addRule("name", customer?.name, arrayOf(Rule.Required))
                addRule("crn", customer?.crn, arrayOf(Rule.Required))

                if(originalName != customer?.name &&  repository.checkName(customer?.name)) {
                    addError("name", "Name already taken. Please specify more details")
                }

                if(originalCRN != customer?.crn) {
                    repository.getCustomerMinimalByCRN(customer?.crn)?.let { customer ->
                        addError("crn", "CRN Conflict with ${customer.name} : ${customer.crn}")
                    }
                }
            }
            super.isInvalid(inputValidation)
        }
    }

    fun presetCustomerName(name: String?) {

    }

//    override fun save() {
//        model.value?.let {
//            val inputValidation = InputValidation()
//            inputValidation.addRule("name", it.name.toString(), arrayOf(Rule.Required))
//
//            viewModelScope.launch {
//                if(originalName != it.name &&  repository.checkName(it.name)) {
//                    inputValidation.addError("name", "Name already taken. Please specify more details")
//                }
//                if(originalCRN != it.crn) {
//                    repository.getCustomerMinimalByCRN(it.crn)?.let { customer ->
//                        inputValidation.addError("crn", "CRN Conflict with ${customer.name} : ${customer.crn}")
//                    }
//                }
//                validation.value = inputValidation
//                super.save()
////                if(inputValidation.isInvalid()) {
////                    validation.value = inputValidation
////                    return@launch
////                }
////                repository.save(it)?.let { customer ->
////                    model.value = customer
////                    dataState.value = DataState.Save(customer)
////                }
//            }
//        }
//    }
}