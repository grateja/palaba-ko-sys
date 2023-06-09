package com.csi.palabakosys.room.repository

import com.csi.palabakosys.app.expenses.ExpenseItemFull
import com.csi.palabakosys.room.dao.DaoExpense
import com.csi.palabakosys.room.entities.EntityExpense
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpensesRepository
@Inject
constructor (
    private val dao: DaoExpense,
) : BaseRepository<EntityExpense>(dao) {
    override suspend fun get(id: UUID?) : EntityExpense? {
        if(id == null) return null
        return dao.get(id)
    }

    suspend fun filter(keyword: String): List<ExpenseItemFull> {
        return dao.getAll(keyword)
    }
}