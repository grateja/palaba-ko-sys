package com.csi.palabakosys.room.db.seeder

import com.csi.palabakosys.model.EnumMachineType
import com.csi.palabakosys.room.dao.DaoMachine
import com.csi.palabakosys.room.entities.EntityMachine

class MachinesSeeder(dao: DaoMachine) : EntitySeederImpl<EntityMachine>(dao) {
    override fun items(): List<EntityMachine> {
        val list = mutableListOf<EntityMachine>()
        var counter = 10
        EnumMachineType.values().forEach {
            for(i in 1..10) {
                list.add(EntityMachine(i,it, ++counter, i))
            }
        }

        return list
    }
}