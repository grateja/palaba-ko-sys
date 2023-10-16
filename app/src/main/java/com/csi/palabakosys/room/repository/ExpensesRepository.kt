package com.csi.palabakosys.room.repository

import com.csi.palabakosys.app.dashboard.data.DateFilter
import com.csi.palabakosys.app.expenses.ExpenseItemFull
import com.csi.palabakosys.room.dao.DaoExpense
import com.csi.palabakosys.room.entities.EntityExpense
import java.time.LocalDate
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

    suspend fun filter(keyword: String, dateFilter: DateFilter?): List<ExpenseItemFull> {
        return dao.getAll(keyword, dateFilter?.dateFrom, dateFilter?.dateTo)
    }

    fun getDashboard(dateFilter: DateFilter) = dao.getDashboard(dateFilter.dateFrom, dateFilter.dateTo)

    fun getTags() = dao.getTags()
}