package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.csi.palabakosys.model.ActionPermissionEnum
import com.csi.palabakosys.model.Role

@Entity(tableName = "users")
class EntityUser(
    @ColumnInfo(name = "role")
    var role: Role,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "password")
    var password: String,

    @ColumnInfo(name = "permissions")
    var permissions: List<ActionPermissionEnum>,

    @ColumnInfo(name = "contact_number")
    var contactNumber: String? = null,
) : BaseEntity()
