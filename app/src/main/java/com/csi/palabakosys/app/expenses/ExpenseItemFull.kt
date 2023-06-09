package com.csi.palabakosys.app.expenses

import androidx.room.Embedded
import androidx.room.Relation
import com.csi.palabakosys.room.entities.EntityExpense
import com.csi.palabakosys.room.entities.EntityUser

data class ExpenseItemFull(
    @Embedded
    val expense: EntityExpense,

    @Relation(
        parentColumn = "created_by",
        entityColumn = "id"
    )
    val createdBy: EntityUser,

    @Relation(
        parentColumn = "deleted_by",
        entityColumn = "id"
    )
    val deletedBy: EntityUser?,
)