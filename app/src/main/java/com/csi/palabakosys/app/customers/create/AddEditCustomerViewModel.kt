package com.csi.palabakosys.app.customers.create

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.model.Rule
import com.csi.palabakosys.room.entities.EntityCustomer
import com.csi.palabakosys.room.repository.CustomerRepository
import com.csi.palabakosys.util.DataState
import com.csi.palabakosys.util.InputValidation
import com.csi.palabakosys.viewmodels.CreateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditCustomerViewModel
@Inject
constructor(
    private val repository: CustomerRepository
) : CreateViewModel<EntityCustomer>(repository)
{
    fun get(id: String?) {
        model.value.let {
            if(it != null) return
            viewModelScope.launch {
                val crn = repository.getNextJONumber()
                super.get(id, EntityCustomer(crn))
            }
        }
    }

    fun save() {
        model.value?.let {
            val inputValidation = InputValidation()
            inputValidation.addRules("name", it.name.toString(), arrayOf(Rule.REQUIRED))
            if(inputValidation.isInvalid()) {
                validation.value = inputValidation
                return@let
            }

            viewModelScope.launch {
                repository.save(it)?.let { customer ->
                    model.value = customer
                    dataState.value = DataState.Success(customer)
                }
            }
        }
    }
}