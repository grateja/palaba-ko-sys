package com.csi.palabakosys.room.entities

import androidx.room.*
import java.util.*

@Entity(tableName = "package_products")
data class EntityPackageProduct(
    @ColumnInfo(name = "package_id")
    val packageId: UUID,

    @ColumnInfo(name = "product_id")
    val productId: UUID,

    @ColumnInfo(name = "quantity")
    val quantity: Int,

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: UUID = UUID.randomUUID(),
)