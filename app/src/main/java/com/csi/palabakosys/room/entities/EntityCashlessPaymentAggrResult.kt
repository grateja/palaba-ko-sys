package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import com.csi.palabakosys.model.EnumMachineType

data class EntityCashlessPaymentAggrResult(
    @ColumnInfo(name = "cashless_provider")
    val provider: String?,

    @ColumnInfo(name = "count")
    val count: Int,

    @ColumnInfo(name = "amount")
    val amount: Float,
)