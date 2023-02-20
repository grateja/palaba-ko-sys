package com.csi.palabakosys.app.machines

import android.os.Parcelable
import com.csi.palabakosys.model.MachineType
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class MachineMinimal(
    val id: UUID,
    val name: String,
    val machineType: MachineType
) : Parcelable