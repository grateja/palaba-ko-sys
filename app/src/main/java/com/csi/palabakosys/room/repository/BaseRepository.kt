package com.csi.palabakosys.room.repository

import com.csi.palabakosys.room.dao.BaseDao
import com.csi.palabakosys.room.entities.BaseEntity
import java.time.Instant

abstract class BaseRepository<Entity : BaseEntity>(
    private val crudDao: BaseDao<Entity>
) : IRepository<Entity> {
    override suspend fun save(entity: Entity) : Entity? {
        try {
            entity.updatedAt = Instant.now()
            crudDao.save(entity)
            return entity
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun delete(entity: Entity, permanent: Boolean) : Boolean {
        try {
            if(permanent) {
                crudDao.deletePermanent(entity)
            } else {
                crudDao.softDelete(entity)
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}