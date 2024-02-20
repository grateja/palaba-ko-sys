package com.csi.palabakosys.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.csi.palabakosys.room.entities.*
import java.time.LocalDate

@Dao
abstract class DaoJobOrderExtras : BaseDao<EntityJobOrderExtras> {
    @Query("SELECT" +
            "     extras_name," +
            "     COUNT(*) as count," +
            "     category" +
            " FROM job_order_extras" +
            " WHERE (date(created_at / 1000, 'unixepoch', 'localtime') = :dateFrom " +
            "     OR ( :dateTo IS NOT NULL AND date(created_at / 1000, 'unixepoch', 'localtime') " +
            "          BETWEEN :dateFrom AND :dateTo))" +
            "     AND (deleted_at IS NULL)" +
            "     AND (void = 0)" +
            " GROUP BY category, extras_name")
    abstract fun getDashboard(dateFrom: LocalDate, dateTo: LocalDate?) : LiveData<List<EntityJobOrderExtrasAggrResult>>
}