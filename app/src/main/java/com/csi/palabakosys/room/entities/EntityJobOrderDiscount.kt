package com.csi.palabakosys.room.entities

import androidx.room.*
import com.csi.palabakosys.model.DiscountTypeEnum
//import com.csi.palabakosys.model.DiscountTypeEnum
import java.util.*

@Entity(tableName = "job_order_discounts")
data class EntityJobOrderDiscount(
    @ColumnInfo(name = "discount_id")
    val discountId: UUID?,

    val name: String,

    val value: Float,

    val discountType: DiscountTypeEnum,

    @ColumnInfo(name = "applicable_to")
    val applicableToIds: String,

    @PrimaryKey(autoGenerate = false)
    override var id: UUID,
) : BaseEntity(id)