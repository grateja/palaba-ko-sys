//package com.csi.palabakosys.viewmodels
//
//import androidx.lifecycle.*
//import androidx.lifecycle.Observer
//import androidx.work.*
////import com.csi.palabakosys.datastates.RemoteActivationState
//import com.csi.palabakosys.room.entities.*
//import com.csi.palabakosys.room.repository.JobOrderQueuesRepository
//import com.csi.palabakosys.room.repository.MachineRepository
//import com.google.gson.Gson
////import com.csi.lms2022.usecases.ConnectUseCase
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import java.util.*
//import javax.inject.Inject
//
//@HiltViewModel
//class MenuRemoteViewModel
//@Inject
//constructor(
//    private val repository: JobOrderQueuesRepository,
//    private val machineRepo: MachineRepository,
//    private val workManager: WorkManager
//) : ViewModel() {
//    private val gson = Gson()
//    val machine = MutableLiveData<EntityMachine>()
////    val remoteJobOrderAdapter = Adapter<EntityCustomerQueueService>(R.layout.recycler_item_remote_job_orders)
////    val remoteWashServicesAdapter = Adapter<EntityAvailableService>(R.layout.recycler_item_available_service)
////    val dataState = MutableLiveData<RemoteActivationState>()
////    private val workManager = WorkManager.getInstance(context)
//
//    val observer = Observer<WorkInfo> {
//        println("Previous worker")
//    }
//
////    private var remoteWorker: OneTimeWorkRequest? = null
////    private var updateWorker: OneTimeWorkRequest? = null
//
//    fun loadServicesByJobOrders(jobOrderId: UUID) {
//        viewModelScope.launch {
////            repository.getAvailableServiceByCustomerId(jobOrderId, machine.value?.machineType).let {
////                remoteWashServicesAdapter.setData(it.map { _item -> RecyclerItem(_item) })
////            }
//        }
//    }
//
//    fun get(machineId: UUID?) {
//        viewModelScope.launch {
//            machineRepo.get(machineId)?.let { _machine ->
//                machine.value = _machine
////                repository.getByMachineType(_machine.machineType).let {
////                    remoteJobOrderAdapter.setData(it.map { _item -> RecyclerItem(_item) })
////                }
//            }
//        }
//    }
//
////    fun pendingResult() : LiveData<WorkInfo>? {
////        return machine.value?.workerId?.let {
////            return workManager.getWorkInfoByIdLiveData(it)
////        }
////    }
//
//    fun activate(availableService: EntityAvailableService) : LiveData<WorkInfo>? {
////        viewModelScope.launch {
//            machine.value?.let { _machine ->
////                return remoteJobOrderAdapter.getSelectedItem()?.let { queue ->
////                    val machine = _machine.apply {
////                        customerName = queue.getItem().customerName
////                        totalMinutes = availableService.minutes
////                        timeActivated = Instant.now()
////                    }
////                    val jobOrderService = availableService.service.apply {
////                        used += 1
////                    }
////
////                    val machineUsage = EntityMachineUsage().apply {
////                        machineId = _machine.id
////                        customerName = queue.getItem().customerName
////                        minutes = availableService.minutes
////                        price = availableService.service.price
////                    }
////
////                    val remoteWorkerInput = Data.Builder()
////                        .putString("machineId", machine.id.toString())
////                        .putString("token", machineUsage.id.toString())
////                        .putInt("pulse", availableService.washType?.pulse!!)
////                        .build()
////
////                    val remoteWorker = OneTimeWorkRequestBuilder<RemoteWorker>()
////                        .setInputData(remoteWorkerInput)
////                        .build()
////
////                    val updateWorkerInput = Data.Builder()
////                        .putString("machine", gson.toJson(machine))
////                        .putString("joService", gson.toJson(jobOrderService))
////                        .putString("machineUsage", gson.toJson(machineUsage))
////                        .build()
////
////                    val updateWorker = OneTimeWorkRequestBuilder<DebitServiceWorker>()
////                        .setInputData(updateWorkerInput)
////                        .build()
////
////                    workManager.beginWith(remoteWorker)
////                        .then(updateWorker)
////                        .enqueue()
////
////                    return workManager.getWorkInfoByIdLiveData(remoteWorker.id)
////                }
//            }
//        println("Activate returns null")
//        return null
////        }
//    }
//}