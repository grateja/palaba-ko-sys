package com.csi.palabakosys.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.csi.palabakosys.app.joborders.create.packages.MenuJobOrderPackage
import com.csi.palabakosys.app.joborders.create.services.MenuServiceItem
import com.csi.palabakosys.room.entities.EntityPackage
import com.csi.palabakosys.room.entities.EntityPackageService
import com.csi.palabakosys.room.entities.EntityPackageWithItems
import java.util.*

@Dao
abstract class DaoPackage : BaseDao<EntityPackage> {
    @Query("SELECT * FROM packages WHERE id = :id AND deleted_at IS NULL LIMIT 1")
    abstract suspend fun get(id: UUID?): EntityPackage?

    @Query("SELECT *, 1 as quantity FROM packages WHERE package_name LIKE '%' || :keyword || '%' AND deleted_at IS NULL")
    abstract suspend fun getAll(keyword: String?): List<MenuJobOrderPackage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun syncServices(packageServices: List<EntityPackageService>)

    @Query("SELECT * FROM packages WHERE id IN (:ids) AND deleted_at IS NULL")
    abstract suspend fun getByIds(ids: List<UUID>?) : List<EntityPackageWithItems>

    @Query("SELECT svs.*, psvs.quantity, 0 as used FROM services svs JOIN package_services psvs ON psvs.service_id = svs.id WHERE svs.id IN (:ids) AND deleted_at IS NULL")
    abstract suspend fun getServicesByIds(ids: List<UUID>) : List<MenuServiceItem>

    @Query("SELECT * FROM packages WHERE id = :packageId AND deleted_at IS NULL")
    abstract suspend fun getById(packageId: UUID): EntityPackageWithItems?
}