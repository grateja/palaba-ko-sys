package com.csi.palabakosys.room.db.seeder

import com.csi.palabakosys.model.MachineType
import com.csi.palabakosys.model.WashType
import com.csi.palabakosys.room.dao.BaseDao
import com.csi.palabakosys.room.dao.DaoWashService
import com.csi.palabakosys.room.entities.EntityServiceWash

class WashServicesSeeder(dao: DaoWashService): EntitySeederImpl<EntityServiceWash>(dao) {
    override fun items(): List<EntityServiceWash> {
        return listOf(
            EntityServiceWash("Hot Wash", 46, 90f, MachineType.REGULAR_WASHER, WashType.HOT),
            EntityServiceWash("Warm Wash", 36, 70f, MachineType.REGULAR_WASHER, WashType.WARM),
            EntityServiceWash("Delicate Wash", 21, 40f, MachineType.REGULAR_WASHER, WashType.DELICATE),
            EntityServiceWash("Super Wash", 47, 100f, MachineType.REGULAR_WASHER, WashType.SUPER_WASH),
            EntityServiceWash("Regular Dry", 40, 70f, MachineType.REGULAR_DRYER, null),
            EntityServiceWash("Additional Dry", 10, 20f, MachineType.REGULAR_DRYER, null),
            EntityServiceWash("Hot Wash", 46, 100f, MachineType.TITAN_WASHER, WashType.HOT),
            EntityServiceWash("Warm Wash", 36, 90f, MachineType.TITAN_WASHER, WashType.WARM),
            EntityServiceWash("Delicate Wash", 21, 60f, MachineType.TITAN_WASHER, WashType.DELICATE),
            EntityServiceWash("Super Wash", 47, 120f, MachineType.TITAN_WASHER, WashType.SUPER_WASH),
            EntityServiceWash("Regular Dry", 40, 90f, MachineType.TITAN_DRYER, null),
            EntityServiceWash("Additional Dry", 10, 25f, MachineType.TITAN_DRYER, null),
        )
    }
}