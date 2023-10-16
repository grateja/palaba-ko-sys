package com.csi.palabakosys.room.repository

import com.csi.palabakosys.app.dashboard.data.DateFilter
import com.csi.palabakosys.app.expenses.ExpenseItemFull
import com.csi.palabakosys.room.dao.DaoExpense
import com.csi.palabakosys.room.dao.DaoJobOrderProduct
import com.csi.palabakosys.room.dao.DaoJobOrderService
import com.csi.palabakosys.room.entities.EntityExpense
import com.csi.palabakosys.room.entities.EntityJobOrderService
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JobOrderProductRepository
@Inject
constructor (
    private val dao: DaoJobOrderProduct,
) {
    fun getDashboard(dateFilter: DateFilter) = dao.getDashboard(dateFilter.dateFrom, dateFilter.dateTo)
}