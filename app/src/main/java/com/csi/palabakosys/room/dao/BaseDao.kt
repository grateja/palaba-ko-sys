package com.csi.palabakosys.room.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.csi.palabakosys.room.entities.BaseEntity

interface BaseDao<T : BaseEntity> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(data: T)

    @Delete
    suspend fun delete(data: T)

    @Insert
    suspend fun saveAll(list: List<T>)
}