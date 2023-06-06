package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.*

@Entity(tableName = "packages")
data class EntityPackage(
    @ColumnInfo(name = "package_name")
    val packageName: String,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "delivery_id")
    val deliveryId: UUID?,
) : BaseEntity()