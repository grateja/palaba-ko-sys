package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity

@Entity(tableName = "services")
class EntityService(
    @ColumnInfo(name = "name")
    var name: String?,

    @ColumnInfo(name = "price")
    var price: Float,

    @Embedded
    var serviceRef: EntityServiceRef
) : BaseEntity()
