package com.csi.palabakosys.app.remote

import androidx.lifecycle.ViewModel
import com.csi.palabakosys.room.repository.MachineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RemotePanelViewModel

@Inject
constructor(
    private val machineRepository: MachineRepository
) : ViewModel() {
    val machines = machineRepository.getListAsLiveData()
}