package com.csi.palabakosys.room.repository

import com.csi.palabakosys.room.dao.DaoShop
import com.csi.palabakosys.room.entities.EntityShop
import javax.inject.Inject


class ShopRepository
@Inject
constructor (
    private val daoShop: DaoShop,
) {
    suspend fun get() : EntityShop? {
        return daoShop.get()
    }

    suspend fun save(shop: EntityShop) {
        try {
            daoShop.save(shop)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}