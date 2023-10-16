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
    @Query("SELECT product_name, COUNT(*) as count, product_type, measure_unit FROM job_order_products WHERE strftime('%Y-%m-%d', created_at / 1000, 'unixepoch') = :dateFrom OR ( :dateTo IS NOT NULL AND strftime('%Y-%m-%d', created_at / 1000, 'unixepoch') BETWEEN :dateFrom AND :dateTo ) GROUP BY product_name, product_type, measure_unit")
    abstract fun getDashboard(dateFrom: LocalDate, dateTo: LocalDate?) : LiveData<List<EntityJobOrderProductAggrResult>>
}