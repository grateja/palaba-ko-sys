package com.csi.palabakosys.room.repository

import com.csi.palabakosys.room.dao.DaoJobOrder
import com.csi.palabakosys.room.entities.EntityJobOrder
import com.csi.palabakosys.room.entities.EntityJobOrderListItem
import com.csi.palabakosys.room.entities.EntityJobOrderWithItems
import java.lang.Exception
import javax.inject.Inject

class JobOrderRepository
@Inject
constructor (
    private val daoJobOrder: DaoJobOrder,
) {
    suspend fun get(id: String?) : EntityJobOrderWithItems? {
        try {
            if(id == null) return null
            return daoJobOrder.get(id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    suspend fun getAllWithTotalAmount(keyword: String, includeVoid :Boolean = false) : List<EntityJobOrderListItem> {
        return daoJobOrder.getAllWithTotalAmount(keyword, includeVoid)
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

    suspend fun void(jobOrder: EntityJobOrder) {
        daoJobOrder.voidJobOrder(jobOrder)
    }
}