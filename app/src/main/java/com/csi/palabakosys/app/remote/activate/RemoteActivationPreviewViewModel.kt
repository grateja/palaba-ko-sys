package com.csi.palabakosys.app.remote.activate

import android.app.Activity
import androidx.lifecycle.*
import com.csi.palabakosys.model.MachineActivationQueues
import com.csi.palabakosys.model.MachineConnectionStatus
import com.csi.palabakosys.room.entities.EntityCustomer
import com.csi.palabakosys.room.entities.EntityJobOrderService
import com.csi.palabakosys.room.entities.EntityMachine
import com.csi.palabakosys.room.repository.CustomerRepository
import com.csi.palabakosys.room.repository.JobOrderQueuesRepository
import com.csi.palabakosys.room.repository.MachineRepository
import com.csi.palabakosys.room.repository.RemoteRepository
import com.csi.palabakosys.settings.DeveloperSettingsRepository
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
    private val remoteRepository: RemoteRepository,
    private val settingsRepository: DeveloperSettingsRepository
): ViewModel() {
    val fakeActivationOn = settingsRepository.fakeConnectionMode

    private val _validationMessage = MutableLiveData<String?>()
    val validationMessage: LiveData<String?> = _validationMessage

    private val _machineActivationQueue = MutableLiveData<MachineActivationQueues>()
    val machineActivationQueue: LiveData<MachineActivationQueues> = _machineActivationQueue

    private val _dataState = MutableLiveData<DataState>()
    val dataState: LiveData<DataState> = _dataState

//    private val machineId = MutableLiveData<UUID>()
    val machine = _machineActivationQueue.switchMap { machineRepository.getMachineLiveData(it.machineId) } //: LiveData<EntityMachine> = _machine

//    private val joServiceId = MutableLiveData<UUID>()
    val jobOrderService = _machineActivationQueue.switchMap { jobOrderQueuesRepository.getAsLiveData(it.jobOrderServiceId) } //: LiveData<EntityJobOrderService> = _jobOrderService

//    private val _customer = MutableLiveData<EntityCustomer>()
//    val customer: LiveData<EntityCustomer> = _customer
    val customer = _machineActivationQueue.switchMap {customerRepository.getCustomerAsLiveData(it.customerId) }

//    private val _machineStatus = MutableLiveData<MachineConnectionStatus>()
//    val machineStatus: LiveData<MachineConnectionStatus> = _machineStatus
//
//    private val _message = MutableLiveData<String>()
//    val message: LiveData<String> = _message

    fun setValidationMessage(message: String?) {
        _validationMessage.value = message
    }

//    fun setMachineId(id: UUID) {
//        machineId.value = id
//        _dataState.value = DataState.CheckPending(id)
//    }

//    fun setServiceId(id: UUID) {
//        joServiceId.value = id
//    }

//    fun setCustomerId(id: UUID) {
//        viewModelScope.launch {
//            _customer.value = customerRepository.get(id)
//        }
//    }

//    fun setMachineStatus(status: MachineConnectionStatus) {
//        _machineStatus.value = status
//    }
//
//    fun setMessage(message: String?) {
//        this._message.value = message
//    }

    fun setMachineActivationQueue(machineActivationQueues: MachineActivationQueues) {
        _machineActivationQueue.value = machineActivationQueues
//        if(machineActivationQueues.machineId == machineId.value) {
//            _machineActivationQueue.value = machineActivationQueues
//            joServiceId.value = machineActivationQueues.jobOrderServiceId
//
//        }
    }

    fun prepareSubmit() {
        val machineId = this.machine.value?.id ?: return
        val serviceId = this.jobOrderService.value?.id
        val customerId = this.customer.value?.id

        _dataState.value = DataState.InitiateActivation(MachineActivationQueues(machineId, serviceId, customerId))
    }

    fun dismiss() {
        val status = _machineActivationQueue.value?.status
        if(status == MachineConnectionStatus.CONNECTING || status == MachineConnectionStatus.SUCCESS) {
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
                _dataState.value = DataState.FixDataInconsistencies
            }
        }
    }

    fun updateQueue(queues: MachineActivationQueues) {
        val currentQueue = _machineActivationQueue.value
        if(currentQueue?.machineId != queues.machineId) {
            return
        } else {
            _machineActivationQueue.value = queues
        }
    }

    sealed class DataState {
        object StateLess: DataState()
        object FixDataInconsistencies : DataState()
        class InitiateActivation(val queue: MachineActivationQueues) : DataState()
        class Dismiss(val result: Int) : DataState()
    }
}