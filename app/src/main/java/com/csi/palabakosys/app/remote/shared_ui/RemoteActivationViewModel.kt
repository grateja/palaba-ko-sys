package com.csi.palabakosys.app.remote.shared_ui

import androidx.lifecycle.*
import androidx.work.*
import com.csi.palabakosys.room.entities.*
import com.csi.palabakosys.room.repository.JobOrderQueuesRepository
import com.csi.palabakosys.room.repository.MachineRepository
import com.csi.palabakosys.worker.DebitServiceWorker
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
//    val dataState = MutableLiveData<DataState>()
    val machines = MutableLiveData<List<EntityMachine>>()
    val machine = MutableLiveData<EntityMachine>()

    val customerQueues = MutableLiveData<List<EntityCustomerQueueService>>()
    val customerQueue = MutableLiveData<EntityCustomerQueueService>()

    val availableServices = MutableLiveData<List<EntityAvailableService>>()

    fun loadMachines() {
        viewModelScope.launch {
            machineRepository.getAll().let {
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

    private fun getQueuesByCustomer(queueService: EntityCustomerQueueService) {
        viewModelScope.launch {
            queuesRepository.getAvailableServiceByCustomerId(queueService.customerId.toString(), queueService.machineType).let {
                availableServices.value = it
            }
        }
    }

    fun activate(availableService: EntityAvailableService) : LiveData<WorkInfo>? {
        println("activate")
        val machine = this.machine.value ?: return null

        if(availableService.available <= 0) {
//            dataState.value = DataState.Invalidate("No available service")
            return null
        }

        val remoteWorkerInput = Data.Builder()
            .putString(RemoteWorker.JOB_ORDER_SERVICE_ID, availableService.id.toString())
            .putString(RemoteWorker.MACHINE_ID, machine.id.toString())
            .build()

        val remoteWorker = OneTimeWorkRequestBuilder<RemoteWorker>()
            .setInputData(remoteWorkerInput)
            .build()

        val debitWorker = OneTimeWorkRequestBuilder<DebitServiceWorker>()
            .setInputData(remoteWorkerInput)
            .build()

        workManager
            .beginUniqueWork("activate", ExistingWorkPolicy.KEEP, remoteWorker)
            .then(debitWorker)
            .enqueue()

        return workManager.getWorkInfoByIdLiveData(remoteWorker.id)
    }

    fun pendingResult(id: UUID) : LiveData<WorkInfo> {
        return workManager.getWorkInfoByIdLiveData(id)
    }

//    fun resetState() {
//        dataState.value = DataState.StateLess
//    }
//
//    sealed class DataState {
//        object StateLess: DataState()
//        class Invalidate(val message: String): DataState()
//        class ActivateRequest(val machine: EntityMachine, val workerId: UUID): DataState()
//    }
}