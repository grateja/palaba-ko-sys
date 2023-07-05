package com.csi.palabakosys.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.csi.palabakosys.app.machines.MachineListItem
import com.csi.palabakosys.model.EnumMachineType
import com.csi.palabakosys.room.entities.EntityMachine
import java.util.*

@Dao
abstract class DaoMachine : BaseDao<EntityMachine> {
    @Query("SELECT * FROM machines WHERE machine_type = :machineType ORDER BY stack_order")
    abstract suspend fun getAll(machineType: EnumMachineType): List<EntityMachine>

    @Query("SELECT * FROM machines WHERE id = :id")
    abstract suspend fun get(id: UUID) : EntityMachine?

    @Query("SELECT stack_order FROM machines WHERE machine_type = :machineType ORDER BY stack_order DESC")
    abstract suspend fun getLastStackOrder(machineType: EnumMachineType) : Int?

//    @Query("UPDATE machines SET worker_id = :workerId WHERE id = :machineId")
//    abstract suspend fun setWorkerId(machineId: UUID, workerId: UUID?)

    @Query("SELECT * FROM machines ORDER BY stack_order")
    abstract fun getAllAsLiveData(): LiveData<List<EntityMachine>>

    @Query("SELECT * FROM machines ORDER BY stack_order")
    abstract fun getListAllAsLiveData(): LiveData<List<MachineListItem>>

    @Query("SELECT * FROM machines WHERE id = :id")
    abstract fun getMachineLiveData(id: UUID?) : LiveData<EntityMachine?>
}