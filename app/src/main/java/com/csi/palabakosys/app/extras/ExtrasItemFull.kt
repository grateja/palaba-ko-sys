package com.csi.palabakosys.app.extras

import androidx.room.Embedded
import androidx.room.Relation
import com.csi.palabakosys.room.entities.EntityExtras
import com.csi.palabakosys.room.entities.EntityUser

data class ExtrasItemFull (
    @Embedded
    val extras: EntityExtras,

    @Relation(
        parentColumn = "deleted_by",
        entityColumn = "id"
    )
    val deletedBy: EntityUser?,
)