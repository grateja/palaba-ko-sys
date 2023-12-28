package com.csi.palabakosys.app.app_settings.printer.browser

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.*
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.app_settings.printer.SettingsPrinterActivity
import com.csi.palabakosys.databinding.ActivitySettingsPrinterBrowserBinding
import com.csi.palabakosys.util.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsPrinterBrowserActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsPrinterBrowserBinding
    private val viewModel: PrinterBrowserViewModel by viewModels()

    private val helper = BluetoothPrinterHelper(this)
//    private val bluetoothHelper = BluetoothPermissionHelper(this)
//    private val locationHelper = LocationPermissionHelper(this)

//    private val bluetoothStateLauncher = ActivityLauncher(this)
    private val permissionLauncher = ActivityContractsLauncher(this)

    private val foundDeviceAdapter = Adapter<PrinterDevice>(R.layout.recycler_item_printer_device)
    private val savedDeviceAdapter = Adapter<PrinterDevice>(R.layout.recycler_item_printer_device)

//    private val bluetoothManager: BluetoothManager by lazy {
//        getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
//    }
//
//    private val bluetoothAdapter: BluetoothAdapter by lazy {
//        bluetoothManager.adapter
//    }

    private val locationManager: LocationManager by lazy {
        applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

//    private val  bluetoothPermissions: Array<String> by lazy {
//        val permissions = mutableListOf(
//            Manifest.permission.BLUETOOTH_ADMIN,
//        )
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
//            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
//        }
//        permissions.toTypedArray()
//    }

//    private val  locationPermissions: Array<String> by lazy {
//        val permissions = mutableListOf(
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//        )
//        permissions.toTypedArray()
//    }
//
//    private fun hasPermissions(permissions: Array<String>) : Boolean {
//        for (permission in permissions) {
//            if (ContextCompat.checkSelfPermission(this, permission)
//                != PackageManager.PERMISSION_GRANTED
//            ) {
//                println("Permission not granted")
//                println(permission)
//                return false
//            }
//            println("permission granted")
//            println(permission)
//        }
//        return true
//    }

//    private fun hasBluetoothPermissions(): Boolean {
//        return (ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.BLUETOOTH
//        ) == PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.BLUETOOTH_ADMIN
//                ) == PackageManager.PERMISSION_GRANTED &&
//                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.BLUETOOTH_CONNECT
//                ) == PackageManager.PERMISSION_GRANTED))
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings_printer_browser)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val formattedStatement = getString(R.string.permission_prompt)
        binding.textBluetoothPermissionPrompt.text = Html.fromHtml(formattedStatement, Html.FROM_HTML_MODE_COMPACT)

        binding.devices.adapter = savedDeviceAdapter

        subscribeEvents()
        subscribeListeners()
    }

//    private fun setupTabs() {
//        binding.tab.apply {
//            addTab(binding.tab.newTab().setText("Saved devices"))
//            addTab(binding.tab.newTab().setText("Nearby devices"))
//            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
//                override fun onTabSelected(tab: TabLayout.Tab?) {
//                    if(tab?.position == 0) {
////                        getSavedDevices()
//                    } else {
//                        viewModel.setDevices(emptyList())
////                        helper.enableLocation()
////                        if(locationHelper.hasPermissions()) {
////                            bluetoothHelper.startDiscovery()
////                        } else {
////                            locationHelper.requestPermission()
////                        }
//                    }
//                }
//
//                override fun onTabUnselected(tab: TabLayout.Tab?) { }
//
//                override fun onTabReselected(tab: TabLayout.Tab?) { }
//            })
//        }
//    }

    override fun onResume() {
        super.onResume()
        helper.registerReceiver(this)
//        bluetoothHelper.checkBluetooth()
//        bluetoothHelper.registerReceiver(this)
//        locationHelper.registerReceiver(this)
//        viewModel.setBluetoothPermission(hasPermissions(bluetoothPermissions))
//        viewModel.setBluetoothState(bluetoothAdapter.isEnabled)
    }

//    override fun onStart() {
//        super.onStart()

//        registerRece

//        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED).apply {
//            addAction(BluetoothDevice.ACTION_FOUND)
//            addAction(LocationManager.MODE_CHANGED_ACTION)
//        }
//        registerReceiver(receiver, filter)

//        launchPermission(bluetoothPermissions)

