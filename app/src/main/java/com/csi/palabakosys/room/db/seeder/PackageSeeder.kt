package com.csi.palabakosys.room.db.seeder

import com.csi.palabakosys.room.dao.DaoPackage
import com.csi.palabakosys.room.entities.EntityPackage
import kotlinx.coroutines.runBlocking

class PackageSeeder(private val daoPackage: DaoPackage) : EntitySeederImpl<EntityPackage>(daoPackage) {
    override fun items(): List<EntityPackage> {
        return listOf(
            EntityPackage("Regular Package", "8KG Regular Wash/Dry/Fold"),
            EntityPackage("Titan Package", "12KG Regular Wash/Dry/Fold"),
        )
    }
}