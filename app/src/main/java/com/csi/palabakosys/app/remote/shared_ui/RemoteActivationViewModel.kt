package com.csi.palabakosys.app.remote.shared_ui

import androidx.lifecycle.*
import androidx.work.*
import com.csi.palabakosys.model.MachineType
import com.csi.palabakosys.room.entities.*
import com.csi.palabakosys.room.repository.JobOrderQueuesRepository
import com.csi.palabakosys.room.repository.MachineRepository
import com.csi.palabakosys.worker.RemoteWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RemoteActivationViewModel

@Inject
constructor(
    private val queuesRepository: JobOrderQueuesRepository,
    private val machineRepository: MachineRepository,
    private val workManager: WorkManager
) : ViewModel() {
    val dataState = MutableLiveData<DataState>()
    val machines = MutableLiveData<List<EntityMachine>>()
    val machine = MutableLiveData<EntityMachine>()
    val service = MutableLiveData<EntityAvailableService>()
    val selectedTab = MutableLiveData(MachineType.REGULAR_WASHER)

    val customerQueues = MutableLiveData<List<EntityCustomerQueueService>>()
    val customerQueue = MutableLiveData<EntityCustomerQueueService>()

    val availableServices = MutableLiveData<List<EntityAvailableService>>()

    fun resetState() {
        dataState.value = DataState.StateLess
    }

    fun loadMachines(machineType: MachineType) {
        viewModelScope.launch {
            machineRepository.getAll(machineType).let {
                machines.value = it
            }
        }
    }

    fun selectMachine(machineTile: EntityMachine?) {
        viewModelScope.launch {
            if(machineTile != null) {
                machineRepository.get(machineTile.id.toString())?.let {
                    machine.value = it
                }
            }
        }
    }

    fun getCustomersByMachineType() {
        viewModelScope.launch {
            machine.value?.let { _machine ->
                queuesRepository.getByMachineType(_machine.machineType).let {
                    customerQueues.value = it
                }
            }
        }
    }

    fun selectCustomer(_customerQueue: EntityCustomerQueueService) {
        viewModelScope.launch {
            customerQueue.value = _customerQueue
            getQueuesByCustomer(_customerQueue)
        }
    }

    fun selectService(service: EntityAvailableService) {
        this.service.value = service
    }

    private fun getQueuesByCustomer(queueService: EntityCustomerQueueService) {
        viewModelScope.launch {
            queuesRepository.getAvailableServiceByCustomerId(queueService.customerId.toString(), queueService.machineType).let {
                availableServices.value = it
            }
        }
    }

    fun activate() : LiveData<WorkInfo>? {
        println("activate")
        val machine = this.machine.value ?: return null
        val service = this.service.value ?: return null

        if(service.available <= 0) {
            return null
        }

        val remoteWorkerInput = Data.Builder()
            .putString(RemoteWorker.JOB_ORDER_SERVICE_ID, service.id.toString())
            .putString(RemoteWorker.MACHINE_ID, machine.id.toString())
            .build()

        val remoteWorker = OneTimeWorkRequestBuilder<RemoteWorker>()
            .setInputData(remoteWorkerInput)
            .build()

        workManager.enqueue(remoteWorker)

        dataState.value = DataState.InitiateConnection(machine.id, remoteWorker.id)

        return workManager.getWorkInfoByIdLiveData(remoteWorker.id)
    }

    fun pendingWork(workerId: UUID) : LiveData<WorkInfo> {
        return workManager.getWorkInfoByIdLiveData(workerId)
    }

    fun setMachineType(text: String?) {
        selectedTab.value = MachineType.fromString(text)
    }

    sealed class DataState {
        object StateLess: DataState()
        class InitiateConnection(val machineId: UUID, val workerId: UUID) : DataState()
    }
}