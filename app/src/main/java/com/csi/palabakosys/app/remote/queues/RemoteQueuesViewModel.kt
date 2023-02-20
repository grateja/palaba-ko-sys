package com.csi.palabakosys.app.remote.queues

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.app.customers.CustomerMinimal
import com.csi.palabakosys.app.machines.MachineMinimal
import com.csi.palabakosys.model.MachineType
import com.csi.palabakosys.room.entities.EntityAvailableService
import com.csi.palabakosys.room.repository.JobOrderQueuesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RemoteQueuesViewModel

@Inject
constructor(
    private val queuesRepository: JobOrderQueuesRepository,
) : ViewModel()
{
    val availableServices = MutableLiveData<List<EntityAvailableService>>()
    var customer: CustomerMinimal? = null
    var machine: MachineMinimal? = null

    fun getAvailableServicesByCustomerId() {
        viewModelScope.launch {
            queuesRepository.getAvailableServiceByCustomerId(customer?.id.toString(), machine?.machineType).let {
                availableServices.value = it
            }
        }
    }

    fun setCurrentCustomer(customer: CustomerMinimal?) {
        this.customer = customer
    }

    fun setCurrentMachine(machine: MachineMinimal?) {
        this.machine = machine
    }
}