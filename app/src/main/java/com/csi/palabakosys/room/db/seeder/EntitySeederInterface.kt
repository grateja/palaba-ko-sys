package com.csi.palabakosys.room.db.seeder

import com.csi.palabakosys.room.entities.BaseEntity

interface EntitySeederInterface<T: BaseEntity> {
    fun items() : List<T>
    suspend fun seed()
}