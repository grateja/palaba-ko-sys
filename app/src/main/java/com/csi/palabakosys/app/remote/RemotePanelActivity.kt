package com.csi.palabakosys.app.remote

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.M
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.csi.palabakosys.R
import com.csi.palabakosys.app.machines.options.BottomSheetMachineOptionsFragment
import com.csi.palabakosys.app.machines.MachineListItem
import com.csi.palabakosys.app.remote.activate.RemoteActivationPreviewActivity
import com.csi.palabakosys.app.remote.customer.RemoteCustomerActivity
import com.csi.palabakosys.app.remote.panel.RemotePanelAdapter
import com.csi.palabakosys.app.remote.running.RemoteRunningActivity
import com.csi.palabakosys.databinding.ActivityRemotePanelBinding
import com.csi.palabakosys.model.MachineActivationQueues
import com.csi.palabakosys.services.MachineActivationService
import com.csi.palabakosys.util.ActivityLauncher
import com.csi.palabakosys.util.Constants
import com.csi.palabakosys.util.calculateSpanCount
import com.google.android.material.tabs.TabLayout
import com.sangcomz.fishbun.util.setStatusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemotePanelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRemotePanelBinding
    private lateinit var machineOption: BottomSheetMachineOptionsFragment
    private val viewModel: RemotePanelViewModel by viewModels()

    private val adapter = RemotePanelAdapter() //Adapter<MachineListItem>(R.layout.recycler_item_machine_tile)
//    private val regularDryersAdapter = RemotePanelAdapter() //Adapter<MachineListItem>(R.layout.recycler_item_machine_tile)
//    private val titanWashersAdapter = RemotePanelAdapter() //Adapter<MachineListItem>(R.layout.recycler_item_machine_tile)
//    private val titanDryersAdapter = RemotePanelAdapter() //Adapter<MachineListItem>(R.layout.recycler_item_machine_tile)

    private val launcher = ActivityLauncher(this)

    fun isWifiConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(resources.getColor(R.color.color_code_machines, null))
        binding = DataBindingUtil.setContentView(this, R.layout.activity_remote_panel)
        setSupportActionBar(binding.toolbar)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.recycler.layoutManager = GridLayoutManager(this, spanCount)
        binding.recycler.adapter = adapter
//
//        binding.inclRegularDryers.recycler.layoutManager = GridLayoutManager(this, spanCount)
//        binding.inclRegularDryers.recycler.adapter = regularDryersAdapter
//
//        binding.inclTitanWashers.recycler.layoutManager = GridLayoutManager(this, spanCount)
//        binding.inclTitanWashers.recycler.adapter = titanWashersAdapter
//
//        binding.inclTitanDryers.recycler.layoutManager = GridLayoutManager(this, spanCount)
//        binding.inclTitanDryers.recycler.adapter = titanDryersAdapter

        subscribeEvents()
        subscribeListeners()

        println("is connected ${isWifiConnected()}")

        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.registerDefaultNetworkCallback(wifiStateCallback)
    }

    private val wifiStateCallback = object: ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Log.d("WifiStateCallback", "Network available")
            checkWifiConnection()
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            Log.d("WifiStateCallback", "Network lost")
            runOnUiThread {
                viewModel.setWiFiConnectionState(false)
            }
            // Handle Wi-Fi disconnected state
        }

        private fun checkWifiConnection() {
            val connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val network = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

            runOnUiThread {
                viewModel.setWiFiConnectionState(networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true)
            }
        }
    }

    private fun subscribeEvents() {
        adapter.onItemClick = { selectMachine(it) }
        adapter.onOptionClick = { showOptions(it) }
        binding.tabMachineType.tabMachineType.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.setMachineType(tab?.text.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
//        regularDryersAdapter.onItemClick = { selectMachine(it) }
//        titanWashersAdapter.onItemClick = { selectMachine(it) }
//        titanDryersAdapter.onItemClick = { selectMachine(it) }
    }

    private fun subscribeListeners() {
        viewModel.machines.observe(this, Observer {
            adapter.setData(it)
            adapter.startUpdatingTime()
//            regularDryersAdapter.setData(it.filter { it.machine.machineType == EnumMachineType.REGULAR_DRYER })
//            titanWashersAdapter.setData(it.filter { it.machine.machineType == EnumMachineType.TITAN_WASHER })
//            titanDryersAdapter.setData(it.filter { it.machine.machineType == EnumMachineType.TITAN_DRYER })
        })
    }

    private fun showOptions(item: MachineListItem) {
        machineOption = BottomSheetMachineOptionsFragment.getInstance(item.machine.id)
        machineOption.show(supportFragmentManager, null)
    }

    private fun selectMachine(item: MachineListItem) {
        val intent = if(item.machine.activationRef?.running() == true) {
            Intent(this, RemoteRunningActivity::class.java).apply {
                putExtra(Constants.MACHINE_ID_EXTRA, item.machine.id.toString())
            }
        } else if(item.machine.serviceActivationId != null) {
            Intent(this, RemoteActivationPreviewActivity::class.java).apply {
                val machineId = item.machine.id
                val joServiceId = item.machine.serviceActivationId
                val customerId = item.customer?.id
                val queue = MachineActivationQueues(machineId, joServiceId, customerId)
                putExtra(MachineActivationService.ACTIVATION_QUEUES_EXTRA, queue)
            }
        } else {
            Intent(this, RemoteCustomerActivity::class.java).apply {
                putExtra(Constants.MACHINE_ID_EXTRA, item.machine.id.toString())
            }
        }
        launcher.launch(intent)
    }

    private val spanCount: Int by lazy {
//        val columnWidth = resources.getDimensionPixelSize(R.dimen.machine_tile_width)
//        val margin = resources.getDimension(R.dimen.activity_horizontal_margin)
        applicationContext.calculateSpanCount(R.dimen.machine_tile_width)
//        val displayMetrics = resources.displayMetrics`
//        val parentWidth = displayMetrics.widthPixels - (margin * 2).toInt()
//
//        val spanCount = parentWidth / columnWidth
//        if (spanCount > 0) spanCount else 1
    }
}