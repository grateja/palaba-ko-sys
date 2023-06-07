package com.csi.palabakosys.room.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.csi.palabakosys.app.joborders.create.delivery.DeliveryCharge

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

data class EntityPackageProductWithProduct(
    @Embedded
    val productCrossRef: EntityPackageProduct,

    @Relation(
        parentColumn = "product_id",
        entityColumn = "id",
        entity = EntityProduct::class
    )
    val product: EntityProduct
)

data class EntityPackageExtrasWithExtras(
    @Embedded
    val extrasCrossRef: EntityPackageExtras,

    @Relation(
        parentColumn = "extras_id",
        entityColumn = "id",
        entity = EntityExtras::class
    )
    val extras: EntityExtras
)

data class EntityPackageWithItems(
    @Embedded
    val prePackage: EntityPackage,

    @Relation(
        parentColumn = "id",
        entityColumn = "package_id",
        entity = EntityPackageService::class
    )
    val services: List<EntityPackageServiceWithService>?,

    @Relation(
        parentColumn = "id",
        entityColumn = "package_id",
        entity = EntityPackageExtras::class
    )
    val extras: List<EntityPackageExtrasWithExtras>?,

    @Relation(
        parentColumn = "id",
        entityColumn = "package_id",
        entity = EntityPackageProduct::class
    )
    val products: List<EntityPackageProductWithProduct>?,
)