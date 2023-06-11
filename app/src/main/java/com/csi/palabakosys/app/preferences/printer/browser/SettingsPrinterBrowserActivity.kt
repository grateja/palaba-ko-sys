package com.csi.palabakosys.app.preferences.printer.browser

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.*
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.preferences.printer.SettingsPrinterActivity
import com.csi.palabakosys.databinding.ActivitySettingsPrinterBrowserBinding
import com.csi.palabakosys.util.ActivityContractsLauncher
import com.csi.palabakosys.util.ActivityLauncher
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsPrinterBrowserActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsPrinterBrowserBinding
    private val viewModel: PrinterBrowserViewModel by viewModels()

    private val bluetoothLauncher = ActivityLauncher(this)
    private val permissionLauncher = ActivityContractsLauncher(this)

    private val foundDeviceAdapter = Adapter<PrinterDevice>(R.layout.recycler_item_printer_device)
    private val savedDeviceAdapter = Adapter<PrinterDevice>(R.layout.recycler_item_printer_device)

    private val bluetoothManager: BluetoothManager by lazy {
        getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        bluetoothManager.adapter
    }

    private val locationManager: LocationManager by lazy {
        applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private val  bluetoothPermissions: Array<String> by lazy {
        val permissions = mutableListOf(
            Manifest.permission.BLUETOOTH_ADMIN,
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
        }
        permissions.toTypedArray()
    }

    private val  locationPermissions: Array<String> by lazy {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
        permissions.toTypedArray()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings_printer_browser)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val formattedStatement = getString(R.string.permission_prompt)

        binding.labelPermissionPrompt.text = Html.fromHtml(formattedStatement, Html.FROM_HTML_MODE_COMPACT)

        binding.recyclerFoundDevices.adapter = foundDeviceAdapter
        binding.recyclerSavedDevices.adapter = savedDeviceAdapter

        subscribeEvents()
        subscribeListeners()
    }

    override fun onStart() {
        super.onStart()

        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED).apply {
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(LocationManager.MODE_CHANGED_ACTION)
        }
        registerReceiver(receiver, filter)

        launchPermission(bluetoothPermissions)

        enableBluetooth().let {
            if(it) getBondedDevices()
            updateBluetoothView(it)
        }
        updateLocationView(isGPSEnabled())
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }

    private fun subscribeListeners() {
        viewModel.savedDevices.observe(this, Observer {
            savedDeviceAdapter.setData(it)
        })
        viewModel.foundDevices.observe(this, Observer {
            foundDeviceAdapter.setData(it)
        })
    }

    private fun submit(printerDevice: PrinterDevice) {
        val intent = Intent(intent.action).apply {
            putExtra(SettingsPrinterActivity.PRINTER_DEVICE_EXTRA, printerDevice)
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun subscribeEvents() {
        permissionLauncher.onOk = { result ->
            if(bluetoothPermissions.all { result.containsKey(it) }) {
                getBondedDevices()
            }
        }

        foundDeviceAdapter.onItemClick = {
            submit(it)
        }

        savedDeviceAdapter.onItemClick = {
            submit(it)
        }

        bluetoothLauncher.onOk = {
            getBondedDevices()
        }

        binding.buttonPermission.setOnClickListener {
            launchPermission(bluetoothPermissions)
        }

        binding.buttonTurnOnBluetooth.setOnClickListener {
            enableBluetooth()
        }

        binding.buttonTurnOnLocationServices.setOnClickListener {
            enableLocationServices()
        }

        searchNearbyDevices()
    }

    private fun updateBluetoothView(enabled: Boolean) {
        if(enabled) {
            binding.mainContainer.visibility = View.VISIBLE
            binding.buttonTurnOnBluetooth.visibility = View.GONE
        } else {
            binding.mainContainer.visibility = View.GONE
            binding.buttonTurnOnBluetooth.visibility = View.VISIBLE
        }
    }

    private fun updateLocationView(isGpsEnabled: Boolean) {
        if(isGpsEnabled) {
            binding.buttonTurnOnLocationServices.visibility = View.GONE
        } else {
            binding.buttonTurnOnLocationServices.visibility = View.VISIBLE
        }
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

    private fun isGPSEnabled() : Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun launchPermission(permissions: Array<String>) {
        permissionLauncher.launch(permissions)
    }

    private fun getBondedDevices() {
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
            val items = it.map { device ->
                PrinterDevice(device.name, device.address)
            }
            savedDeviceAdapter.setData(items)
        }
    }

    private fun searchNearbyDevices() {
        val permissions = bluetoothPermissions + locationPermissions
        if(permissions.any {ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED}) {
            permissionLauncher.launch(permissions)
            return
        }
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.BLUETOOTH_SCAN
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            permissionLauncher.launch(bluetoothPermissions + locationPermissions)
//            return
//        }
        bluetoothAdapter.startDiscovery()
    }

    private val receiver = object: BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action) {
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                    updateBluetoothView(state == BluetoothAdapter.STATE_ON)
                }
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    device?.let {
//                        if (ActivityCompat.checkSelfPermission(
//                                applicationContext,
//                                Manifest.permission.BLUETOOTH_CONNECT
//                            ) != PackageManager.PERMISSION_GRANTED
//                        ) {
//                            // TODO: Consider calling
//                            //    ActivityCompat#requestPermissions
//                            // here to request the missing permissions, and then overriding
//                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                            //                                          int[] grantResults)
//                            // to handle the case where the user grants the permission. See the documentation
//                            // for ActivityCompat#requestPermissions for more details.
//                            return
//                        }
                        viewModel.addFoundDevice(
                            PrinterDevice(
                                it.name,
                                it.address
                            )
                        )
                    }


                    println("device found")
                    println(device?.address)
//                    val deviceName = device?.name
                    val deviceHardwareAddress = device?.address // MAC address
                    device?.let {
//                        devicesAdapter.add(it)
                    }
                }
                LocationManager.MODE_CHANGED_ACTION -> {
                    val locationState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        intent.getBooleanExtra(LocationManager.EXTRA_LOCATION_ENABLED, false)
                    } else {
                        false
                    }
                    updateLocationView(locationState)
                    println("Location settings changed")
                    println(locationState)
                }
            }
        }
    }

    private fun enableLocationServices() {
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
        }
    }
}