package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.UUID

@Entity(tableName = "expenses")
class EntityExpense : BaseEntity() {
    var remarks: String? = null
    var amount: Float? = 0f
    var tag: String? = null

    @ColumnInfo(name = "user_id")
    var userId: UUID? = null
}