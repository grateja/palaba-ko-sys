package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "other_services")
class EntityServiceOther : BaseEntity() {
    @ColumnInfo(name = "name")
    var name: String? = null

    @ColumnInfo(name = "price")
    var price: Float = 0f
}