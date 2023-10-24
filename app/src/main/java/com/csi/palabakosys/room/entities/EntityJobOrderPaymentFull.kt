package com.csi.palabakosys.room.entities

import androidx.room.Embedded
import androidx.room.Relation

data class EntityJobOrderPaymentFull(
    @Embedded
    val payment: EntityJobOrderPayment?,

    @Relation(
        parentColumn = "userId",
        entityColumn = "id",
        entity = EntityUser::class
    )
    val user: EntityUser,
//
//    @Relation(
//        parentColumn = "id",
//        entityColumn = "payment_id",
//        entity = EntityJobOrder::class
//    )
//    val jobOrders: List<EntityJobOrderWithItems>
) {
//    fun jobOrderReferences() : String {
//        return jobOrders.map { it.jobOrder.jobOrderNumber }.joinToString(",")
//    }
}