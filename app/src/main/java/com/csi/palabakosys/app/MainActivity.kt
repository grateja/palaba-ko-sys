package com.csi.palabakosys.app

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.window.layout.WindowMetricsCalculator
import com.csi.palabakosys.R
import com.csi.palabakosys.app.joborders.create.customer.SelectCustomerActivity
import com.csi.palabakosys.app.machines.MachinesActivity
import com.csi.palabakosys.app.preferences.ip.SettingsIPAddressActivity
import com.csi.palabakosys.app.preferences.printer.SettingsPrinterActivity
import com.csi.palabakosys.app.remote.shared_ui.RemoteActivationActivity
import com.csi.palabakosys.broadcast_receiver.ConnectivityReceiver
import com.csi.palabakosys.databinding.ActivityMainBinding
import com.csi.palabakosys.model.EnumMachineType
import com.csi.palabakosys.services.MachineActivationService
import com.csi.palabakosys.viewmodels.MainViewModel
//import com.csi.palabakosys.worker.RemoteWorker
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint
import java.net.InetAddress
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : EndingActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.btnTest.setOnClickListener {
            val intent = Intent(this, SelectCustomerActivity::class.java)
//            val intent = Intent(this, CreateJobOrderActivity::class.java)
            startActivity(intent)
        }
        binding.btnRemote.setOnClickListener {
            val intent = Intent(this, RemoteActivationActivity::class.java)
            startActivity(intent)
        }
        binding.btnIpSettings.setOnClickListener {
            val intent = Intent(this, SettingsIPAddressActivity::class.java)
            startActivity(intent)
        }
        binding.btnPrinterSettings.setOnClickListener {
            val intent = Intent(this, SettingsPrinterActivity::class.java)
            startActivity(intent)
        }
        binding.btnMachines.setOnClickListener {
            val intent = Intent(this, MachinesActivity::class.java)
            startActivity(intent)
//            if(ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
//            val intent = Intent(applicationContext, MachineActivationService::class.java).apply {
//                putExtra("machineType", EnumMachineType.REGULAR_DRYER.id)
//                putExtra(RemoteWorker.MACHINE_ID, "ff72b605-88f8-4a3e-9818-7cb83167e336")
//                putExtra(RemoteWorker.JOB_ORDER_SERVICE_ID, "c621ceba-9d56-4703-9719-782eec6e3b87")
//            }
//            ContextCompat.startForegroundService(applicationContext, intent)
//            val s = startService(intent)
//            stopService(intent)
//                startForegroundService(intent)
//            } else {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
//                }
//            }

        }

        computeWindowSizeClasses()
        val connectivityManager = getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }

    private val receiver = object: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            println("receiver received")
            println(p1?.action)
            if(p1?.action == "TestService") {
                val data = p1.getStringExtra("data")
                println("data from broadcast receiver")
                println(data)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter("TestService")
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter)
//        registerReceiver(receiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if(it) {
            Toast.makeText(applicationContext, "Press teh button again", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(applicationContext, "Fuck you", Toast.LENGTH_LONG).show()
        }
    }


    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // network is available for use
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            println("Available")
            network.describeContents()
        }

        // Network capabilities have changed for the network
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
            println(unmetered)
        }

        // lost network connection
        override fun onLost(network: Network) {
            super.onLost(network)
            println("Connection lost")
        }
    }

    private fun computeWindowSizeClasses() {
        val metrics = WindowMetricsCalculator.getOrCreate()
            .computeCurrentWindowMetrics(this)

        val widthDp = metrics.bounds.width() /
                resources.displayMetrics.density

        binding.dpWidth.text = widthDp.toString() + " ID:" + binding.dpWidth.id.toString()
        println(widthDp)
    }

//    private var doubleclick = false
//    override fun onBackPressed() {
//        if(doubleclick) {
//            moveTaskToBack(true)
////            exitProcess(-1)
//        }
//        doubleclick = true
//        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
//        Handler(Looper.getMainLooper()).postDelayed(Runnable {
//            doubleclick = false
//        }, 2000)
//    }
}