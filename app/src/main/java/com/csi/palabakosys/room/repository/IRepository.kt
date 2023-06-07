package com.csi.palabakosys.room.repository

import java.util.UUID

interface IRepository<T> {
    suspend fun get(id: UUID?) : T?
    suspend fun delete(entity: T) : Boolean
    suspend fun save(entity: T) : T?
}