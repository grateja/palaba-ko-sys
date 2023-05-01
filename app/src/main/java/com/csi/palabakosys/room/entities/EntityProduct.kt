package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.csi.palabakosys.model.ProductType
import com.csi.palabakosys.util.MeasureUnit

@Entity(tableName = "products")
class EntityProduct(
    @ColumnInfo(name = "name")
    var name: String?,

    @ColumnInfo(name = "price")
    var price: Float,

    @ColumnInfo(name = "current_stock")
    var currentStock: Int,

    @ColumnInfo(name = "measure_unit")
    val measureUnit: MeasureUnit,

    @ColumnInfo(name = "unit_per_serve")
    val unitPerServe: Float,

    @ColumnInfo(name = "product_type")
    var productType: ProductType?
) : BaseEntity() {
    constructor() : this(null, 0f, 0, MeasureUnit.PCS, 0f, ProductType.OTHER)
//    @ColumnInfo(name = "name")
//    var name: String? = null
//
//    @ColumnInfo(name = "price")
//    var price: Float = 0f
//
//    @ColumnInfo(name = "current_stock")
//    var currentStock: Int = 0
//
//    @ColumnInfo(name = "product_type")
//    var productType: ProductType? = null
}