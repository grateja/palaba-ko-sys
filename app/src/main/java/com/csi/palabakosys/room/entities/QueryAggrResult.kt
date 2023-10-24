package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo

data class QueryAggrResult(
    @ColumnInfo("total_count")
    val count: Int,

    @ColumnInfo("total_sum")
    val sum: Float
)
