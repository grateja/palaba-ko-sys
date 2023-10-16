package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import com.csi.palabakosys.model.EnumMeasureUnit
import com.csi.palabakosys.model.EnumProductType

data class EntityJobOrderProductAggrResult(
    @ColumnInfo(name = "product_name")
    val name: String?,
    val count: String,

    @ColumnInfo(name = "product_type")
    val productType: EnumProductType?,

    @ColumnInfo(name = "measure_unit")
    val measureUnit: EnumMeasureUnit?,
)
