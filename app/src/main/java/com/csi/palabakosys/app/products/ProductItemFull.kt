package com.csi.palabakosys.app.products

import androidx.room.Embedded
import androidx.room.Relation
import com.csi.palabakosys.room.entities.EntityExpense
import com.csi.palabakosys.room.entities.EntityProduct
import com.csi.palabakosys.room.entities.EntityUser

data class ProductItemFull(
    @Embedded
    val product: EntityProduct,

    @Relation(
        parentColumn = "deleted_by",
        entityColumn = "id"
    )
    val deletedBy: EntityUser?,
)