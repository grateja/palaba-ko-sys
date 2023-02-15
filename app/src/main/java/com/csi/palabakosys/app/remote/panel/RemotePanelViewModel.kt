package com.csi.palabakosys.app.remote.panel

import androidx.lifecycle.*
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.csi.palabakosys.room.entities.EntityAvailableService
import com.csi.palabakosys.room.entities.EntityCustomerQueueService
import com.csi.palabakosys.room.entities.EntityMachine
import com.csi.palabakosys.room.repository.JobOrderQueuesRepository
import com.csi.palabakosys.room.repository.MachineRepository
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
)
: ViewModel() {
    val title = MutableLiveData("Select Customer")
    val machines = MutableLiveData<List<MachineTile>>()
    val customerQueues = MutableLiveData<List<EntityCustomerQueueService>>()
    val availableServices = MutableLiveData<List<EntityAvailableService>>()
    val machineTile = MutableLiveData<MachineTile>()
    val queueService = MutableLiveData<EntityCustomerQueueService>()

    val hasSelectedCustomer = MediatorLiveData<Boolean>().apply {
        fun update() {
            value = availableServices.value?.isNotEmpty()
        }

        addSource(availableServices) {update()}
    }

    fun loadAll() {
        viewModelScope.launch {
            machineRepository.getAll().let {
                machines.value = it.map { machine ->
                    MachineTile(machine)
                }
            }
        }
    }

    fun selectMachine(machineTile: MachineTile) {
        this.title.value = "SELECT CUSTOMER"
        this.machineTile.value = machineTile
        this.getQueuesByMachine()
    }

    fun selectJobOrder(service: EntityCustomerQueueService?) {
        this.title.value = "SELECT SERVICE"
        this.queueService.value = service
        this.getQueuesByJobOrder()
    }

    fun clearCustomer() {
        this.title.value = "SELECT CUSTOMER"
        this.queueService.value = null
        this.availableServices.value = emptyList()
    }

    private fun getQueuesByMachine() {
        viewModelScope.launch {
            machineTile.value?.let { _machineTile ->
                queuesRepository.getByMachineType(_machineTile.machine.machineType).let {
                    customerQueues.value = it
                    availableServices.value = emptyList()
                }
            }
        }
    }

    private fun getQueuesByJobOrder() {
        viewModelScope.launch {
            queuesRepository.getAvailableServiceByJobOrder(queueService.value?.jobOrderId.toString(), machineTile.value?.machine?.machineType).let {
                availableServices.value = it
            }
        }
    }

    fun activate(availableService: EntityAvailableService) : LiveData<WorkInfo> {
//        return viewModelScope.launch {
            val remoteWorkerInput = Data.Builder()
                .putString(RemoteWorker.TOKEN, availableService.id.toString())
                .putInt(RemoteWorker.PULSE, availableService.service.serviceRef.pulse())
                .putString(RemoteWorker.MACHINE_ID, machineTile.value?.machine?.id.toString())
                .build()

            val remoteWorker = OneTimeWorkRequestBuilder<RemoteWorker>()
                .setInputData(remoteWorkerInput)
                .build()

            println("worker id before start")
            println(remoteWorker.id)

            workManager.beginWith(remoteWorker)
                .enqueue()

            println("worker id after start")
            println(remoteWorker.id)

            return workManager.getWorkInfoByIdLiveData(remoteWorker.id)
//        }
    }

    fun pendingResult() : LiveData<WorkInfo>? {
        return machineTile.value?.machine?.activationRef?.workerId?.let {
            return workManager.getWorkInfoByIdLiveData(it)
        }
    }
}