//        enableBluetooth().let {
//            if(it) getBondedDevices()
//            updateBluetoothView(it)
//        }
//        updateLocationView(isGPSEnabled())
//    }

    override fun onPause() {
        super.onPause()
        helper.unregisterReceiver()
//        bluetoothHelper.unregisterReceiver()
//        locationHelper.unregisterReceiver()
    }

    private fun subscribeListeners() {
//        viewModel.bluetoothEnabled.observe(this, Observer {
//            if(it) {
////                if(hasPermissions(bluetoothPermissions)) {
////                    getSavedDevices()
////                }
//            }
//        })
        viewModel.devices.observe(this, Observer {
            savedDeviceAdapter.setData(it)
        })
//        viewModel.foundDevices.observe(this, Observer {
//            foundDeviceAdapter.setData(it)
//        })
    }

    private fun submit(printerDevice: PrinterDevice) {
        val intent = Intent(intent.action).apply {
            putExtra(SettingsPrinterActivity.PRINTER_DEVICE_EXTRA, printerDevice)
        }
        setResult(RESULT_OK, intent)
        finish()
    }

//    private fun toggleBluetoothState() {
//        if(!hasPermissions(bluetoothPermissions)) {
//            launchPermission(bluetoothPermissions)
//            println("has no permission")
//        } else {
//            println("has permission")
//            enableBluetooth()
//        }
//    }

    private fun subscribeEvents() {
        binding.swipeRefresh.setOnRefreshListener {
            helper.startDiscovery()
        }
        binding.cardBluetooth.setOnClickListener {
            helper.enableBluetooth()
//            bluetoothHelper.enableBluetooth()
//            toggleBluetoothState()
        }
        binding.checkboxBluetoothState.setOnClickListener {
            helper.enableBluetooth()
        }

        binding.buttonLocationServiceToggle.setOnClickListener {
            helper.enableLocation()
        }

        helper.apply {
            setOnBluetoothStateChanged {
                viewModel.setBluetoothState(it)
            }
            setOnBondedDeviceLoaded {
                viewModel.setDevices(it)
            }
            setOnDeviceFound {
                viewModel.addFoundDevice(it)
                binding.swipeRefresh.isRefreshing = false
            }
            setOnLocationStateChanged {
                viewModel.setLocationState(it)
                helper.startDiscovery()
            }
        }

//        permissionLauncher.onOk = { result ->
//            println("result")
//            println(result)
//            if(bluetoothPermissions.all { result.containsKey(it) }) {
//                println("enable bluetooth")
//                enableBluetooth()
//            } else if(locationPermissions.all { result.containsKey(it) }) {
//                println("enable location")
//                enableLocationServices()
//            } else {
//                println("not all enabled")
//            }
//        }

        foundDeviceAdapter.onItemClick = {
            submit(it)
        }

        savedDeviceAdapter.onItemClick = {
            submit(it)
        }

//        bluetoothStateLauncher.onOk = {
//            getBondedDevices()
//        }

//        binding.buttonPermission.setOnClickListener {
//            launchPermission(bluetoothPermissions)
//        }

//        binding.buttonTurnOnBluetooth.setOnClickListener {
//            enableBluetooth()
//        }

//        binding.buttonTurnOnLocationServices.setOnClickListener {
//            enableLocationServices()
//        }

//        searchNearbyDevices()
    }

//    private fun updateLocationView(isGpsEnabled: Boolean) {
//        if(isGpsEnabled) {
//            binding.buttonTurnOnLocationServices.visibility = View.GONE
//        } else {
//            binding.buttonTurnOnLocationServices.visibility = View.VISIBLE
//        }
//    }

//    private fun enableBluetooth() {
//        val intentBluetooth = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//        bluetoothStateLauncher.launch(intentBluetooth)
//    }

//    private fun isGPSEnabled() : Boolean {
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//    }
//
//    private fun launchPermission(permissions: Array<String>) {
//        permissionLauncher.launch(permissions)
//    }

//    @SuppressLint("MissingPermission")
//    private fun getSavedDevices() {
//        if(hasPermissions(bluetoothPermissions)) {
//            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) && ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.BLUETOOTH_CONNECT
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                launchPermission(bluetoothPermissions)
//                return
//            }
//            bluetoothAdapter.bondedDevices.let {
//                val items = it.map { device ->
//                    PrinterDevice(device.name, device.address)
//                }
//                savedDeviceAdapter.setData(items)
//            }
//        }
//    }

//    private fun getNearbyDevices() {
//        val permissions = locationPermissions + bluetoothPermissions
//        if(hasPermissions(permissions)) {
//            if (ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.BLUETOOTH_SCAN
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                launchPermission(permissions)
//                return
//            }
////            bluetoothAdapter.startDiscovery()
//            viewModel.setDevices(emptyList())
//        }
//    }

