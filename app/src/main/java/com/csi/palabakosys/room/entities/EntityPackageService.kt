package com.csi.palabakosys.room.entities

import androidx.room.*
import java.util.*

@Entity(tableName = "package_services")
data class EntityPackageService(
    @ColumnInfo(name = "package_id")
    val packageId: UUID,

    @ColumnInfo(name = "service_id")
    val serviceId: UUID,

    @ColumnInfo(name = "quantity")
    val quantity: Int,

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: UUID = UUID.randomUUID(),
)