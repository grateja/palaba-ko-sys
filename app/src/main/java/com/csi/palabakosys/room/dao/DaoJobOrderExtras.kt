package com.csi.palabakosys.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.csi.palabakosys.room.entities.*
import java.time.LocalDate

@Dao
abstract class DaoJobOrderExtras : BaseDao<EntityJobOrderExtras> {
    @Query("SELECT extras_name, COUNT(*) as count, category FROM job_order_extras WHERE strftime('%Y-%m-%d', created_at / 1000, 'unixepoch') = :dateFrom OR ( :dateTo IS NOT NULL AND strftime('%Y-%m-%d', created_at / 1000, 'unixepoch') BETWEEN :dateFrom AND :dateTo ) GROUP BY category, extras_name")
    abstract fun getDashboard(dateFrom: LocalDate, dateTo: LocalDate?) : LiveData<List<EntityJobOrderExtrasAggrResult>>
}