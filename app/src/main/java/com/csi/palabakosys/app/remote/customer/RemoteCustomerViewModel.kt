package com.csi.palabakosys.app.remote.customer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.csi.palabakosys.room.entities.EntityCustomerQueueService
import com.csi.palabakosys.room.repository.JobOrderQueuesRepository
import com.csi.palabakosys.room.repository.MachineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RemoteCustomerViewModel

@Inject
constructor(
    private val machineRepository: MachineRepository,
    private val queuesRepository: JobOrderQueuesRepository
) : ViewModel() {
    private val _navigationState = MutableLiveData<NavigationState>()
    val navigationState: LiveData<NavigationState> = _navigationState

    fun setMachineId(machineId: UUID) {
        this.machineId.value = machineId
    }

    val machineId = MutableLiveData<UUID?>()
    val machine = machineId.switchMap { machineRepository.getMachineLiveData(it) }
    val customerQueues = machine.switchMap { queuesRepository.getByMachineType(it?.machineType) }

    fun resetNavigationState() {
        _navigationState.value = NavigationState.ResetState
    }

    fun openQueueServices(queueService: EntityCustomerQueueService) {
        machineId.value ?.let {
            _navigationState.value = NavigationState.OpenQueuesServices(it, queueService)
        }
    }

    sealed class NavigationState {
        object ResetState: NavigationState()
        data class OpenQueuesServices(val machineId: UUID, val customerQueueService: EntityCustomerQueueService) : NavigationState()
    }
}