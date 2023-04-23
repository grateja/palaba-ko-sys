package com.csi.palabakosys.room.dao

import androidx.room.*
import com.csi.palabakosys.room.entities.EntityActivationRef
import com.csi.palabakosys.room.entities.EntityJobOrderService
import com.csi.palabakosys.room.entities.EntityMachine
import com.csi.palabakosys.room.entities.EntityMachineUsage
import java.time.Instant
import java.util.*

@Dao
interface DaoRemote {
    @Query("UPDATE machines SET time_activated = :timeActivated, total_minutes = :totalMinutes, worker_id = null WHERE id = :machineId")
    fun startMachine(machineId: String?, timeActivated: Instant?, totalMinutes: Int?)

    @Query("UPDATE job_order_services SET used = used + 1 WHERE id = :jobOrderServiceId")
    fun useService(jobOrderServiceId: String?)

    @Insert
    fun insertMachineUsage(machineUsage: EntityMachineUsage)

    @Transaction
    suspend fun activate(activationRef: EntityActivationRef, jobOrderServiceId: String?, machineId: String?, machineUsage: EntityMachineUsage) {
        startMachine(machineId, activationRef.timeActivated, activationRef.totalMinutes)
        useService(jobOrderServiceId)
        insertMachineUsage(machineUsage)
    }
}