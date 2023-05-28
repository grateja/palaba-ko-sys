package com.csi.palabakosys.room.entities

import androidx.room.Embedded
import androidx.room.Relation

data class EntityJobOrderPaymentFull(
    @Embedded
    val payment: EntityJobOrderPayment?,

    @Relation(
        parentColumn = "id",
        entityColumn = "payment_id"
    )
    val jobOrders: List<EntityJobOrder>
)