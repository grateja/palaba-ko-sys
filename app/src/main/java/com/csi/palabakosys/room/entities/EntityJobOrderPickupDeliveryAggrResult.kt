package com.csi.palabakosys.room.entities

import android.icu.util.MeasureUnit
import androidx.room.ColumnInfo
import com.csi.palabakosys.model.EnumDeliveryVehicle
import com.csi.palabakosys.model.EnumMachineType
import com.csi.palabakosys.model.EnumProductType
import com.csi.palabakosys.model.EnumWashType

data class EntityJobOrderPickupDeliveryAggrResult(
    @ColumnInfo(name = "vehicle")
    val vehicle: EnumDeliveryVehicle?,
    val count: Int,
)
