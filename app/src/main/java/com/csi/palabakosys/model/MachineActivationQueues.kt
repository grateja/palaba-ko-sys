package com.csi.palabakosys.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class MachineActivationQueues(
    val machineId: UUID,
    val jobOrderServiceId: UUID,
    val customerId: UUID,
    val customerName: String,
    val serviceName: String,
    val machineName: String,
    var status: MachineConnectionStatus,
    var message: String,
): Parcelable {
    fun connecting() : Boolean {
        return status == MachineConnectionStatus.CONNECTING
    }
}