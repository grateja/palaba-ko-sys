//package com.csi.lms2022.room.dao
//
//import androidx.room.Dao
//import androidx.room.Query
//import com.csi.lms2022.room.entities.EntityServiceQueue
//
//@Dao
//abstract class DaoServiceQueue : BaseDao<EntityServiceQueue> {
//    @Query("SELECT * FROM service_queues WHERE id = :id")
//    abstract suspend fun get(id: String) : EntityServiceQueue?
//}