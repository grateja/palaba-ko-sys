package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "job_order_extras")
data class EntityJobOrderExtras(
    @ColumnInfo(name = "job_order_id")
    var jobOrderId: UUID?,

    @ColumnInfo(name = "extras_id")
    var extrasId: UUID,

    @ColumnInfo(name = "extras_name")
    var extrasName: String,

    @ColumnInfo(name = "price")
    var price: Float,

    @ColumnInfo(name = "quantity")
    var quantity: Int,

    @ColumnInfo(name = "category")
    var category: String?,

    @PrimaryKey(autoGenerate = false)
    override var id: UUID,
) : BaseEntity(id)/* {
    constructor(jobOrderId: UUID?, extrasId: UUID, extrasName: String, price: Float, quantity: Int, isPackage: Boolean, category: String?)
     : this(jobOrderId, extrasId, extrasName, price, quantity, isPackage, category, null)
}*/
