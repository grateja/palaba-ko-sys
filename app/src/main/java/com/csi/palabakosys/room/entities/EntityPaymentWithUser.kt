package com.csi.palabakosys.room.entities

import androidx.room.Embedded
import androidx.room.Relation

data class EntityPaymentWithUser (
    @Embedded
    val payment: EntityJobOrderPayment,

    @Relation(
        parentColumn = "userId",
        entityColumn = "id",
        entity = EntityUser::class
    )
    val user: EntityUser
)