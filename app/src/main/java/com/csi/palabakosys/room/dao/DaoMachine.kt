package com.csi.palabakosys.room.dao

import androidx.room.*
import com.csi.palabakosys.room.entities.EntityMachine

@Dao
abstract class DaoMachine : BaseDao<EntityMachine> {
    @Query("SELECT * FROM machines ORDER BY stack_order")
    abstract suspend fun getAll() : List<EntityMachine>

    @Query("SELECT * FROM machines WHERE id = :id")
    abstract suspend fun get(id: String) : EntityMachine?

    @Query("SELECT stack_order FROM machines WHERE machine_type = :machineType ORDER BY stack_order DESC")
    abstract suspend fun getLastStackOrder(machineType: String) : Int?

    @Query("UPDATE machines SET worker_id = :workerId WHERE id = :machineId")
    abstract suspend fun setWorkerId(machineId: String, workerId: String)
}