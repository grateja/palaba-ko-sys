package com.csi.palabakosys.app.remote

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.csi.palabakosys.model.EnumMachineType
import com.csi.palabakosys.room.repository.MachineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RemotePanelViewModel

@Inject
constructor(
    private val machineRepository: MachineRepository
) : ViewModel() {
    private val machineType = MutableLiveData(EnumMachineType.REGULAR_WASHER)
    val machines = machineType.switchMap { machineRepository.getListAsLiveData(it) } // machineRepository.getListAsLiveData()

    fun setMachineType(machineType: String) {
        this.machineType.value = EnumMachineType.fromName(machineType)
    }
}