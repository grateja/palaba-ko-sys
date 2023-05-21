package com.csi.palabakosys.app.remote.running

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.room.entities.EntityMachine
import com.csi.palabakosys.room.entities.EntityRunningMachine
import com.csi.palabakosys.room.repository.RemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MachineRunningViewModel

@Inject
constructor(
    private val remoteRepository: RemoteRepository
) : ViewModel()
{
    private val _machine = MutableLiveData<EntityRunningMachine>()

    fun get(machineId: UUID?) {
        viewModelScope.launch {
            _machine.value = remoteRepository.getRunningMachine(machineId)
            println("awesome")
            println(_machine.value)
        }
    }
}