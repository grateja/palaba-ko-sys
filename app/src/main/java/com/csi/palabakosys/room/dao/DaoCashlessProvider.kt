package com.csi.palabakosys.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.csi.palabakosys.room.entities.EntityCashlessProvider

@Dao
abstract class DaoCashlessProvider : BaseDao<EntityCashlessProvider> {
    @Query("SELECT * FROM cashless_providers WHERE id = :id")
    abstract suspend fun get(id: String) : EntityCashlessProvider?

    @Query("SELECT * FROM cashless_providers WHERE name LIKE '%' || :keyword || '%' ORDER BY name")
    abstract suspend fun getAll(keyword: String) : List<EntityCashlessProvider>
}