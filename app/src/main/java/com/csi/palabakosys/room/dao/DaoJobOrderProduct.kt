package com.csi.palabakosys.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.csi.palabakosys.room.entities.EntityJobOrderProduct
import com.csi.palabakosys.room.entities.EntityJobOrderProductAggrResult
import com.csi.palabakosys.room.entities.EntityJobOrderService
import com.csi.palabakosys.room.entities.EntityJobOrderServiceAggrResult
import java.time.LocalDate

@Dao
abstract class DaoJobOrderProduct : BaseDao<EntityJobOrderProduct> {
    @Query("SELECT" +
            "     product_name," +
            "     COUNT(*) as count," +
            "     product_type," +
            "     measure_unit " +
            " FROM job_order_products" +
            " WHERE (date(created_at / 1000, 'unixepoch', 'localtime') = :dateFrom " +
            "     OR ( :dateTo IS NOT NULL AND date(created_at / 1000, 'unixepoch', 'localtime')" +
            "         BETWEEN :dateFrom AND :dateTo)) " +
            "     AND deleted_at IS NULL" +
            "     AND (void = 0)" +
            " GROUP BY product_name, product_type, measure_unit")
    abstract fun getDashboard(dateFrom: LocalDate, dateTo: LocalDate?) : LiveData<List<EntityJobOrderProductAggrResult>>
}