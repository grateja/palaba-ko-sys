package com.csi.palabakosys.room.entities

import androidx.room.Embedded
import androidx.room.Relation

data class EntityPackageServiceWithService(
    @Embedded
    val serviceCrossRef: EntityPackageService,

    @Relation(
        parentColumn = "service_id",
        entityColumn = "id",
        entity = EntityService::class
    )
    val service: EntityService
)

data class EntityPackageWithItems(
    @Embedded
    val prePackage: EntityPackage,

    @Relation(
        parentColumn = "id",
        entityColumn = "package_id",
        entity = EntityPackageService::class
    )
    val services: List<EntityPackageServiceWithService>?
)