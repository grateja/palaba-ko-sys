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

    private val _inputValidation = MutableLiveData<InputValidation>()
    val inputValidation: LiveData<InputValidation> = _inputValidation

    private fun cashlessPayment() : EntityCashless {
        return EntityCashless(
            provider.value!!,
            reference.value!!,
            amount.value!!.toFloat()
        )
    }

    fun prepareSubmit() {
        val inputValidation = InputValidation()
        inputValidation.addRule("provider", provider.value, arrayOf(Rule.Required))
        inputValidation.addRule("amount", amount.value, arrayOf(Rule.Required))
        inputValidation.addRule("reference", reference.value, arrayOf(Rule.Required))
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