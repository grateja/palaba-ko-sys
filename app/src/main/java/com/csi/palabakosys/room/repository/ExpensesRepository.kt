package com.csi.palabakosys.room.repository

import com.csi.palabakosys.room.dao.DaoExpense
import com.csi.palabakosys.room.entities.EntityExpense
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpensesRepository
@Inject
constructor (
    private val dao: DaoExpense,
) : BaseRepository<EntityExpense>(dao) {
    override suspend fun get(id: String?) : EntityExpense? {
        if(id == null) return null
        return dao.get(id)
    }

    suspend fun getAll(keyword: String) : List<EntityExpense> {
        return dao.getAll(keyword)
    }
}