package com.csi.palabakosys.viewmodels

import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.model.Rule
import com.csi.palabakosys.room.entities.EntityExpense
import com.csi.palabakosys.room.repository.ExpensesRepository
import com.csi.palabakosys.util.DataState
import com.csi.palabakosys.util.InputValidation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditExpenseViewModel
@Inject
constructor(
    private val repository: ExpensesRepository
) : CreateViewModel<EntityExpense>(repository)
{
    fun get(id: UUID?) {
        model.value.let {
            if(it != null) return
            viewModelScope.launch {
                super.get(id, EntityExpense())
            }
        }
    }

    override fun save() {
        model.value?.let {
            val inputValidation = InputValidation()
            inputValidation.addRules("remarks", it.remarks.toString(), arrayOf(Rule.Required))
            if(inputValidation.isInvalid()) {
                validation.value = inputValidation
                return@let
            }

            viewModelScope.launch {
                repository.save(it)?.let { customer ->
                    model.value = customer
                    dataState.value = DataState.Save(customer)
                }
            }
        }
    }
}