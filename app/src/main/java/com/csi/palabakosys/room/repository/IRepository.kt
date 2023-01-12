package com.csi.palabakosys.room.repository

interface IRepository<T> {
    suspend fun get(id: String?) : T?
    suspend fun delete(entity: T) : Boolean
}