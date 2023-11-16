package com.csi.palabakosys.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.csi.palabakosys.room.entities.EntityJobOrderService
import com.csi.palabakosys.room.entities.EntityJobOrderServiceAggrResult
import java.time.LocalDate

@Dao
abstract class DaoJobOrderService : BaseDao<EntityJobOrderService> {
    @Query("SELECT" +
            "     service_name," +
            "     COUNT(*) as count," +
            "     svc_machine_type," +
            "     svc_wash_type" +
            " FROM job_order_services" +
            " WHERE (strftime('%Y-%m-%d', created_at / 1000, 'unixepoch') = :dateFrom " +
            "     OR ( :dateTo IS NOT NULL AND strftime('%Y-%m-%d', created_at / 1000, 'unixepoch') " +
            "          BETWEEN :dateFrom AND :dateTo ))" +
            "     AND (deleted_at IS NULL)" +
            "     AND (void = 0)" +
            " GROUP BY service_name, svc_wash_type, svc_machine_type")
    abstract fun getDashboard(dateFrom: LocalDate, dateTo: LocalDate?) : LiveData<List<EntityJobOrderServiceAggrResult>>
}