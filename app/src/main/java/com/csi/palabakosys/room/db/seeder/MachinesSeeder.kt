package com.csi.palabakosys.room.db.seeder

import com.csi.palabakosys.model.MachineType
import com.csi.palabakosys.room.dao.DaoMachine
import com.csi.palabakosys.room.entities.EntityMachine

class MachinesSeeder(dao: DaoMachine) : EntitySeederImpl<EntityMachine>(dao) {
    override fun items(): List<EntityMachine> {
        return listOf(
            EntityMachine(1, MachineType.REGULAR_WASHER, 11, 1),
            EntityMachine(2, MachineType.REGULAR_WASHER, 12, 2),
            EntityMachine(2, MachineType.REGULAR_WASHER, 13, 3),
            EntityMachine(2, MachineType.REGULAR_WASHER, 14, 4),
            EntityMachine(1, MachineType.REGULAR_DRYER, 31, 1),
            EntityMachine(2, MachineType.REGULAR_DRYER, 32, 2),
            EntityMachine(2, MachineType.REGULAR_DRYER, 33, 3),
            EntityMachine(2, MachineType.REGULAR_DRYER, 34, 4),
            EntityMachine(1, MachineType.TITAN_WASHER, 21, 1),
            EntityMachine(2, MachineType.TITAN_WASHER, 22, 2),
            EntityMachine(2, MachineType.TITAN_WASHER, 23, 3),
            EntityMachine(2, MachineType.TITAN_WASHER, 24, 4),
            EntityMachine(1, MachineType.TITAN_DRYER, 41, 1),
            EntityMachine(2, MachineType.TITAN_DRYER, 42, 2),
            EntityMachine(2, MachineType.TITAN_DRYER, 43, 3),
            EntityMachine(2, MachineType.TITAN_DRYER, 44, 4),
        )
    }
}