package com.csi.palabakosys.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.csi.palabakosys.room.entities.EntityMachineUsage
import java.util.UUID

@Dao
abstract class DaoMachineUsage : BaseDao<EntityMachineUsage> {
    @Query("SELECT * FROM machine_usages WHERE id = :id")
    abstract suspend fun get(id: UUID) : EntityMachineUsage?

    @Query("SELECT * FROM machine_usages WHERE machine_id = :machineId")
    abstract suspend fun getAll(machineId: UUID) : List<EntityMachineUsage>
}