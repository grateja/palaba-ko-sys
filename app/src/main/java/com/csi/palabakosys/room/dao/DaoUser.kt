package com.csi.palabakosys.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.csi.palabakosys.room.entities.EntityUser

@Dao
abstract class DaoUser : BaseDao<EntityUser> {
    @Query("SELECT * FROM users WHERE id = :id")
    abstract suspend fun get(id: String) : EntityUser?

    @Query("SELECT * FROM users WHERE email = :email")
    abstract suspend fun getByEmail(email: String) : EntityUser?

    @Query("SELECT * FROM users")
    abstract suspend fun getAll() : List<EntityUser>

    @Query("SELECT * FROM users WHERE role = 'admin'")
    abstract suspend fun getAdmin() : EntityUser?
}