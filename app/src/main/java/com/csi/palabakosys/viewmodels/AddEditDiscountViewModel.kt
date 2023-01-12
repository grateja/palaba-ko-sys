package com.csi.palabakosys.viewmodels

import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.model.Rule
import com.csi.palabakosys.room.entities.EntityDiscount
import com.csi.palabakosys.room.repository.DiscountsRepository
import com.csi.palabakosys.util.DataState
import com.csi.palabakosys.util.InputValidation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditDiscountViewModel
@Inject
constructor(
    private val repository: DiscountsRepository
) : CreateViewModel<EntityDiscount>(repository)
{
    fun get(id: String?) {
        model.value.let {
            if(it != null) return
            viewModelScope.launch {
                super.get(id, EntityDiscount())
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