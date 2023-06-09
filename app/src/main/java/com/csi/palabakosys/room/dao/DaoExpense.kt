package com.csi.palabakosys.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.csi.palabakosys.app.expenses.ExpenseItemFull
import com.csi.palabakosys.room.entities.EntityExpense
import java.util.UUID

@Dao
abstract class DaoExpense : BaseDao<EntityExpense> {
    @Query("SELECT * FROM expenses WHERE id = :id AND deleted_at IS NULL")
    abstract suspend fun get(id: UUID) : EntityExpense?

    @Query("SELECT * FROM expenses WHERE remarks LIKE '%' || :keyword || '%' AND deleted_at IS NULL ORDER BY remarks")
    abstract suspend fun getAll(keyword: String) : List<ExpenseItemFull>
}