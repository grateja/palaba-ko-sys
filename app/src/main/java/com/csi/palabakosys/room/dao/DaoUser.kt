package com.csi.palabakosys.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.csi.palabakosys.room.entities.EntityUser
import java.util.UUID

@Dao
abstract class DaoUser : BaseDao<EntityUser> {
    @Query("SELECT * FROM users WHERE id = :id")
    abstract suspend fun get(id: UUID) : EntityUser?

    @Query("SELECT * FROM users WHERE email = :email")
    abstract suspend fun getByEmail(email: String) : EntityUser?

    @Query("SELECT * FROM users")
    abstract suspend fun getAll() : List<EntityUser>

    @Query("SELECT * FROM users WHERE role = 'admin'")
    abstract suspend fun getAdmin() : EntityUser?

    @Query("SELECT * FROM users WHERE email = :email AND deleted_at IS NULL LIMIT 1")
    abstract fun getByEmailLiveData(email: String?): LiveData<EntityUser?>

    @Query("SELECT * FROM users WHERE email = :email AND password = :password AND deleted_at IS NULL LIMIT 1")
    abstract suspend fun getByEmailAndPassword(email: String, password: String): EntityUser?
}