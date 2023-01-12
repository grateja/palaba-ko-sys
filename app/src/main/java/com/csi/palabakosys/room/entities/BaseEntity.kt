package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.*

abstract class BaseEntity () {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: UUID = UUID.randomUUID()

    @ColumnInfo(name = "created_at")
    var createdAt: Instant = Instant.now()

    @ColumnInfo(name = "updated_at")
    var updatedAt: Instant = Instant.now()

    @ColumnInfo(name = "deleted_at")
    var deletedAt: Instant? = null

    @ColumnInfo(name = "sync")
    var sync: Instant? = null
}
