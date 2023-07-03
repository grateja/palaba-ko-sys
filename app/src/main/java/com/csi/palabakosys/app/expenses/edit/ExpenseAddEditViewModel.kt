package com.csi.palabakosys.app.expenses.edit

import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.model.Rule
import com.csi.palabakosys.room.entities.EntityExpense
import com.csi.palabakosys.room.repository.ExpensesRepository
import com.csi.palabakosys.util.InputValidation
import com.csi.palabakosys.viewmodels.CreateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ExpenseAddEditViewModel

@Inject
constructor(
    private val expensesRepository: ExpensesRepository
) : CreateViewModel<EntityExpense>(expensesRepository) {
    fun get(id: UUID?) {
        viewModelScope.launch {
            super.get(id, EntityExpense())
        }
    }

    fun save(userId: UUID?) {

        val model = model.value

        model?.createdBy = userId

        this.validation.value = InputValidation().apply {
            addRule("remarks", model?.remarks, arrayOf(Rule.Required))
            addRule("amount", model?.amount, arrayOf(Rule.Required, Rule.IsNumeric))
//            addRules("tag", model.value?.tag, arrayOf(Rule.Required))
        }

        super.save()
    }
}