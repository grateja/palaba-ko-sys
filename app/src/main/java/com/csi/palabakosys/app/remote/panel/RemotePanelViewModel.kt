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
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
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
    private val gson = Gson()
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
        val remoteWorkerInput = Data.Builder()
            .putString(RemoteWorker.TOKEN, availableService.id.toString())
            .putInt(RemoteWorker.PULSE, availableService.service.serviceRef.pulse())
            .putString(RemoteWorker.MACHINE_ID, machineTile.value?.machine?.id.toString())
            .build()

        val remoteWorker = OneTimeWorkRequestBuilder<RemoteWorker>()
            .setInputData(remoteWorkerInput)
            .build()

        val debitWorkerInput = Data.Builder()
            .putString(DebitServiceWorker.MACHINE, gson.toJson(machineTile.value?.machine?.apply {
                this.activationRef = EntityActivationRef(Instant.now(), availableService.minutes, null, availableService.jobOrderId, remoteWorker.id)
            }))
            .putString(DebitServiceWorker.JOB_ORDER_SERVICE, gson.toJson(availableService.service.apply {
                this.used += 1
            }))
            .putString(DebitServiceWorker.MACHINE_USAGE, gson.toJson(EntityMachineUsage()))
            .build()

        val debitWorker = OneTimeWorkRequestBuilder<DebitServiceWorker>()
            .setInputData(debitWorkerInput)
            .build()

        println("worker id before start")
        println(remoteWorker.id)

        workManager.beginWith(remoteWorker)
            .then(debitWorker)
            .enqueue()

        println("worker id after start")
        println(remoteWorker.id)

        return workManager.getWorkInfoByIdLiveData(remoteWorker.id)
    }

    fun pendingResult() : LiveData<WorkInfo>? {
        return machineTile.value?.machine?.activationRef?.workerId?.let {
            return workManager.getWorkInfoByIdLiveData(it)
        }
    }
}