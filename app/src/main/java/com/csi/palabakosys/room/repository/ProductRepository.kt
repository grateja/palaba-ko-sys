package com.csi.palabakosys.room.repository

import com.csi.palabakosys.room.dao.DaoProduct
import com.csi.palabakosys.room.entities.EntityProduct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository
@Inject
constructor (
    private val daoProduct: DaoProduct,
) : BaseRepository<EntityProduct> (daoProduct) {
    override suspend fun get(id: String?) : EntityProduct? {
        if(id == null) return null

        return daoProduct.get(id)
    }

    suspend fun getAll() : List<EntityProduct> {
        return daoProduct.getAll()
    }
}