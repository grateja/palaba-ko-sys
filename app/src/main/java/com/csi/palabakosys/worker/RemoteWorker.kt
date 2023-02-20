package com.csi.palabakosys.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.csi.palabakosys.preferences.AppPreferenceRepository
import com.csi.palabakosys.room.entities.EntityMachine
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
    private val appPreferences: AppPreferenceRepository
) : CoroutineWorker(context, workerParams) {
    companion object {
        const val TOKEN = "token"
        const val PULSE = "pulse"
        const val MACHINE_ID = "machineId"
        const val MESSAGE = "message"
    }
    private var message = "Connecting..."
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(false)
        .build()

    override suspend fun doWork(): Result {
        val token = inputData.getString(TOKEN)
        val pulse = inputData.getInt(PULSE, 0)
        val machineId = inputData.getString(MACHINE_ID)

        val machine = machineRepository.get(machineId)

        return if(activate(machine, pulse, token)) {
            println("Do Work Request success")
            Result.success(workDataOf(MESSAGE to message))
        } else {
            Result.failure(workDataOf(MESSAGE to message))
        }
    }

    private suspend fun activate(machine: EntityMachine?, pulse: Int, token: String?) : Boolean {
        var invalid = false
        if(machine == null) {
            message = "Invalid IP Address"
            return false
        }

        if(token.isNullOrBlank()) {
            message = "Token cannot be null"
            invalid = true
        } else if(pulse <= 0) {
            message = "Pulse cannot be '0'"
            invalid = true
        }

        if(invalid) {
            println("message")
            println(message)
            return false
        }

        val ipAddress = appPreferences.ipSettings.toString(machine.ipEnd)
        val url = "http://${ipAddress}/activate?pulse=$pulse&token=$token"

        val request = Request.Builder()
            .url(url)
            .build()

        println(url)

        machineRepository.setWorkerId(machine, id.toString())

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