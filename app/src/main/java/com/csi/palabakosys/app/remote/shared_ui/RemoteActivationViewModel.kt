package com.csi.palabakosys.app.remote.shared_ui

import androidx.lifecycle.*
import androidx.work.*
import com.csi.palabakosys.model.EnumMachineType
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
//    val machines = MutableLiveData<List<EntityMachine>>()
    val machine = MutableLiveData<EntityMachine>()
    val service = MutableLiveData<EntityAvailableService>()
//    val selectedTab = MutableLiveData(EnumMachineType.REGULAR_WASHER)

    val customerQueues = MutableLiveData<List<EntityCustomerQueueService>>()
    val customerQueue = MutableLiveData<EntityCustomerQueueService>()

    val availableServices = MutableLiveData<List<EntityAvailableService>>()

    fun resetState() {
        dataState.value = DataState.StateLess
    }

//    fun loadMachines(machineType: EnumMachineType) {
//        viewModelScope.launch {
//            machineRepository.getAll(machineType).let {
//                machines.value = it
//            }
//        }
//    }

    fun selectMachine(machineTile: EntityMachine?) {
        viewModelScope.launch {
            if(machineTile != null) {
                machineRepository.get(machineTile.id)?.let {
                    machine.value = it
                }
            }
        }
    }

    fun getCustomersByMachineType() {
        viewModelScope.launch {
            machine.value?.machineType?.let {
                queuesRepository.getByMachineType(it).let { list ->
                    customerQueues.value = list
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
            queuesRepository.getAvailableServiceByCustomerId(queueService.customerId, queueService.machineType).let {
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

//    fun setMachineType(text: String?) {
//        println("machine type")
//        println(text)
//        selectedTab.value = EnumMachineType.fromName(text)
//    }

    sealed class DataState {
        object StateLess: DataState()
        class InitiateConnection(val machineId: UUID, val workerId: UUID) : DataState()
    }
}