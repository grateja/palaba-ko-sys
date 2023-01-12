package com.csi.palabakosys.room.repository

import com.csi.palabakosys.room.dao.DaoDiscount
import com.csi.palabakosys.room.entities.EntityDiscount
import javax.inject.Inject


class DiscountsRepository
@Inject
constructor (
    private val dao: DaoDiscount,
) : BaseRepository<EntityDiscount>(dao) {
    override suspend fun get(id: String?) : EntityDiscount? {
        if(id == null) return null
        return dao.get(id)
    }

    suspend fun getAll(keyword: String) : List<EntityDiscount> {
        return dao.getAll(keyword)
    }
}