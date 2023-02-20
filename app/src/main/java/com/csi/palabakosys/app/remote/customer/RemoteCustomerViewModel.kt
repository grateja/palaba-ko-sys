package com.csi.palabakosys.app.remote.customer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.app.customers.CustomerMinimal
import com.csi.palabakosys.app.machines.MachineMinimal
import com.csi.palabakosys.model.MachineType
import com.csi.palabakosys.room.entities.EntityCustomerQueueService
import com.csi.palabakosys.room.repository.JobOrderQueuesRepository
import com.csi.palabakosys.room.repository.MachineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemoteCustomerViewModel

@Inject
constructor(
    private val queuesRepository: JobOrderQueuesRepository,
    private val machineRepository: MachineRepository,
) : ViewModel() {
    val dataState = MutableLiveData<DataState>()
    val customerQueues = MutableLiveData<List<EntityCustomerQueueService>>()
    val machine = MutableLiveData<MachineMinimal>()

    fun getMachine(machineId: String?) {
        viewModelScope.launch {
            machineRepository.get(machineId)?.let {
                machine.value = MachineMinimal(it.id, it.machineName(), it.machineType!!)
            }
        }
    }

    fun getCustomersByMachineType(machineType: MachineType) {
        viewModelScope.launch {
            queuesRepository.getByMachineType(machineType).let {
                customerQueues.value = it
            }
        }
    }

    fun selectCustomer(queue: EntityCustomerQueueService) {
        val customer = CustomerMinimal(queue.customerId, queue.customerName, "", null)
        val machine = this.machine.value!!
        dataState.value = DataState.SelectCustomer(customer, machine)
    }

    fun resetState() {
        dataState.value = DataState.StateLess
    }

    sealed class DataState {
        class SelectCustomer(val customer: CustomerMinimal, val machine: MachineMinimal): DataState()
        object StateLess: DataState()
    }
}