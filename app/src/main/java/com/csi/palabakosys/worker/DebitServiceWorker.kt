package com.csi.palabakosys.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.csi.palabakosys.room.entities.EntityJobOrderService
import com.csi.palabakosys.room.entities.EntityMachine
import com.csi.palabakosys.room.entities.EntityMachineUsage
import com.csi.palabakosys.room.repository.RemoteRepository
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DebitServiceWorker
@AssistedInject
constructor (
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val remoteRepository: RemoteRepository
) : CoroutineWorker(context, workerParams) {
    var message = ""
    override suspend fun doWork(): Result {
        val gson = Gson()
        val machine = gson.fromJson(inputData.getString("machine"), EntityMachine::class.java)
        val joService = gson.fromJson(inputData.getString("joService"), EntityJobOrderService::class.java)
        val machineUsage = gson.fromJson(inputData.getString("machineUsage"), EntityMachineUsage::class.java)

        return if(update(machine, joService, machineUsage)) {
            Result.success(workDataOf("message" to message))
        } else {
            Result.failure(workDataOf("message" to message))
        }
    }
    suspend fun update(machine: EntityMachine, joService: EntityJobOrderService, machineUsage: EntityMachineUsage) : Boolean {
        return try {
            println("id from debit service")
//            machine.workerId = id
            remoteRepository.activate(machine, joService, machineUsage)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}