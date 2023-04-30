package com.csi.palabakosys.room.repository

import com.csi.palabakosys.room.dao.DaoUser
import com.csi.palabakosys.room.entities.EntityUser
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository
@Inject
constructor (
    private val daoUser: DaoUser,
) : BaseRepository<EntityUser>(daoUser) {
    override suspend fun get(id: String?) : EntityUser? {
        if(id == null) return null
        return daoUser.get(id)
    }

    suspend fun getAdmin() : EntityUser? {
        return daoUser.getAdmin()
    }

    suspend fun getByEmail(email: String) : EntityUser? {
        return daoUser.getByEmail(email)
    }

    suspend fun getAll() : List<EntityUser> {
        try {
            return daoUser.getAll()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }
}