package com.csi.palabakosys.app.remote.shared_ui

import androidx.lifecycle.*
import androidx.work.*
import com.csi.palabakosys.model.EnumMachineType
import com.csi.palabakosys.room.entities.*
import com.csi.palabakosys.room.repository.JobOrderQueuesRepository
import com.csi.palabakosys.room.repository.MachineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RemoteActivationViewModel

@Inject
constructor(
    private val queuesRepository: JobOrderQueuesRepository
) : ViewModel() {
    val dataState = MutableLiveData<DataState>()
    val machine = MutableLiveData<EntityMachine>()
    val service = MutableLiveData<EntityAvailableService>()

    val customerQueues = MutableLiveData<List<EntityCustomerQueueService>>()
    val customerQueue = MutableLiveData<EntityCustomerQueueService>()

    val availableServices = MutableLiveData<List<EntityAvailableService>>()

    fun resetState() {
        dataState.value = DataState.StateLess
    }

    fun selectMachine(machine: EntityMachine?) {
        this.machine.value = machine
        this.loadCustomerQueuesByMachineType(machine?.machineType)
        this.dataState.value = DataState.SelectMachine
    }

    private fun loadCustomerQueuesByMachineType(machineType: EnumMachineType?) {
        viewModelScope.launch {
            machineType?.let {
                queuesRepository.getByMachineType(it).let { customers ->
                    customerQueues.value = customers
                }
            }
        }
    }

    fun selectCustomer(customerQueue: EntityCustomerQueueService) {
        this.customerQueue.value = customerQueue
        this.getQueuesByCustomer(customerQueue)
        this.dataState.value = DataState.SelectCustomer
    }

    private fun getQueuesByCustomer(queueService: EntityCustomerQueueService) {
        viewModelScope.launch {
            queuesRepository.getAvailableServiceByCustomerId(queueService.customerId, queueService.machineType).let {
                availableServices.value = it
            }
        }
    }

    fun selectService(service: EntityAvailableService) {
        this.service.value = service
        this.dataState.value = DataState.SelectService
    }


    fun activate() {
        val machine = this.machine.value ?: return
        val service = this.service.value ?: return

        dataState.value = DataState.InitiateConnection(machine.id, service.id!!)
    }

    sealed class DataState {
        object StateLess: DataState()
        object SelectMachine: DataState()
        object SelectCustomer: DataState()
        object SelectService: DataState()
        class InitiateConnection(val machineId: UUID, val workerId: UUID) : DataState()
    }
}