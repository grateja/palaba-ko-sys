package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.*

@Entity(tableName = "packages")
data class EntityPackage(
    @ColumnInfo(name = "package_name")
    var packageName: String,

    @ColumnInfo(name = "description")
    var description: String?,

    @ColumnInfo(name = "total_price")
    val totalPrice: Float?,

//    @ColumnInfo(name = "delivery_id")
//    val deliveryId: UUID?,
) : BaseEntity() {
    constructor() : this("", "", null)
}