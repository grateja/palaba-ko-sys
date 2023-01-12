package com.csi.palabakosys.room.entities

import androidx.room.Entity

@Entity(tableName = "discounts")
class EntityDiscount : BaseEntity() {
    var name: String? = null
    var percentage: Float = 0f
}