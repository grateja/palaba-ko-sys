package com.csi.palabakosys.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.csi.palabakosys.R
import com.csi.palabakosys.model.MachineActivationQueues
import com.csi.palabakosys.model.MachineConnectionStatus
import com.csi.palabakosys.preferences.AppPreferenceRepository
import com.csi.palabakosys.room.entities.EntityActivationRef
import com.csi.palabakosys.room.entities.EntityJobOrderService
import com.csi.palabakosys.room.entities.EntityMachine
import com.csi.palabakosys.room.entities.EntityMachineUsage
import com.csi.palabakosys.room.repository.JobOrderQueuesRepository
import com.csi.palabakosys.room.repository.MachineRepository
import com.csi.palabakosys.room.repository.RemoteRepository
import com.csi.palabakosys.util.toUUID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.Instant
import java.util.Collections
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MachineActivationService : Service() {
    @Inject
    lateinit var remoteRepository: RemoteRepository

    @Inject
    lateinit var machineRepository: MachineRepository

    @Inject
    lateinit var appPreferences: AppPreferenceRepository

    @Inject
    lateinit var queuesRepository: JobOrderQueuesRepository

    companion object {
        const val MACHINE_ACTIVATION = "machine_activation"
//        const val CHECK_PENDING_ACTION = "check_pending"

//        const val MESSAGE_ACTION = 1
//        const val REQUEST_STARTED_ACTION = "request_started"
//        const val REQUEST_SUCCESS_ACTION = "request_finished"
//        const val REQUEST_FAILED_ACTION = "request_failed"
        const val INPUT_INVALID_ACTION = "input_invalid"

        const val PENDING_QUEUES_EXTRA = "pending_queues"
        const val MESSAGE_EXTRA = "message_extra"
//        const val REQUEST_STATUS_EXTRA = "request_status"

        const val CHECK_ONLY_EXTRA = "check_only"
        const val MACHINE_ID_EXTRA = "machine_id"
        const val JO_SERVICE_ID_EXTRA = "jo_service_id"

        private const val NOTIFICATION_ID = 102
        private const val CHANNEL_ID = "IAmSuperAwesome"
    }

    private val queues: MutableList<MachineActivationQueues> by lazy {
        mutableListOf()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        super.onStartCommand(intent, flags, startId)

        startForeground(NOTIFICATION_ID, getNotification())

        val checkOnly = intent?.getBooleanExtra(CHECK_ONLY_EXTRA, false) ?: false

        if(checkOnly) {
            val pending = checkPendingQueues(intent)
            if(pending != null) {
                LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(MACHINE_ACTIVATION).apply {
                    putExtra(PENDING_QUEUES_EXTRA, pending)
                })
            } else {
                stopSelf()
            }
        } else {
            enqueue(intent)
        }

        return START_NOT_STICKY
    }

    private fun enqueue(intent: Intent?) {
        val machineId = intent?.getStringExtra(MACHINE_ID_EXTRA).toUUID()
        val joServiceId = intent?.getStringExtra(JO_SERVICE_ID_EXTRA).toUUID()

        if(machineId != null && joServiceId != null) {
            val queue = MachineActivationQueues(
                machineId, joServiceId, MachineConnectionStatus.CONNECTING, "Machine activation enqueue..."
            )
            LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(MACHINE_ACTIVATION).apply {
                putExtra(PENDING_QUEUES_EXTRA, queue)
            })
            queues.add(queue)
            start(machineId, joServiceId)
        }
    }

    private fun checkPendingQueues(intent: Intent?) : MachineActivationQueues? {
        return intent?.getStringExtra(MACHINE_ID_EXTRA)?.let { machineId ->
            queues.find {
                it.machineId == machineId.toUUID() && it.status == MachineConnectionStatus.CONNECTING
            }
        }
    }

//    private fun updateQueue(machineId: UUID, queue: MachineActivationQueues) {
//        queues.find { it.machineId == machineId }?.let {
//            Collections.replaceAll(queues, it, queue)
//            LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(MACHINE_ACTIVATION).apply {
//                putExtra(PENDING_QUEUES_EXTRA, queue)
//            })
//        }
//    }

    private fun finishQueue(machineId: UUID, status: MachineConnectionStatus, message: String) {
        queues.find {
            it.machineId == machineId
        }?.apply {
            this.status = status
            this.message = message
        }?.let { queue ->
            queues.remove(queue)
            LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(MACHINE_ACTIVATION).apply {
                putExtra(PENDING_QUEUES_EXTRA, queue)
            })
            if(queues.size == 0) {
                stopSelf()
            }
        }
    }

    private val notificationBuilder: NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(this, CHANNEL_ID)
            // 2
            .setContentTitle(this.getString(R.string.app_name))
            .setContentText("Machine activation in progress...")
