package com.csi.palabakosys.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.csi.palabakosys.R
import com.csi.palabakosys.preferences.AppPreferenceRepository
import com.csi.palabakosys.preferences.PrinterSettings
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class PrinterService : Service() {
    companion object {
        private const val NOTIFICATION_ID = 103
        private const val CHANNEL_ID = "printer_settings_channel_id"
        private const val CHANNEL_NAME = "Printer Settings"
        private const val CHANNEL_DESCRIPTION = "Channel for printer settings notifications"
        const val PAYLOAD_TEXT = "payload"
        const val CUSTOM_SETTINGS = "customSettings"
        const val PRINT_STARTED_ACTION = "print_started_action"
        const val PRINT_FINISHED_ACTION = "print_finished_action"
        const val CANCEL_PRINT_ACTION = "print_finished_action"
        const val PRINT_ERROR_ACTION = "print_error"
        const val MESSAGE = "message"
    }

    @Inject
    lateinit var appPreferenceRepository: AppPreferenceRepository

    var printer: EscPosPrinter? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        startForeground(NOTIFICATION_ID, getNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val action = intent?.action
        if(action == CANCEL_PRINT_ACTION) {
            stopSelf()
        }

        val settings = intent?.getParcelableExtra<PrinterSettings>(CUSTOM_SETTINGS)
        intent?.getStringExtra(PAYLOAD_TEXT)?.let {
            startPrint(it, settings)
        }

        return START_NOT_STICKY
    }

    private val notificationManager by lazy {
        this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val notificationBuilder: NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(this.getString(R.string.app_name))
            .setContentText("Print started")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
    }

    private fun getNotification(): Notification {
        notificationManager.createNotificationChannel(createChannel())
        return notificationBuilder.build()
    }

    private fun createChannel() =
        NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = CHANNEL_DESCRIPTION
            setSound(null, null)
        }

    private val bluetoothManager: BluetoothManager by lazy {
        getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        bluetoothManager.adapter
    }

    private fun startPrint(text: String, customSettings: PrinterSettings?) {
        Thread(Runnable {
            try {
                LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(PRINT_STARTED_ACTION))

                val settings = customSettings ?: appPreferenceRepository.printerSettings()
                val device = bluetoothAdapter.getRemoteDevice(settings.address)
                val connection = BluetoothConnection(device)

                println("create printer")
                printer = EscPosPrinter(connection, settings.dpi, settings.width, settings.character)

                println("start print")
                printer?.printFormattedTextAndCut(text)

                println("disconnect")
                printer?.disconnectPrinter()

            } catch (e: Exception) {
                e.printStackTrace()
                val intent = Intent(PRINT_ERROR_ACTION).apply {
                    putExtra(MESSAGE, e.message)
                }
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            } finally {
                printer?.disconnectPrinter()
                LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(PRINT_FINISHED_ACTION))
                safeStop()
            }
        }).start()
    }

    private fun safeStop() {
        Thread {
            runBlocking {
                delay(3000L)
                stopSelf()
            }
        }.start()
    }
}