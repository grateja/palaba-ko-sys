package com.csi.palabakosys.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class MachineActivationQueues(
    val machineId: UUID,
    val jobOrderServiceId: UUID,
    var status: MachineConnectionStatus,
    var message: String,
): Parcelable