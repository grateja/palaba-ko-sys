package com.csi.palabakosys.viewmodels

import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.model.Rule
import com.csi.palabakosys.room.entities.EntityProduct
import com.csi.palabakosys.room.repository.ProductRepository
import com.csi.palabakosys.util.DataState
import com.csi.palabakosys.util.InputValidation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditProductViewModel
@Inject
constructor(
    private val repository: ProductRepository
) : CreateViewModel<EntityProduct>(repository)
{
    fun get(id: String?) {
        super.get(id, EntityProduct())
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
                repository.save(it)?.let { product ->
                    model.value = product
                    dataState.value = DataState.Success(product)
                }
            }
        }
    }
}