package com.csi.palabakosys.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.csi.palabakosys.preferences.AppPreferenceRepository
import com.csi.palabakosys.room.entities.EntityMachine
import com.csi.palabakosys.room.repository.JobOrderQueuesRepository
import com.csi.palabakosys.room.repository.MachineRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import okhttp3.*
import java.util.concurrent.TimeUnit

@HiltWorker
class RemoteWorker
@AssistedInject
constructor (
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val machineRepository: MachineRepository,
    private val jobOrderQueuesRepository: JobOrderQueuesRepository,
    private val appPreferences: AppPreferenceRepository
) : CoroutineWorker(context, workerParams) {
    companion object {
//        const val PULSE = "pulse"
        const val MACHINE_ID = "machineId"
//        const val MACHINE_IP_END = "machineIpEnd"
        const val MESSAGE = "message"
        const val JOB_ORDER_SERVICE_ID = "jobOrderServiceId"
    }
    private var message = "Connecting..."
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(false)
        .build()

    override suspend fun doWork(): Result {
        println("Work started")
//        val pulse = inputData.getInt(PULSE, 0)
        val machineId = inputData.getString(MACHINE_ID)
//        val machineIpEnd = inputData.getInt(MACHINE_IP_END, 0)
        val jobOrderServiceId = inputData.getString(JOB_ORDER_SERVICE_ID)

        val machine = machineRepository.get(machineId)
        if(machine == null) {
            message = "Invalid machine"
            println(message)
            return Result.failure(workDataOf(MESSAGE to message))
        }

//        if(machine.workerId == id) {
//            message = "Duplicate work detected"
//            println(message)
//            return Result.failure(workDataOf(MESSAGE to message))
//        }

        val service = jobOrderQueuesRepository.get(jobOrderServiceId)
        if(service == null) {
            message = "Invalid Service"
            println(message)
            return Result.failure(workDataOf(MESSAGE to message))
        }

        val token = "${service.id}-${(service.quantity - service.used)}"

        return if(activate(machineId, machine.ipEnd, service.serviceRef.pulse(), token)) {
            println("Do Work Request success")
            println(message)
            Result.success(workDataOf(MESSAGE to message))
        } else {
            println("Do Work Request failed")
            println(message)
            machineRepository.setWorkerId(machineId.toString(), null)
            Result.failure(workDataOf(MESSAGE to message))
        }
    }

    private suspend fun activate(machineId: String?, machineIpEnd: Int, pulse: Int, token: String) : Boolean {
        var invalid = false
        if(machineId == null || (machineIpEnd <= 1 || machineIpEnd >= 254)) {
            println("machine ip end")
            println(machineIpEnd)

            println("machine id")
            println(machineId)
            message = "Invalid IP Address"
            return false
        }

//        if(token.isNullOrBlank()) {
//            message = "Token cannot be null"
//            invalid = true
//        } else

        if(pulse <= 0) {
            message = "Pulse cannot be '0'"
            invalid = true
        }

        if(invalid) {
            println("message")
            println(message)
            return false
        }

        val ipAddress = appPreferences.ipSettings.toString(machineIpEnd)
        val url = "http://${ipAddress}/activate?pulse=$pulse&token=${token}"

        val request = Request.Builder()
            .url(url)
            .build()

        println(url)

        machineRepository.setWorkerId(machineId, id.toString())

        return try {
            message = client.newCall(request).execute().body().toString()
            println("Request success")
            println(message)
            true
        } catch (e: Exception) {
            message = e.message.toString()
            println("Request failed")
            println(message)
//            e.printStackTrace()
            false
        }
    }
}