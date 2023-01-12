package com.csi.palabakosys.viewmodels

import com.csi.palabakosys.room.entities.EntityMachine
import com.csi.palabakosys.room.repository.MachineRepository
import com.csi.palabakosys.util.DeleteViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeleteMachineViewModel
@Inject
constructor(
    repository: MachineRepository
) : DeleteViewModel<EntityMachine>(repository)