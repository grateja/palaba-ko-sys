package com.csi.palabakosys.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.csi.palabakosys.room.entities.EntityExpense

@Dao
abstract class DaoExpense : BaseDao<EntityExpense> {
    @Query("SELECT * FROM expenses WHERE id = :id")
    abstract suspend fun get(id: String) : EntityExpense?

    @Query("SELECT * FROM expenses WHERE remarks LIKE '%' || :keyword || '%' ORDER BY remarks")
    abstract suspend fun getAll(keyword: String) : List<EntityExpense>
}