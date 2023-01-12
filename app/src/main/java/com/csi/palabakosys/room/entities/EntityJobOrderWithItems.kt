package com.csi.palabakosys.room.entities

import androidx.room.Embedded
import androidx.room.Relation

data class EntityJobOrderWithItems (
    @Embedded var jobOrder: EntityJobOrder,

    @Relation(
        parentColumn = "customer_id",
        entityColumn = "id"
    )
    var customer: EntityCustomer,

    @Relation(
        parentColumn = "id",
        entityColumn = "job_order_id"
    )
    var services: List<EntityJobOrderService>,

    @Relation(
        parentColumn = "id",
        entityColumn = "job_order_id"
    )
    var products: List<EntityJobOrderProduct>,

) {
    @Relation(
        parentColumn = "id",
        entityColumn = "job_order_id"
    )
    var payment: EntityJobOrderPayment? = null
//
//    @Relation(
//        parentColumn = "id",
//        entityColumn = "job_order_id"
//    )
//    var serviceQueues: List<EntityServiceQueue> = emptyList()
//
//    init {
//        services = services.map {
//            it.jobOrderId = jobOrder.id
//            it
//        }
//        products = products.map {
//            it.jobOrderId = jobOrder.id
//            it
//        }
//    }

    fun totalAmount () : Float {
        return services.map {
            it.price * it.quantity
        }.sum() + products.map {
            it.price * it.quantity
        }.sum()
    }

    fun cashBack() : Float {
        return services.map {
            it.cashBack * it.quantity
        }.sum()
    }
}