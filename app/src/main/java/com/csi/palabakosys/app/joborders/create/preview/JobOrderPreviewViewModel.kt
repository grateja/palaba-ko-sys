package com.csi.palabakosys.app.joborders.create.preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class JobOrderPreviewViewModel

@Inject
constructor(

) : ViewModel() {
    private lateinit var jobOrderId: UUID
    private lateinit var customerId: UUID

    private val _dataState = MutableLiveData<DataState>()
    val dataState: LiveData<DataState> = _dataState

    sealed class DataState {
        data class OpenPayment(val customerId: UUID) : DataState()
    }

    fun setJobOrderId(jobOrderId: UUID) {
        this.jobOrderId = jobOrderId
    }

    fun setCustomerId(customerId: UUID) {
        this.customerId = customerId
    }

    fun openPayment() {
        _dataState.value = DataState.OpenPayment(customerId)
    }
}