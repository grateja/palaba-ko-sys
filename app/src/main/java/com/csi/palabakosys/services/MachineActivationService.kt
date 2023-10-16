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
import com.csi.palabakosys.room.entities.*
import com.csi.palabakosys.room.repository.CustomerRepository
import com.csi.palabakosys.room.repository.JobOrderQueuesRepository
import com.csi.palabakosys.room.repository.MachineRepository
import com.csi.palabakosys.room.repository.RemoteRepository
import com.csi.palabakosys.util.Constants
import com.csi.palabakosys.util.MachineActivationBus
import com.csi.palabakosys.util.toUUID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Cache
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.Instant
import java.util.*
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

    @Inject
    lateinit var customerRepository: CustomerRepository

    @Inject
    lateinit var queues: MachineActivationBus

    companion object {
        const val MACHINE_ACTIVATION = "machine_activation"
        const val MACHINE_ACTIVATION_READY = "machine_activation_ready"
        const val INPUT_INVALID_ACTION = "input_invalid"
        const val DATABASE_INCONSISTENCIES_ACTION = "inconsistent_db"

        const val PENDING_QUEUES_EXTRA = "pending_queues"
        const val MESSAGE_EXTRA = "message_extra"

        const val CHECK_ONLY_EXTRA = "check_only"
        const val JO_SERVICE_ID_EXTRA = "jo_service_id"
        const val CUSTOMER_ID_EXTRA = "customer_id"

        private const val NOTIFICATION_ID = 102
        private const val CHANNEL_ID = "IAmSuperAwesome"
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        startForeground(NOTIFICATION_ID, getNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        super.onStartCommand(intent, flags, startId)

        val checkOnly = intent?.getBooleanExtra(CHECK_ONLY_EXTRA, false) ?: false

        if(checkOnly) {
            val pending = checkPendingQueues(intent)
            if(pending != null) {
                LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(MACHINE_ACTIVATION).apply {
                    putExtra(PENDING_QUEUES_EXTRA, pending)
                })
            } else {
                checkInconsistencies(intent)
            }
        } else {
            enqueue(intent)
        }

        return START_NOT_STICKY
    }

    private fun checkInconsistencies(intent: Intent?) {
        val context = this
        Thread {
            runBlocking {
                val machineId = intent?.getStringExtra(Constants.MACHINE_ID_EXTRA).toUUID()
                val machine = machineRepository.get(machineId)
                if(machine?.serviceActivationId != null) {
                    // something is wrong
                    sendInvalidInput("Inconsistencies with the database detected")
                    LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(DATABASE_INCONSISTENCIES_ACTION).apply {
                        putExtra(Constants.MACHINE_ID_EXTRA, machine.id.toString())
                        putExtra(JO_SERVICE_ID_EXTRA, machine.serviceActivationId.toString())
                    })
                } else {
                    // all good
                    // send empty queue
                    safeStop()
                    LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(MACHINE_ACTIVATION_READY))
                }
            }
        }.start()
    }

    private fun enqueue(intent: Intent?) {
        val machineId = intent?.getStringExtra(Constants.MACHINE_ID_EXTRA).toUUID()
        val joServiceId = intent?.getStringExtra(JO_SERVICE_ID_EXTRA).toUUID()
        val customerId = intent?.getStringExtra(CUSTOMER_ID_EXTRA).toUUID()

        if(machineId != null && joServiceId != null && customerId != null) {
            start(machineId, joServiceId, customerId)
        } else {
            sendInvalidInput("Inconsistent data detected")
        }
    }

    private fun checkPendingQueues(intent: Intent?) : MachineActivationQueues? {
        return intent?.getStringExtra(Constants.MACHINE_ID_EXTRA)?.let { machineId ->
            queues.get(machineId.toUUID())
        }
    }

    private fun finishQueue(machineId: UUID, status: MachineConnectionStatus, message: String) {
        queues.get(machineId)?.apply {
            this.status = status
            this.message = message
        }?.let { queue ->
            LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(MACHINE_ACTIVATION).apply {
                putExtra(PENDING_QUEUES_EXTRA, queue)
            })
            Thread {
                runBlocking {
                    delay(1000L)
                    queues.remove(queue.machineId)
                    safeStop()
                }
            }.start()
        }
    }

    private fun safeStop() {
        if(queues.size() == 0) {
            stopSelf()
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
            .cache(null)
            .connectTimeout(appPreferences.urlSettings.connectionTimeout, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build()
    }

    private fun start(machineId: UUID, jobOrderServiceId: UUID, customerId: UUID) {
        val context = this

        Thread {
            runBlocking {
                val machine = machineRepository.get(machineId)
                val jobOrderService = queuesRepository.get(jobOrderServiceId)
                val customer = customerRepository.get(customerId)

                if (validate(machine, jobOrderService, customer)) {

                    val queue = MachineActivationQueues(
                        machine!!.id,
                        jobOrderService!!.id,
                        customer!!.id,
                        customer.name!!,
                        jobOrderService.serviceName,
                        machine.machineName(),
                        MachineConnectionStatus.CONNECTING,
                        "Connecting to ${machine.machineName()}..."
                    )
                    LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(MACHINE_ACTIVATION).apply {
                        putExtra(PENDING_QUEUES_EXTRA, queue)
                    })
                    queues.add(queue)

                    println("add item")
                    println(queues.size())

                    remoteRepository.preActivate(machine.id, jobOrderService.id)

                    if (activate(machine, jobOrderService)) {

                        val machineUsage = EntityMachineUsage(machineId, jobOrderServiceId, customerId)
                        val activationRef = EntityActivationRef(
                            Instant.now(),
                            jobOrderService.serviceRef.minutes,
                            jobOrderService.jobOrderId,
                            customerId
                        )

                        remoteRepository.activate(
                            activationRef,
                            jobOrderService.id,
                            machine.id,
                            machineUsage
                        )
                    } else {
                        remoteRepository.revertActivation(machine.id, jobOrderService.id)
                    }
                }
            }
        }.start()
    }

    private fun sendInvalidInput(message: String) {
        val intent = Intent(INPUT_INVALID_ACTION)
        intent.putExtra(MESSAGE_EXTRA, message)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun validate(machine: EntityMachine?, jobOrderService: EntityJobOrderService?, customer: EntityCustomer?) : Boolean {
        if(machine == null || (machine.ipEnd <= 1 || machine.ipEnd >= 254)) {
            sendInvalidInput("Invalid IP Address")
            return false
        }

        if((machine.activationRef?.remainingTime() ?: 0) > 0) {
            sendInvalidInput("Machine is already running")
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

        if(customer?.name == null) {
            sendInvalidInput("Customer must have a name")
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
            val response = client.newCall(request).execute()//.body()?.string().toString()
            val body = response.body()?.string().toString()

            if(response.code() == 200) {
                if(body.toInt() < 1) {
                    finishQueue(machine.id, MachineConnectionStatus.FAILED,"Invalid response from card terminal")
                    return false
                } else {
                    finishQueue(machine.id, MachineConnectionStatus.SUCCESS, "${machine.machineName()} Activation success")
                    true
                }
            } else {
                finishQueue(machine.id, MachineConnectionStatus.FAILED,"Invalid response from card terminal")
                return false
            }
        } catch(e: NumberFormatException) {
            finishQueue(machine.id, MachineConnectionStatus.FAILED, "Request success but got an invalid response from the terminal")
            return false
        } catch (e: Exception) {
            finishQueue(machine.id, MachineConnectionStatus.FAILED, e.message.toString())
            e.printStackTrace()
            false
        }
    }
}