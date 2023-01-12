package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.csi.palabakosys.model.Role

@Entity(tableName = "users")
class EntityUser(
    @ColumnInfo(name = "role")
    var role: Role
) : BaseEntity() {
    @ColumnInfo(name = "name")
    var name: String? = null

    @ColumnInfo(name = "contact_number")
    var contactNumber: String? = null

    @ColumnInfo(name = "email")
    var email: String? = null

    @ColumnInfo(name = "password")
    var password: String? = null
}
