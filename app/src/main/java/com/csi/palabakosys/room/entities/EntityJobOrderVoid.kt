package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import java.time.Instant

class EntityJobOrderVoid(
    @ColumnInfo(name = "void_by")
    var voidBy: String?,

    @ColumnInfo(name = "void_remarks")
    var remarks: String?,

    @ColumnInfo(name = "void_date")
    var date: Instant? = Instant.now(),
)