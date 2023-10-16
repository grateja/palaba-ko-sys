package com.csi.palabakosys.room.entities

import android.icu.util.MeasureUnit
import androidx.room.ColumnInfo
import com.csi.palabakosys.model.EnumMachineType
import com.csi.palabakosys.model.EnumProductType
import com.csi.palabakosys.model.EnumWashType

data class EntityJobOrderExtrasAggrResult(
    @ColumnInfo(name = "extras_name")
    val name: String?,
    val count: String,

    val category: String?,
)
