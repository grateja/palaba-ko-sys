package com.csi.palabakosys.room.repository

import com.csi.palabakosys.app.joborders.create.packages.MenuJobOrderPackage
import com.csi.palabakosys.room.dao.DaoPackage
import com.csi.palabakosys.room.entities.EntityPackage
import com.csi.palabakosys.room.entities.EntityPackageWithItems
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JobOrderPackageRepository

@Inject
constructor(private val daoPackage: DaoPackage) : BaseRepository<EntityPackage>(daoPackage) {
    override suspend fun get(id: UUID?): EntityPackage? {
        return daoPackage.get(id)
    }

    suspend fun getAll(keyword: String?): List<MenuJobOrderPackage> {
        return daoPackage.getAll(keyword)
    }

    suspend fun getByIds(ids: List<UUID>?): List<EntityPackageWithItems> {
        return daoPackage.getByIds(ids)
    }

    suspend fun getById(packageId: UUID): EntityPackageWithItems? {
        return daoPackage.getById(packageId)
    }
}