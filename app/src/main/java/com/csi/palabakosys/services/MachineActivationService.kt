package com.csi.palabakosys.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.csi.palabakosys.R
import com.csi.palabakosys.app.MainActivity
import com.csi.palabakosys.model.EnumMachineType
import com.csi.palabakosys.room.repository.MachineRepository
import com.csi.palabakosys.room.repository.RemoteRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MachineActivationService : Service() {
    @Inject
    lateinit var remoteRepository: RemoteRepository

    @Inject
    lateinit var machineRepository: MachineRepository

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        super.onStartCommand(intent, flags, startId)

        startForeground(NOTIFICATION_ID, getNotification()) // Display a persistent notification

//        notificationManager.notify(NOTIFICATION_ID, getNotification())

        val context = this

        runBlocking {
            println("get machines")
            val machineType = EnumMachineType.fromId(intent?.getIntExtra("machineType", 4)) ?: EnumMachineType.TITAN_DRYER
            machineRepository.getAll(machineType).let {
                it.forEach {
//                    println("machine")
//                    println(it.machineName())
                    println(it.machineName())
                    val i = Intent("TestService").apply {
                        putExtra("data", it.machineName())
                    }
                    LocalBroadcastManager.getInstance(context).sendBroadcast(i)
                }
            }
        }

        Thread(Runnable {
            var x = 20
            while(x-- >= 0) {
                runBlocking {
                    delay(3000)
                    println("keme")
                }
            }
            stopSelf()
        }).start()
//        stopSelf()
        return START_STICKY
    }

//    private fun buildNotification(): Notification {
//        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
//        builder.setContentTitle(getString(R.string.app_name))
//            .setContentText("Running")
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .setPriority(NotificationCompat.PRIORITY_LOW)
//            .setOngoing(true)
//            .setOnlyAlertOnce(true)
//            .setAutoCancel(true)
//
////        return notificationManager.createNotificationChannel(createChannel())
//
//        return builder
//    }

//    private val contentIntent by lazy {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            PendingIntent.getActivity(
//                this,
//                0,
//                Intent(this, MainActivity::class.java),
//                PendingIntent.FLAG_MUTABLE
//            )
//        } else {
//            PendingIntent.getActivity(
//                this,
//                0,
//                Intent(this, MainActivity::class.java),
//                PendingIntent.FLAG_UPDATE_CURRENT
//            )
//        }
//    }

    private val notificationBuilder: NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(this, CHANNEL_ID)
            // 2
            .setContentTitle(this.getString(R.string.app_name))
            .setContentText("KEME Lang")
//            .setSound(null)
//            .setContentIntent(contentIntent)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            // 3
            .setAutoCancel(true)
    }

    private fun getNotification(): Notification {
        // 1
        notificationManager.createNotificationChannel(createChannel())

        // 2
        return notificationBuilder.build()
    }

    private fun createChannel() =
        // 1
        NotificationChannel(
            CHANNEL_ID,
            "CHANNEL_NAME",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {

            // 2
            description = "CHANNEL_DESCRIPTION"
            setSound(null, null)
        }

    private val notificationManager by lazy {
        this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    companion object {
        private const val NOTIFICATION_ID = 102
        private const val CHANNEL_ID = "IAmSuperAwesome"
    }
}