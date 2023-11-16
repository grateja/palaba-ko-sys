package com.csi.palabakosys.app.remote.queues

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.csi.palabakosys.model.MachineActivationQueues
import com.csi.palabakosys.room.entities.EntityCustomerQueueService
import com.csi.palabakosys.room.repository.JobOrderQueuesRepository
import com.csi.palabakosys.room.repository.MachineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RemoteQueuesViewModel

@Inject
constructor(
    private val queuesRepository: JobOrderQueuesRepository,
    private val machineRepository: MachineRepository
) : ViewModel() {
    private val _navigationState = MutableLiveData<NavigationState>()
    val navigationState: LiveData<NavigationState> = _navigationState

    private val _machineId = MutableLiveData<UUID>()
    val machine = _machineId.switchMap { machineRepository.getMachineLiveData(it) }

    val customerQueue = MutableLiveData<EntityCustomerQueueService>()
    val availableServices = customerQueue.switchMap { queuesRepository.getAvailableServicesByCustomerIdAsLiveData(it.customerId, it.machineType) } //MutableLiveData<List<EntityAvailableService>>()

    fun setMachineId(machineId: UUID) {
        this._machineId.value = machineId
    }

    fun setCustomerQueue(customerQueue: EntityCustomerQueueService?) {
        customerQueue?.let {
            this.customerQueue.value = it
        }
    }

    fun openActivationPreview(joServiceId: UUID) {
        val machineId = _machineId.value ?: return
        val customerId = customerQueue.value?.customerId ?: return
        val queues = MachineActivationQueues(machineId, joServiceId, customerId)
        _navigationState.value = NavigationState.OpenActivationPreview(queues)
    }

    fun resetNavigationState() {
        _navigationState.value = NavigationState.ResetState
    }

    sealed class NavigationState {
        object ResetState: NavigationState()
//        class OpenActivationPreview(val machineId: UUID, val joServiceId: UUID, val customerId: UUID) : NavigationState()
        class OpenActivationPreview(val queue: MachineActivationQueues) : NavigationState()
    }
}