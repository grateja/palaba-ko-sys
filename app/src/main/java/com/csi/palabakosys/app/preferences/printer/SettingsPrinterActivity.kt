package com.csi.palabakosys.app.preferences.printer

import android.Manifest
import android.bluetooth.*
import android.content.*
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.databinding.ActivitySettingsPrinterBinding
import com.csi.palabakosys.util.ActivityContractsLauncher
import com.csi.palabakosys.util.ActivityLauncher
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.tasks.Task
import java.io.OutputStream
import java.util.*

class SettingsPrinterActivity : AppCompatActivity() {
    private val bluetoothLauncher = ActivityLauncher(this)
    private lateinit var binding: ActivitySettingsPrinterBinding
    private val permissionLauncher = ActivityContractsLauncher(this)
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter

    private val devicesAdapter = Adapter<BluetoothDevice>(R.layout.recycler_item_bluetooth_device)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings_printer)
        binding.recyclerBluetoothDevices.adapter = devicesAdapter
        devicesAdapter.onItemClick = {
//            printTextToPrinter(it.address, binding.textTestPrint.text.toString())
            printWithEscPos(it.address, binding.textTestPrint.text.toString())
        }


        bluetoothManager = this.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        binding.buttonTurnOnBluetooth.setOnClickListener {
            if(enableBluetooth()) {
                startDiscovery()
                getBondedDevices()
            }
        }

        binding.buttonTurnOnLocationServices.setOnClickListener {
            enableLocation()
        }

        permissionLauncher.onOk = { result ->
            result.entries.forEach {
                println("permission ${it.key}=${it.value}")
            }
        }
        permissionLauncher.onCancel = {
            println("Canceled")
        }
        checkPermissions()

        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
            filter.addAction(BluetoothDevice.ACTION_FOUND)
            filter.addAction(LocationManager.MODE_CHANGED_ACTION)

        registerReceiver(
            bluetoothStateChangeBroadcastReceiver,
            filter
        )
    }

    private fun isGPSEnabled() : Boolean {
        val locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun enableBluetooth() : Boolean {
        return if(!bluetoothAdapter.isEnabled) {
            val intentBluetooth = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            bluetoothLauncher.launch(intentBluetooth)
            false
        } else {
            true
        }
    }

    private fun getBondedDevices() {
        if(enableBluetooth()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_ADMIN
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                println("permission not granted")
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            bluetoothAdapter.bondedDevices.let {
                devicesAdapter.setData(it.toMutableList())
            }
        }
    }

    private fun startDiscovery() : Boolean {
        println("start discovery")
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.BLUETOOTH_ADMIN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            checkPermissions()
            println("Cannot search nearby devices")
            println("Permission not granted")
            return false
        }
        bluetoothAdapter.startDiscovery()
        return true
    }

    private val bluetoothStateChangeBroadcastReceiver = object: BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.R)
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action) {
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                    binding.buttonTurnOnBluetooth.text = if(state == BluetoothAdapter.STATE_ON) {
                        "You're good to go"
                    } else { "Turn on bluetooth" }
                    if(state == BluetoothAdapter.STATE_ON) {
                        startDiscovery()
                    }
                }
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    println("device found")
                    println(device?.address)
