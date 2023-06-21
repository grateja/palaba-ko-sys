package com.csi.palabakosys.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.csi.palabakosys.app.joborders.create.packages.MenuJobOrderPackage
import com.csi.palabakosys.app.joborders.create.services.MenuServiceItem
import com.csi.palabakosys.room.entities.*
import java.util.*

@Dao
abstract class DaoPackage : BaseDao<EntityPackage> {
    @Query("SELECT * FROM packages WHERE id = :id AND deleted_at IS NULL LIMIT 1")
    abstract suspend fun get(id: UUID?): EntityPackage?

//    @Query("SELECT *, 1 as quantity FROM packages WHERE package_name LIKE '%' || :keyword || '%' AND deleted_at IS NULL")
    @Query("SELECT packages.id, packages.package_name," +
            "    COALESCE((" +
            "        SELECT SUM(products.price * package_products.quantity)" +
            "        FROM package_products" +
            "        JOIN products ON package_products.product_id = products.id" +
            "        WHERE package_products.package_id = packages.id" +
            "    ), 0) +" +
            "    COALESCE((" +
            "        SELECT SUM(services.price * package_services.quantity)" +
            "        FROM package_services" +
            "        JOIN services ON package_services.service_id = services.id" +
            "        WHERE package_services.package_id = packages.id" +
            "    ), 0) +" +
            "    COALESCE((" +
            "        SELECT SUM(extras.price * package_extras.quantity)" +
            "        FROM package_extras" +
            "        JOIN extras ON package_extras.extras_id = extras.id" +
            "        WHERE package_extras.package_id = packages.id" +
            "    ), 0) AS total_price" +
            ", description, 1 as quantity FROM packages WHERE package_name LIKE '%' || :keyword || '%' AND deleted_at IS NULL")
    abstract suspend fun getAll(keyword: String?): List<MenuJobOrderPackage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun syncServices(packageServices: List<EntityPackageService>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun syncExtras(packageExtras: MutableList<EntityPackageExtras>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun syncProducts(packageProducts: MutableList<EntityPackageProduct>)

    @Query("SELECT * FROM packages WHERE id IN (:ids) AND deleted_at IS NULL")
    abstract suspend fun getByIds(ids: List<UUID>?) : List<EntityPackageWithItems>

    @Query("SELECT * FROM packages WHERE deleted_at IS NULL ORDER BY package_name")
    abstract fun getAllAsLiveData() : LiveData<List<EntityPackageWithItems>>

    @Query("SELECT svs.*, psvs.quantity, 0 as used FROM services svs JOIN package_services psvs ON psvs.service_id = svs.id WHERE svs.id IN (:ids) AND deleted_at IS NULL")
    abstract suspend fun getServicesByIds(ids: List<UUID>) : List<MenuServiceItem>

    @Query("SELECT * FROM packages WHERE id = :packageId AND deleted_at IS NULL")
    abstract suspend fun getById(packageId: UUID): EntityPackageWithItems?
}