package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.csi.palabakosys.model.EnumDiscountType
import com.csi.palabakosys.model.EnumDiscountApplicable
//import com.csi.palabakosys.model.DiscountTypeEnum

@Entity(tableName = "discounts")
class EntityDiscount(
    var name: String? = null,

    var value: Float,

    @ColumnInfo(name = "discount_type")
    var discountType: EnumDiscountType?,

    @ColumnInfo(name = "applicable_to")
    var applicableTo: List<EnumDiscountApplicable>
) : BaseEntity(null) {
    constructor() : this(null, 0f, null, listOf())
}