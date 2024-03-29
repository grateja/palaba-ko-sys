package com.csi.palabakosys.room.db.seeder

import com.csi.palabakosys.model.EnumActionPermission
import com.csi.palabakosys.model.Role
import com.csi.palabakosys.room.dao.DaoUser
import com.csi.palabakosys.room.entities.EntityUser

class UsersSeeder(private val daoUser: DaoUser) : EntitySeederImpl<EntityUser>(daoUser) {
    override fun items(): List<EntityUser> {
        return listOf(
//            EntityUser(Role.DEVELOPER, "Developer", "developer@dev.com", "123",
//                listOf(EnumActionPermission.ALL), "",
//                arrayListOf(4)
//            ),
            EntityUser(Role.ADMIN, "Admin", "admin@dev.com", "123",
                listOf(
                    EnumActionPermission.ALL,
                ),"",
                arrayListOf(4)
            ),
            EntityUser(Role.STAFF, "Staff 1", "staff1@dev.com", "123",
                listOf(EnumActionPermission.BASIC),"",
                arrayListOf(4)
            ),
            EntityUser(Role.STAFF, "Staff 2", "staff2@dev.com", "123",
                listOf(EnumActionPermission.BASIC),"",
                arrayListOf(4)
            ),
        )
    }
}