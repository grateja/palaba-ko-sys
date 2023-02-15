package com.csi.palabakosys.room.dao

import androidx.room.*
import com.csi.palabakosys.room.entities.EntityJobOrderService
import com.csi.palabakosys.room.entities.EntityMachine
import com.csi.palabakosys.room.entities.EntityMachineUsage
import java.time.Instant
import java.util.*

@Dao
interface DaoRemote {
//    @Query("UPDATE machines SET time_activated = :timeActivated, total_minutes = :totalMinutes, customer_name = :customerName WHERE id = :machineId")
//    fun startMachine(machineId: UUID?, timeActivated: Instant?, totalMinutes: Int, customerName: String?)
//
//    @Update
//    fun useService(jobOrderService: EntityJobOrderService)
//
//    @Insert
//    fun insertMachineUsage(machineUsage: EntityMachineUsage)
//
//    @Transaction
//    suspend fun activate(machine: EntityMachine, jobOrderService: EntityJobOrderService, machineUsage: EntityMachineUsage) {
//        startMachine(machine.id, machine.timeActivated, machine.totalMinutes, machine.customerName)
//        useService(jobOrderService)
//        insertMachineUsage(machineUsage)
//    }
}