//                    val deviceName = device?.name
                    val deviceHardwareAddress = device?.address // MAC address
                    device?.let {
                        devicesAdapter.add(it)
                    }
                }
                LocationManager.MODE_CHANGED_ACTION -> {
                    val locationState = intent.getBooleanExtra(LocationManager.EXTRA_LOCATION_ENABLED, false)
                    println("Location settings changed")
                    println(locationState)
                }
            }
        }
    }

    private fun checkPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH_ADMIN,
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
        }
        permissionLauncher.launch(permissions.toTypedArray())
    }

    private fun enableLocation() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val locationRequest = LocationRequest.Builder(10000L)
                .build()
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)

            val client: SettingsClient = LocationServices.getSettingsClient(this)
            val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

            task.addOnSuccessListener { locationSettingsResponse ->
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
                val state = locationSettingsResponse.locationSettingsStates

                val label =
                    "GPS >> (Present: ${state?.isGpsPresent}  | Usable: ${state?.isGpsUsable} ) \n\n" +
                            "Network >> ( Present: ${state?.isNetworkLocationPresent} | Usable: ${state?.isNetworkLocationUsable} ) \n\n" +
                            "Location >> ( Present: ${state?.isLocationPresent} | Usable: ${state?.isLocationUsable} )"
                println(label)
            }

            task.addOnFailureListener { exception ->
                exception.printStackTrace()
                try {
                    val response = task.getResult(ApiException::class.java)
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                } catch (exception: ApiException) {
                    when (exception.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            try {
                                // Cast to a resolvable exception.
                                val resolvable: ResolvableApiException = exception as ResolvableApiException
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                    this, Priority.PRIORITY_HIGH_ACCURACY
                                )
                            } catch (e: IntentSender.SendIntentException) {
                                // Ignore the error.
                            } catch (e: ClassCastException) {
                                // Ignore, should be an impossible error.
                            }
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.

                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                        }
                    }
                }
//            if (exception is ResolvableApiException) {
//                // Location settings are not satisfied, but this can be fixed
//                // by showing the user a dialog.
//                try {
//                    // Show the dialog by calling startResolutionForResult(),
//                    // and check the result in onActivityResult().
//                    exception.startResolutionForResult(
//                        this@MainActivity,
//                        100
//                    )
//                } catch (sendEx: IntentSender.SendIntentException) {
//                    // Ignore the error.
//                    textView.text = sendEx.message.toString()
//                }
//            }
            }
//        } else {
//            println("version not suppoerted ${BuildConfig.VERSION_CODE} ${BuildConfig.VERSION_NAME}")
//        }
    }

    private lateinit var printer: EscPosPrinter
    private fun printWithEscPos(address: String, text: String) {
        val thread = Thread(Runnable {
            try {
                val device = bluetoothAdapter.getRemoteDevice(address)
                val bluetoothConnection = BluetoothConnection(device)
                printer = EscPosPrinter(bluetoothConnection, 203, 58f, 32)
                printer.printFormattedTextAndCut(text)
                printer.disconnectPrinter()
            } catch (e: Exception) {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
                }
            } finally {
                try {
                    printer.disconnectPrinter()
                } catch (_: Exception){}
            }
        }).start()
    }

    private val ESC_POS_COMMAND = 0x1B.toByte()
    private val LINE_FEED = 0x0A.toByte()

    private fun printTextToPrinter(printerDeviceAddress: String, text: String) {
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.BLUETOOTH_ADMIN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            checkPermissions()
            return
        }

//        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val printerDevice: BluetoothDevice? = bluetoothAdapter.getRemoteDevice(printerDeviceAddress)
        val socket: BluetoothSocket? = printerDevice?.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
        try {
            socket?.connect()
            val outputStream: OutputStream? = socket?.outputStream
            outputStream?.write(text.toByteArray())
            outputStream?.write(LINE_FEED.toInt())
            outputStream?.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                socket?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

//    @SuppressLint("MissingPermission")
//    fun isPrinter(device: BluetoothDevice): Boolean {
//        // Check if the device name contains "printer" or "receipt"
//        val deviceName = device.name?.lowercase()
//        if (deviceName != null && (deviceName.contains("printer") || deviceName.contains("receipt"))) {
//            return true
//        }
//
//        // Check if the device supports the "Serial Port Profile" (SPP) or "Object Push Profile" (OPP)
//        val profiles = device.bluetoothClass?.supportedProfiles ?: return false
//        return profiles.contains(BluetoothProfile.SERIAL_PORT) || profiles.contains(BluetoothProfile.OBJECT_PUSH)
//    }
}