package com.csi.palabakosys.room.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import com.csi.palabakosys.app.joborders.create.delivery.DeliveryCharge
import com.csi.palabakosys.app.joborders.create.discount.MenuDiscount
import com.csi.palabakosys.app.joborders.create.extras.MenuExtrasItem
import com.csi.palabakosys.app.joborders.create.products.MenuProductItem
import com.csi.palabakosys.app.joborders.create.services.MenuServiceItem
import com.csi.palabakosys.app.joborders.list.JobOrderListItem
import com.csi.palabakosys.app.joborders.payment.JobOrderPaymentMinimal
import com.csi.palabakosys.room.dao.DaoJobOrder
import com.csi.palabakosys.room.entities.EntityJobOrder
import com.csi.palabakosys.room.entities.EntityJobOrderListItem
import com.csi.palabakosys.room.entities.EntityJobOrderWithItems
import java.lang.Exception
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JobOrderRepository
@Inject
constructor (
    private val daoJobOrder: DaoJobOrder,
) {
    suspend fun get(id: UUID?) : EntityJobOrderWithItems? {
        try {
            if(id == null) return null
            return daoJobOrder.get(id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    suspend fun getAllWithTotalAmount(keyword: String, includeVoid :Boolean = false) : List<EntityJobOrderListItem> {
        return listOf() //daoJobOrder.getAllWithTotalAmount(keyword, includeVoid)
    }

    suspend fun getNextJONumber() : String {
        val currentJO = daoJobOrder.getLastJobOrderNumber()?.toInt() ?: 0
        return (currentJO + 1).toString().padStart(6, '0')
    }

    suspend fun save(jobOrderWithItem: EntityJobOrderWithItems) : EntityJobOrderWithItems? {
        try {
            daoJobOrder.save(jobOrderWithItem)
            return jobOrderWithItem
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    suspend fun saveAll(
        joNumber: String,
        customerId: UUID,
        preparedBy: String,
        services: List<MenuServiceItem>,
        products: List<MenuProductItem>,
        extras: List<MenuExtrasItem>,
        delivery: DeliveryCharge,
        discount: MenuDiscount) {

    }

    suspend fun void(jobOrder: EntityJobOrder) {
        daoJobOrder.voidJobOrder(jobOrder)
    }

    suspend fun getCurrentJobOrder(customerId: UUID?) : EntityJobOrderWithItems? {
        return daoJobOrder.getCurrentJobOrder(customerId)
    }

    suspend fun getAllUnpaidByCustomerId(customerId: UUID?): List<JobOrderPaymentMinimal> {
        return daoJobOrder.getAllUnpaidByCustomerId(customerId)
    }

    suspend fun getPreviousUnpaidByCustomerId(customerId: UUID, jobOrderId: UUID?): List<JobOrderPaymentMinimal> {
        return daoJobOrder.getPreviousUnpaidByCustomerId(customerId, jobOrderId)
    }

    suspend fun load(keyword: String?, orderBy: String?, sortDirection: String?): List<JobOrderListItem> {
        return daoJobOrder.load(keyword, orderBy, sortDirection)
    }
}