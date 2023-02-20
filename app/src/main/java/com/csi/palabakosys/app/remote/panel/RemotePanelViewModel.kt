package com.csi.palabakosys.app.remote.panel

import androidx.lifecycle.*
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
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
class RemotePanelViewModel

@Inject
constructor(
    private val queuesRepository: JobOrderQueuesRepository,
    private val machineRepository: MachineRepository,
    private val workManager: WorkManager
) : ViewModel() {
//    private val gson = Gson()
//    val title = MutableLiveData("Select Customer")
    val machines = MutableLiveData<List<EntityMachine>>()
//    val customerQueues = MutableLiveData<List<EntityCustomerQueueService>>()
//    val availableServices = MutableLiveData<List<EntityAvailableService>>()
    val machine = MutableLiveData<EntityMachine>()
    val queueService = MutableLiveData<EntityCustomerQueueService>()

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
//        this.machine.value = machine
//        viewModelScope.launch {
//            machineRepository.get(machine.id.toString())?.let {
//                title.value = "SELECT CUSTOMER"
//                machineTile.value = it
//                getQueuesByMachine()
//                println("worker id")
//                println(it.workerId)
//            }
//        }
    }

    fun selectCustomer(service: EntityCustomerQueueService?) {
        this.queueService.value = service
    }

    fun clearCustomer() {
        this.queueService.value = null
    }

//    private fun getQueuesByMachine() {
//        viewModelScope.launch {
//            machine.value?.let { _machineTile ->
//                queuesRepository.getByMachineType(_machineTile.machineType).let {
//                    customerQueues.value = it
//                    availableServices.value = emptyList()
//                }
//            }
//        }
//    }

//    private fun getQueuesByJobOrder() {
//        viewModelScope.launch {
//            queuesRepository.getAvailableServiceByJobOrder(queueService.value?.jobOrderId.toString(), machine.value?.machineType).let {
//                availableServices.value = it
//            }
//        }
//    }

//    fun activate(availableService: EntityAvailableService) : LiveData<WorkInfo>? {
//        val _machine = this.machine.value ?: return null
//
//        val remoteWorkerInput = Data.Builder()
//            .putString(RemoteWorker.TOKEN, availableService.id.toString())
//            .putInt(RemoteWorker.PULSE, availableService.service.serviceRef.pulse())
//            .putString(RemoteWorker.MACHINE_ID, _machine.id.toString())
//            .build()
//
//        val remoteWorker = OneTimeWorkRequestBuilder<RemoteWorker>()
//            .setInputData(remoteWorkerInput)
//            .build()
//
//        val debitWorkerInput = Data.Builder()
//            .putString(DebitServiceWorker.MACHINE_ID, it.id.toString())
//            .putString(DebitServiceWorker.JOB_ORDER_SERVICE_ID, gson.toJson(availableService.service.apply {
//                this.used += 1
//            }))
//            .putString(DebitServiceWorker.MACHINE_USAGE, gson.toJson(EntityMachineUsage()))
//            .build()
//
//
//        val debitWorker = OneTimeWorkRequestBuilder<DebitServiceWorker>()
//            .setInputData(debitWorkerInput)
//            .build()
//
//        println("worker id before start")
//        println(remoteWorker.id)
//
//        workManager.beginWith(remoteWorker)
//            .then(debitWorker)
//            .enqueue()
//
//        println("worker id after start")
//        println(remoteWorker.id)
//
//        return workManager.getWorkInfoByIdLiveData(remoteWorker.id)
//    }

    fun pendingResult(id: UUID) : LiveData<WorkInfo> {
        return workManager.getWorkInfoByIdLiveData(id)
    }
}