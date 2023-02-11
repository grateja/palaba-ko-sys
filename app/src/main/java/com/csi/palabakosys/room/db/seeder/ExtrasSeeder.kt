package com.csi.palabakosys.room.db.seeder

import com.csi.palabakosys.room.dao.DaoExtras
import com.csi.palabakosys.room.entities.EntityExtras

class ExtrasSeeder(dao: DaoExtras): EntitySeederImpl<EntityExtras>(dao) {
    override fun items(): List<EntityExtras> {
        return listOf(
            EntityExtras("8KG Fold", 30f, "fold"),
            EntityExtras("12KG Fold", 50f, "fold"),
        )
    }
}