//    private fun searchNearbyDevices() {
//        val permissions = bluetoothPermissions + locationPermissions
//        if(permissions.any {ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED}) {
//            permissionLauncher.launch(permissions)
//            return
//        }
////        if (ActivityCompat.checkSelfPermission(
////                this,
////                Manifest.permission.BLUETOOTH_SCAN
////            ) != PackageManager.PERMISSION_GRANTED
////        ) {
////            permissionLauncher.launch(bluetoothPermissions + locationPermissions)
////            return
////        }
////        bluetoothAdapter.startDiscovery()
//    }

//    private val receiver = object: BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            when(intent?.action) {
//                BluetoothAdapter.ACTION_STATE_CHANGED -> {
//                    val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
//                }
//                BluetoothDevice.ACTION_FOUND -> {
//                    val device: BluetoothDevice? =
//                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
//                    device?.let {
////                        if (ActivityCompat.checkSelfPermission(
////                                applicationContext,
////                                Manifest.permission.BLUETOOTH_CONNECT
////                            ) != PackageManager.PERMISSION_GRANTED
////                        ) {
////                            // TODO: Consider calling
////                            //    ActivityCompat#requestPermissions
////                            // here to request the missing permissions, and then overriding
////                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
////                            //                                          int[] grantResults)
////                            // to handle the case where the user grants the permission. See the documentation
////                            // for ActivityCompat#requestPermissions for more details.
////                            return
////                        }
////                        viewModel.addFoundDevice(
////                            PrinterDevice(
////                                it.name,
////                                it.address
////                            )
////                        )
//                    }
//
//
//                    println("device found")
//                    println(device?.address)
////                    val deviceName = device?.name
//                    val deviceHardwareAddress = device?.address // MAC address
//                    device?.let {
////                        devicesAdapter.add(it)
//                    }
//                }
//                LocationManager.MODE_CHANGED_ACTION -> {
//                    val locationState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                        intent.getBooleanExtra(LocationManager.EXTRA_LOCATION_ENABLED, false)
//                    } else {
//                        false
//                    }
////                    updateLocationView(locationState)
//                    println("Location settings changed")
//                    println(locationState)
//                }
//            }
//        }
//    }

//    private fun enableLocationServices() {
//        val locationRequest = LocationRequest.Builder(10000L)
//            .build()
//        val builder = LocationSettingsRequest.Builder()
//            .addLocationRequest(locationRequest)
//
//        val client: SettingsClient = LocationServices.getSettingsClient(this)
//        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
//
//        task.addOnSuccessListener { locationSettingsResponse ->
//            // All location settings are satisfied. The client can initialize
//            // location requests here.
//            // ...
//            val state = locationSettingsResponse.locationSettingsStates
//
//            val label =
//                "GPS >> (Present: ${state?.isGpsPresent}  | Usable: ${state?.isGpsUsable} ) \n\n" +
//                        "Network >> ( Present: ${state?.isNetworkLocationPresent} | Usable: ${state?.isNetworkLocationUsable} ) \n\n" +
//                        "Location >> ( Present: ${state?.isLocationPresent} | Usable: ${state?.isLocationUsable} )"
//            println(label)
//        }
//
//        task.addOnFailureListener { exception ->
//            exception.printStackTrace()
//            try {
//                val response = task.getResult(ApiException::class.java)
//                // All location settings are satisfied. The client can initialize location
//                // requests here.
//            } catch (exception: ApiException) {
//                when (exception.statusCode) {
//                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
//                        try {
//                            // Cast to a resolvable exception.
//                            val resolvable: ResolvableApiException = exception as ResolvableApiException
//                            // Show the dialog by calling startResolutionForResult(),
//                            // and check the result in onActivityResult().
//                            resolvable.startResolutionForResult(
//                                this, Priority.PRIORITY_HIGH_ACCURACY
//                            )
//                        } catch (e: IntentSender.SendIntentException) {
//                            // Ignore the error.
//                        } catch (e: ClassCastException) {
//                            // Ignore, should be an impossible error.
//                        }
//                    }
//                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
//                        // Location settings are not satisfied. But could be fixed by showing the
//                        // user a dialog.
//
//                        // Location settings are not satisfied. However, we have no way to fix the
//                        // settings so we won't show the dialog.
//                    }
//                }
//            }
//        }
//    }
}