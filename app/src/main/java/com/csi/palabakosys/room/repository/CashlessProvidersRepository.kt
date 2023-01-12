package com.csi.palabakosys.room.repository

import com.csi.palabakosys.room.dao.DaoCashlessProvider
import com.csi.palabakosys.room.entities.EntityCashlessProvider
import javax.inject.Inject

class CashlessProvidersRepository
@Inject
constructor (
    private val dao: DaoCashlessProvider,
) : BaseRepository<EntityCashlessProvider>(dao) {
    override suspend fun get(id: String?) : EntityCashlessProvider? {
        if(id == null) return null
        return dao.get(id)
    }

    suspend fun getAll(keyword: String) : List<EntityCashlessProvider> {
        return dao.getAll(keyword)
    }
}