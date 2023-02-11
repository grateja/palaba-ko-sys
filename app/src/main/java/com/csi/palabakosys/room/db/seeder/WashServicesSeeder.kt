package com.csi.palabakosys.room.db.seeder

import com.csi.palabakosys.model.MachineType
import com.csi.palabakosys.model.WashType
import com.csi.palabakosys.room.dao.DaoService
import com.csi.palabakosys.room.entities.EntityServiceRef
import com.csi.palabakosys.room.entities.EntityService

class WashServicesSeeder(dao: DaoService): EntitySeederImpl<EntityService>(dao) {
    override fun items(): List<EntityService> {
        return listOf(
            EntityService("Hot Wash", 90f, EntityServiceRef(MachineType.REGULAR_WASHER, WashType.HOT, 47)),
            EntityService("Warm Wash", 80f, EntityServiceRef(MachineType.REGULAR_WASHER, WashType.WARM, 36)),
            EntityService("Cold Wash", 70f, EntityServiceRef(MachineType.REGULAR_WASHER, WashType.COLD, 36)),
            EntityService("Delicate Wash", 50f, EntityServiceRef(MachineType.REGULAR_WASHER, WashType.DELICATE, 21)),
            EntityService("Regular Dry", 80f, EntityServiceRef(MachineType.REGULAR_DRYER, null, 40)),
            EntityService("Additional Dry", 30f, EntityServiceRef(MachineType.REGULAR_DRYER, null, 10)),
            EntityService("Hot Wash", 110f, EntityServiceRef(MachineType.TITAN_WASHER, WashType.HOT, 47)),
            EntityService("Warm Wash", 100f, EntityServiceRef(MachineType.TITAN_WASHER, WashType.WARM, 36)),
            EntityService("Cold Wash", 90f, EntityServiceRef(MachineType.TITAN_WASHER, WashType.COLD, 36)),
            EntityService("Delicate Wash", 70f, EntityServiceRef(MachineType.TITAN_WASHER, WashType.DELICATE, 21)),
            EntityService("Regular Dry", 100f, EntityServiceRef(MachineType.TITAN_DRYER, null, 40)),
            EntityService("Additional Dry", 50f, EntityServiceRef(MachineType.TITAN_DRYER, null, 10)),
        )
    }
}