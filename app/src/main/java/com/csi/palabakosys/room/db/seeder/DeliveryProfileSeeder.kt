package com.csi.palabakosys.room.db.seeder

import com.csi.palabakosys.app.joborders.create.delivery.MenuDeliveryProfile
import com.csi.palabakosys.model.DeliveryVehicle
import com.csi.palabakosys.room.dao.DaoDeliveryProfile
import com.csi.palabakosys.room.entities.EntityDeliveryProfile


class DeliveryProfileSeeder(dao: DaoDeliveryProfile): EntitySeederImpl<EntityDeliveryProfile>(dao) {
    override fun items(): List<EntityDeliveryProfile> {
        return listOf(
            EntityDeliveryProfile(DeliveryVehicle.TRIKE_PEDAL, 20f, 10f, 1f),
            EntityDeliveryProfile(DeliveryVehicle.TRIKE_ELECTRIC, 25f, 10f, 5f),
            EntityDeliveryProfile(DeliveryVehicle.MOTORCYCLE, 30f, 12f, 10f),
            EntityDeliveryProfile(DeliveryVehicle.TRICYCLE, 40f, 12f, 5f),
            EntityDeliveryProfile(DeliveryVehicle.SEDAN, 50f, 15f, 30f),
            EntityDeliveryProfile(DeliveryVehicle.MPV, 60f, 15f, 50f),
            EntityDeliveryProfile(DeliveryVehicle.SMALL_VAN, 70f, 17f, 100f),
        )
    }
}