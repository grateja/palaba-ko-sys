package com.csi.palabakosys.app.joborders.payment.cashless

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csi.palabakosys.model.Rule
import com.csi.palabakosys.room.entities.EntityCashless
import com.csi.palabakosys.util.InputValidation

class CashlessPaymentViewModel : ViewModel()
{
    private val _dataState = MutableLiveData<DataState>()
    val dataState: LiveData<DataState> = _dataState
    val provider = MutableLiveData("")
    val amount = MutableLiveData("")
    val reference = MutableLiveData("")

    private fun cashlessPayment() : EntityCashless {
        return EntityCashless(
            provider.value!!,
            reference.value!!,
            amount.value!!.toFloat()
        )
    }

    fun prepareSubmit() {
        val inputValidation = InputValidation()
        inputValidation.addRules("provider", provider.value!!, arrayOf(Rule.REQUIRED))
        inputValidation.addRules("amount", amount.value!!, arrayOf(Rule.REQUIRED))
        inputValidation.addRules("reference", reference.value!!, arrayOf(Rule.REQUIRED))
        if(inputValidation.isInvalid()) {
            _dataState.value = DataState.InvalidOperation(inputValidation)
        } else {
            _dataState.value = DataState.Submit(cashlessPayment())
        }
    }

    sealed class DataState {
        class InvalidOperation(val inputValidation: InputValidation): DataState()
        class Submit(val cashless: EntityCashless) : DataState()
    }
}