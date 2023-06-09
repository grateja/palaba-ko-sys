package com.csi.palabakosys.room.dao

import androidx.room.*
import com.csi.palabakosys.room.entities.BaseEntity
import java.time.Instant

interface BaseDao<T : BaseEntity> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(data: T)

    @Delete
    suspend fun deletePermanent(data: T)

    suspend fun softDelete(data: T) {
        data.deletedAt = Instant.now()
        save(data)
    }

    @Insert
    suspend fun saveAll(list: List<T>)
}