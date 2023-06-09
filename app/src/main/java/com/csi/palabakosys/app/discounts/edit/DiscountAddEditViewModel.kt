package com.csi.palabakosys.app.discounts.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.model.EnumDiscountApplicable
import com.csi.palabakosys.model.Rule
import com.csi.palabakosys.room.entities.EntityDiscount
import com.csi.palabakosys.room.entities.EntityExpense
import com.csi.palabakosys.room.repository.DiscountsRepository
import com.csi.palabakosys.room.repository.ExpensesRepository
import com.csi.palabakosys.util.DataState
import com.csi.palabakosys.util.InputValidation
import com.csi.palabakosys.viewmodels.CreateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DiscountAddEditViewModel

@Inject
constructor(
    private val repository: DiscountsRepository
) : CreateViewModel<EntityDiscount>(repository) {
    fun get(id: UUID?) {
        viewModelScope.launch {
            val entity = super.get(id, EntityDiscount())
            _applicableTo.value = EnumDiscountApplicable.values().map { enum ->
                DiscountApplicableViewModel(enum, entity.applicableTo.any {it.id == enum.id})
            }
        }
    }

    private val _applicableTo = MutableLiveData<List<DiscountApplicableViewModel>>()
    val applicableTo: LiveData<List<DiscountApplicableViewModel>> = _applicableTo

    fun syncApplicable(applicable: DiscountApplicableViewModel) {
        _applicableTo.value = _applicableTo.value?.apply {
            this.find {it.applicable.id == applicable.applicable.id}?.selected = applicable.selected
        }
    }

//    private val applicableTo = EnumDiscountApplicable.values().map {
//        DiscountApplicableViewModel(it, false)
//    }

    override fun save() {
        this.validation.value = InputValidation().apply {
            addRules("remarks", model.value?.name, arrayOf(Rule.Required))
            addRules("amount", model.value?.value, arrayOf(Rule.Required, Rule.IsNumeric(model.value?.value)))
            addRules("discountType", model.value?.discountType, arrayOf(Rule.Required))
        }

        val applicable = this.applicableTo.value?.filter { it.selected }

        if(applicable == null || applicable.isEmpty()) {
            dataState.value = DataState.Invalidate("Please select at least one")
            return
        }

        model.value?.applicableTo = applicable.map { it.applicable }

        super.save()
    }
}