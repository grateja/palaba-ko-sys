package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.csi.palabakosys.model.ProductType

@Entity(tableName = "products")
class EntityProduct : BaseEntity() {
    @ColumnInfo(name = "name")
    var name: String? = null

    @ColumnInfo(name = "price")
    var price: Float = 0f

    @ColumnInfo(name = "current_stock")
    var currentStock: Int = 0

    @ColumnInfo(name = "product_type")
    var productType: ProductType? = null
}