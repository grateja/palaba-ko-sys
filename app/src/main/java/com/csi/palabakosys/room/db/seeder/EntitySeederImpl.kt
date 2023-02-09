package com.csi.palabakosys.room.db.seeder

import com.csi.palabakosys.room.dao.BaseDao
import com.csi.palabakosys.room.entities.BaseEntity

abstract class EntitySeederImpl<T: BaseEntity>(val dao: BaseDao<T>) : EntitySeederInterface<T> {
    override suspend fun seed() {
        dao.saveAll(items())
    }
}