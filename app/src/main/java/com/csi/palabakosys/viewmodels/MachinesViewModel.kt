package com.csi.palabakosys.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.R
import com.csi.palabakosys.model.MachineType
import com.csi.palabakosys.room.repository.MachineRepository
import com.csi.palabakosys.adapters.Adapter
//import com.csi.palabakosys.adapters.RecyclerItem
import com.csi.palabakosys.room.entities.EntityMachine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MachinesViewModel
@Inject
constructor(
    private val machinesRepository: MachineRepository
) : ViewModel() {

    val selectedMachine: MutableLiveData<EntityMachine> = MutableLiveData()
//    val regularWashersAdapter = Adapter<EntityMachine>(R.layout.recycler_machines)
//    val regularDryersAdapter = Adapter<EntityMachine>(R.layout.recycler_machines)
//    val titanWashersAdapter = Adapter<EntityMachine>(R.layout.recycler_machines)
//    val titanDryersAdapter = Adapter<EntityMachine>(R.layout.recycler_machines)
    val liveData = MutableLiveData<List<EntityMachine>>()

    init {
//        regularWashersAdapter.onItemClick = {
//            selectedMachine.value = it.getItem()
//        }
//
//        regularDryersAdapter.onItemClick = {
//            selectedMachine.value = it.getItem()
//        }
//
//        titanWashersAdapter.onItemClick = {
//            selectedMachine.value = it.getItem()
//        }
//
//        titanDryersAdapter.onItemClick = {
//            selectedMachine.value = it.getItem()
//        }
    }

    fun getMachines() {
        viewModelScope.launch {
            val machines = machinesRepository.getAll()
//            regularWashersAdapter.setData(machines.filter { it.machineType == MachineType.REGULAR_WASHER }.map { RecyclerItem(it) })
//            regularDryersAdapter.setData(machines.filter { it.machineType == MachineType.REGULAR_DRYER }.map { RecyclerItem(it) })
//            titanWashersAdapter.setData(machines.filter { it.machineType == MachineType.TITAN_WASHER }.map { RecyclerItem(it) })
//            titanDryersAdapter.setData(machines.filter { it.machineType == MachineType.TITAN_DRYER }.map { RecyclerItem(it) })
            liveData.value = machines
        }
    }

    fun save(machine: EntityMachine) {
        viewModelScope.launch {
            machinesRepository.save(machine)
        }
    }
}