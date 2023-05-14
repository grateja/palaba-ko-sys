package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.csi.palabakosys.model.EnumDiscountApplicable
//import com.csi.palabakosys.model.DiscountTypeEnum

@Entity(tableName = "discounts")
class EntityDiscount(
    var name: String? = null,

    val percentage: Float,

//    @ColumnInfo(name = "discount_type")
//    val discountType: DiscountTypeEnum?,

    @ColumnInfo(name = "applicable_to")
    val applicableTo: List<EnumDiscountApplicable>
) : BaseEntity(null) {
//    constructor() : this(null,0f, 0f, null, null)

//    fun applicableTo() : List<EnumDiscountApplicable> {
//        return EnumDiscountApplicable.fromIds(this.applicableTo)
//    }
}