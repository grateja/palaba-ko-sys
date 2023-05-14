package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.csi.palabakosys.model.EnumProductType
import com.csi.palabakosys.util.MeasureUnit
import java.util.*

@Entity(tableName = "job_order_products")
data class EntityJobOrderProduct(
    @ColumnInfo(name = "job_order_id")
    var jobOrderId: UUID?,

    @ColumnInfo(name = "product_id")
    var productId: UUID,

    @ColumnInfo(name = "product_name")
    var productName: String,

    @ColumnInfo(name = "price")
    var price: Float,

    @ColumnInfo(name = "measure_unit")
    val measureUnit: MeasureUnit,

    @ColumnInfo(name = "unit_per_serve")
    val unitPerServe: Float,

    @ColumnInfo(name = "quantity")
    var quantity: Int,

    @ColumnInfo(name = "product_type")
    var productType: EnumProductType,

    @Ignore
    @ColumnInfo
    val uuid: UUID? = null
) : BaseEntity(uuid) {
    constructor(jobOrderId: UUID?, productId: UUID, productName: String, price: Float, measureUnit: MeasureUnit, unitPerServe: Float, quantity: Int, productType: EnumProductType)
     : this(jobOrderId, productId, productName, price, measureUnit, unitPerServe, quantity, productType, null)
}
