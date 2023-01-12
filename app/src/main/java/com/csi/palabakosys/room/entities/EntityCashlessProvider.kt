package com.csi.palabakosys.room.entities

import androidx.room.Entity

@Entity(tableName = "cashless_providers")
class EntityCashlessProvider : BaseEntity() {
    var name: String? = null
}