package com.csi.palabakosys.app.remote.panel

import androidx.lifecycle.MutableLiveData
import com.csi.palabakosys.app.remote.shared_ui.MachineStatus
import com.csi.palabakosys.room.entities.EntityMachine

data class MachineTile(
    val entityMachine: EntityMachine,
) {
    var connecting = false
}