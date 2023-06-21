package com.csi.palabakosys.app.machines

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csi.palabakosys.model.EnumMachineType
import com.csi.palabakosys.room.entities.EntityMachine
import com.csi.palabakosys.room.repository.MachineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MachinesViewModel

@Inject
constructor(
    machineRepository: MachineRepository
) : ViewModel() {
    private val _machines = machineRepository.getListAsLiveData()
    private val _machineType = MutableLiveData(EnumMachineType.REGULAR_WASHER)
    val machines = MediatorLiveData<List<MachineListItem>>().apply {
        fun update() {
            val machines = _machines.value?: listOf()
            val machineType = _machineType.value
            value = machines.filter { it.machine.machineType == machineType }
        }
        addSource(_machines) { update() }
        addSource(_machineType) { update() }
    }

    fun setMachineType(machineType: String) {
        _machineType.value = EnumMachineType.fromName(machineType)
    }
}