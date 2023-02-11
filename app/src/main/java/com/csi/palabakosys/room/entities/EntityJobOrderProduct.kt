package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.csi.palabakosys.model.ProductType
import java.util.*

@Entity(tableName = "job_order_products")
data class EntityJobOrderProduct(
    @ColumnInfo(name = "job_order_id")
    var jobOrderId: UUID?,

    @ColumnInfo(name = "product_type")
    var productType: ProductType?,

    @ColumnInfo(name = "product_id")
    var productId: UUID?,

    @ColumnInfo(name = "product_name")
    var productName: String?,

    @ColumnInfo(name = "price")
    var price: Float,

    @ColumnInfo(name = "quantity")
    var quantity: Int,
) : BaseEntity()
