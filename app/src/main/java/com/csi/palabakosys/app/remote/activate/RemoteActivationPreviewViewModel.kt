package com.csi.palabakosys.app.remote.activate

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.model.MachineConnectionStatus
import com.csi.palabakosys.room.entities.EntityCustomer
import com.csi.palabakosys.room.entities.EntityJobOrderService
import com.csi.palabakosys.room.entities.EntityMachine
import com.csi.palabakosys.room.repository.CustomerRepository
import com.csi.palabakosys.room.repository.JobOrderQueuesRepository
import com.csi.palabakosys.room.repository.MachineRepository
import com.csi.palabakosys.room.repository.RemoteRepository
import com.csi.palabakosys.util.MachineActivationBus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RemoteActivationPreviewViewModel

@Inject
constructor(
    private val machineRepository: MachineRepository,
    private val customerRepository: CustomerRepository,
    private val jobOrderQueuesRepository: JobOrderQueuesRepository,
    private val remoteRepository: RemoteRepository
): ViewModel() {
    private val _dataState = MutableLiveData<DataState>()
    val dataState: LiveData<DataState> = _dataState

    private val _machine = MutableLiveData<EntityMachine>()
    val machine: LiveData<EntityMachine> = _machine

    private val _jobOrderService = MutableLiveData<EntityJobOrderService>()
    val jobOrderService: LiveData<EntityJobOrderService> = _jobOrderService

    private val _customer = MutableLiveData<EntityCustomer>()
    val customer: LiveData<EntityCustomer> = _customer

    private val _machineStatus = MutableLiveData<MachineConnectionStatus>()
    val machineStatus: LiveData<MachineConnectionStatus> = _machineStatus

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun setMachineId(id: UUID) {
        viewModelScope.launch {
            _machine.value = machineRepository.get(id)
            _dataState.value = DataState.CheckPending(id)
        }
    }

    fun setServiceId(id: UUID) {
        viewModelScope.launch {
            _jobOrderService.value = jobOrderQueuesRepository.get(id)
        }
    }

    fun setCustomerId(id: UUID) {
        viewModelScope.launch {
            _customer.value = customerRepository.get(id)
        }
    }

    fun setMachineStatus(status: MachineConnectionStatus) {
        _machineStatus.value = status
    }

    fun setMessage(message: String?) {
        this._message.value = message
    }

    fun prepareSubmit() {
        val machineId = this.machine.value?.id
        val serviceId = this.jobOrderService.value?.id
        val customerId = this.customer.value?.id

        _dataState.value = DataState.InitiateActivation(machineId, serviceId, customerId)
    }

    fun dismiss() {
        println("machine status")
        println(machineStatus.value)
        if(machineStatus.value == MachineConnectionStatus.CONNECTING || machineStatus.value == MachineConnectionStatus.SUCCESS) {
            _dataState.value = DataState.Dismiss(Activity.RESULT_OK)
        } else {
            _dataState.value = DataState.Dismiss(Activity.RESULT_CANCELED)
        }
    }

    fun resetState() {
        _dataState.value = DataState.StateLess
    }

    fun fix() {
        viewModelScope.launch {
            val machine = machine.value
            val machineId = machine?.id
            val serviceId = machine?.serviceActivationId
            if(machineId != null && serviceId != null) {
                remoteRepository.revertActivation(machineId, serviceId)
            }
        }
    }

    sealed class DataState {
        object StateLess: DataState()
        class CheckPending(val machineId: UUID) : DataState()
        class InitiateActivation(val machineId: UUID?, val workerId: UUID?, val customerId: UUID?) : DataState()
        class Dismiss(val result: Int) : DataState()
    }
}