//            .setSound(null)
//            .setContentIntent(contentIntent)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            // 3
            .setAutoCancel(true)
    }

    private fun getNotification(): Notification {
        notificationManager.createNotificationChannel(createChannel())
        return notificationBuilder.build()
    }

    private fun createChannel() =
        NotificationChannel(
            CHANNEL_ID,
            "CHANNEL_NAME",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "CHANNEL_DESCRIPTION"
            setSound(null, null)
        }

    private val notificationManager by lazy {
        this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(appPreferences.urlSettings.connectionTimeout, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build()
    }

    private fun start(machineId: UUID, jobOrderServiceId: UUID) {

        Thread {
            runBlocking {
//                val machineId = intent?.getStringExtra(MACHINE_ID_EXTRA).toUUID()
//                val jobOrderServiceId = intent?.getStringExtra(JO_SERVICE_ID_EXTRA).toUUID()

                val machine = machineRepository.get(machineId)
                val jobOrderService = queuesRepository.get(jobOrderServiceId)

                if (validate(machine, jobOrderService)) {
                    machineRepository.setWorkerId(machine!!.id, jobOrderService!!.id)

                    if (activate(machine, jobOrderService)) {

                        val machineUsage = EntityMachineUsage(machineId, jobOrderServiceId)
                        val activationRef = EntityActivationRef(
                            Instant.now(),
                            jobOrderService.serviceRef.minutes,
                            jobOrderService.jobOrderId
                        )

                        remoteRepository.activate(
                            activationRef,
                            jobOrderService.id,
                            machine.id,
                            machineUsage
                        )
                    }
                    machineRepository.setWorkerId(machine.id, null)
                }
            }
        }.start()
    }

    private fun sendInvalidInput(message: String) {
        val intent = Intent(INPUT_INVALID_ACTION)
        intent.putExtra(MESSAGE_EXTRA, message)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun validate(machine: EntityMachine?, jobOrderService: EntityJobOrderService?) : Boolean {
        if(machine == null || (machine.ipEnd <= 1 || machine.ipEnd >= 254)) {
            sendInvalidInput("Invalid IP Address")
            return false
        }

        if(jobOrderService == null) {
            sendInvalidInput("Invalid Service")
            return false
        }

        if(jobOrderService.serviceRef.pulse() <= 0) {
            sendInvalidInput("Pulse cannot be 0")
            return false
        }

        if(jobOrderService.quantity <= jobOrderService.used) {
            sendInvalidInput(jobOrderService.serviceName + " already used")
            return false
        }

        return true
    }

    private suspend fun activate(machine: EntityMachine, jobOrderService: EntityJobOrderService) : Boolean {

        val token = "${jobOrderService.id}-${(jobOrderService.quantity - jobOrderService.used)}"
        val pulse = jobOrderService.serviceRef.pulse()

        val ipAddress = appPreferences.ipSettings.toString(machine.ipEnd)
        val url = appPreferences.urlSettings.toString(ipAddress)

        val requestBody = FormBody.Builder()
            .add("pulse", pulse.toString())
            .add("token", token)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .tag(machine.id)
            .build()

        println(url)


        return try {
//            LocalBroadcastManager.getInstance(this).sendBroadcast(
//                Intent(REQUEST_STARTED_ACTION)
//            )

            val response = client.newCall(request).execute()//.body()?.string().toString()
            val body = response.body()?.string().toString()

            if(response.code() == 200) {
                if(body.toInt() < 1) {
//                    LocalBroadcastManager.getInstance(this).sendBroadcast(
//                        Intent(REQUEST_FAILED_ACTION).apply {
//                            putExtra(MESSAGE_EXTRA, "Invalid response from card terminal")
//                            putExtra(MACHINE_ID_EXTRA, machine.id.toString())
//                        }
//                    )
                    finishQueue(machine.id, MachineConnectionStatus.FAILED,"Invalid response from card terminal")
                    return false
                } else {
//                    LocalBroadcastManager.getInstance(this).sendBroadcast(
//                        Intent(REQUEST_SUCCESS_ACTION).apply {
//                            putExtra(MACHINE_ID_EXTRA, machine.id.toString())
//                        }
//                    )
                    finishQueue(machine.id, MachineConnectionStatus.SUCCESS, "${machine.machineName()} Activation success")
                    true
                }
            } else {
//                LocalBroadcastManager.getInstance(this).sendBroadcast(
//                    Intent(REQUEST_FAILED_ACTION).apply {
//                        putExtra(MESSAGE_EXTRA, "Invalid response from card terminal")
//                        putExtra(MACHINE_ID_EXTRA, machine.id.toString())
//                    }
//                )
                finishQueue(machine.id, MachineConnectionStatus.FAILED,"Invalid response from card terminal")
                return false
            }
        } catch(e: NumberFormatException) {
//            sendInvalidInput("Request success but got an invalid response from the terminal")
//            LocalBroadcastManager.getInstance(this).sendBroadcast(
//                Intent(REQUEST_FAILED_ACTION).apply {
//                    putExtra(MESSAGE_EXTRA, "Request success but got an invalid response from the terminal")
//                    putExtra(MACHINE_ID_EXTRA, machine.id.toString())
//                }
//            )
//            sendRequestStatus(REQUEST_FAILED_ACTION, machine.id.toString(), -1)
            finishQueue(machine.id, MachineConnectionStatus.FAILED, "Request success but got an invalid response from the terminal")
            return false
        } catch (e: Exception) {
//            LocalBroadcastManager.getInstance(this).sendBroadcast(
//                Intent(REQUEST_FAILED_ACTION).apply {
//                    putExtra(MESSAGE_EXTRA, e.message)
//                    putExtra(MACHINE_ID_EXTRA, machine.id.toString())
//                }
//            )
            finishQueue(machine.id, MachineConnectionStatus.FAILED, e.message.toString())
            e.printStackTrace()
            false
        }
    }

//    @Parcelize
//    data class ActivationResult(
//        val machineId: UUID,
//        val joServiceId: UUID,
//        val statusCode: Int,
//        val message: String,
//    ) : Parcelable
}