package com.csi.palabakosys.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.csi.palabakosys.room.entities.*
import java.time.LocalDate

@Dao
abstract class DaoJobOrderPickupDelivery {
    @Query("SELECT" +
            "     vehicle," +
            "     COUNT(*) as count" +
            " FROM job_order_delivery_charges" +
            " WHERE (strftime('%Y-%m-%d', created_at / 1000, 'unixepoch') = :dateFrom" +
            "     OR ( :dateTo IS NOT NULL AND strftime('%Y-%m-%d', created_at / 1000, 'unixepoch')" +
            "          BETWEEN :dateFrom AND :dateTo))" +
            "     AND (deleted_at IS NULL)" +
            "     AND (void = 0)" +
            " GROUP BY vehicle")
    abstract fun getDashboard(dateFrom: LocalDate, dateTo: LocalDate?) : LiveData<List<EntityJobOrderPickupDeliveryAggrResult>>
}