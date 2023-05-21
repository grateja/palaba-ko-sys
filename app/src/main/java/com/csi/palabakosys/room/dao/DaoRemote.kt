package com.csi.palabakosys.room.dao

import androidx.room.*
import com.csi.palabakosys.room.entities.EntityActivationRef
import com.csi.palabakosys.room.entities.EntityMachineUsage
import com.csi.palabakosys.room.entities.EntityRunningMachine
import java.time.Instant
import java.util.*

@Dao
interface DaoRemote {
    @Query("UPDATE machines SET time_activated = :timeActivated, total_minutes = :totalMinutes, service_activation_id = null, jo_service_id = :jobOrderServiceId WHERE id = :machineId")
    fun startMachine(machineId: UUID, jobOrderServiceId: UUID, timeActivated: Instant?, totalMinutes: Int?)

    @Insert
    fun insertMachineUsage(machineUsage: EntityMachineUsage)

    @Transaction
    suspend fun activate(activationRef: EntityActivationRef, jobOrderServiceId: UUID, machineId: UUID, machineUsage: EntityMachineUsage) {
        startMachine(machineId, jobOrderServiceId, activationRef.timeActivated, activationRef.totalMinutes)
        insertMachineUsage(machineUsage)
    }

    @Query("UPDATE machines SET service_activation_id = :jobOrderServiceId WHERE id = :machineId")
    fun setServiceActivationId(machineId: UUID, jobOrderServiceId: UUID?)

    @Query("UPDATE job_order_services SET used = used + 1 WHERE id = :jobOrderServiceId")
    fun preUseService(jobOrderServiceId: UUID)

    @Query("UPDATE job_order_services SET used = used - 1 WHERE id = :jobOrderServiceId")
    fun revertUsage(jobOrderServiceId: UUID)

    @Transaction
    suspend fun preActivate(machineId: UUID, jobOrderServiceId: UUID) {
        setServiceActivationId(machineId, jobOrderServiceId)
        preUseService(jobOrderServiceId)
    }

    @Transaction
    suspend fun cancelActivation(machineId: UUID, jobOrderServiceId: UUID) {
        setServiceActivationId(machineId, null)
        revertUsage(jobOrderServiceId)
    }

    @Transaction
    @Query("SELECT * FROM machines WHERE id = :machineId LIMIT 1")
    suspend fun getActiveMachine(machineId: UUID?) : EntityRunningMachine?
}