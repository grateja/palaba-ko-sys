package com.csi.palabakosys.app.main_menu

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.EndingActivity
import com.csi.palabakosys.app.app_settings.developer.DeveloperSettingsActivity
import com.csi.palabakosys.app.customers.list.CustomersActivity
import com.csi.palabakosys.app.discounts.DiscountsActivity
import com.csi.palabakosys.app.expenses.ExpensesActivity
import com.csi.palabakosys.app.extras.ExtrasActivity
import com.csi.palabakosys.app.joborders.list.JobOrderListActivity
import com.csi.palabakosys.app.packages.PackagesActivity
import com.csi.palabakosys.app.app_settings.ip.SettingsIPAddressActivity
import com.csi.palabakosys.app.app_settings.job_orders.AppSettingsJobOrdersActivity
import com.csi.palabakosys.app.app_settings.printer.SettingsPrinterActivity
import com.csi.palabakosys.app.app_settings.user.AppSettingsUserAccountsActivity
import com.csi.palabakosys.app.auth.AuthActionDialogActivity
import com.csi.palabakosys.app.auth.LoginCredentials
import com.csi.palabakosys.app.pickup_and_deliveries.PickupAndDeliveriesActivity
import com.csi.palabakosys.app.products.ProductsActivity
import com.csi.palabakosys.app.remote.RemotePanelActivity
import com.csi.palabakosys.app.dashboard.DashBoardActivity
import com.csi.palabakosys.app.payment_list.PaymentListActivity
import com.csi.palabakosys.app.services.ServicesActivity
import com.csi.palabakosys.databinding.ActivityMainBinding
import com.csi.palabakosys.model.EnumActionPermission
import com.csi.palabakosys.util.ActivityLauncher
import com.csi.palabakosys.util.calculateSpanCount
import com.csi.palabakosys.viewmodels.MainViewModel
//import com.csi.palabakosys.worker.RemoteWorker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : EndingActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private val authLauncher = ActivityLauncher(this)

    private val adapter = Adapter<MenuItem>(R.layout.recycler_item_main_menu)

    private val menuItems by lazy {
        listOf(
            MenuItem(
                "Dashboard",
                "Generate and view sales reports.",
                DashBoardActivity::class.java,
                R.drawable.icon_sales_report,
                permissions = listOf(
                    EnumActionPermission.VIEW_DASHBOARD
                )
            ),
            MenuItem(
                "Job Orders",
                "Manage and track job orders.",
                JobOrderListActivity::class.java,
                R.drawable.icon_job_orders
            ),
            MenuItem(
                "Machines",
                "Access and control remote machines.",
                RemotePanelActivity::class.java,
                R.drawable.icon_machines
            ),
            MenuItem(
                "Payments",
                "View Payments",
                PaymentListActivity::class.java
            ),
            MenuItem(
                "Customers",
                "Manage customer information and profiles.",
                CustomersActivity::class.java,
                R.drawable.icon_customers
            ),
            MenuItem(
                "Expenses",
                "Track and record business expenses.",
                ExpensesActivity::class.java,
                R.drawable.icon_expenses
            ),
            MenuItem(
                "Shop Preferences",
                "Customize services, products, extras, pickup & delivery, packages, and discounts.",
                null,
                R.drawable.icon_shop_preferences,
                null,
                arrayListOf(
                    MenuItem(
                        "Wash & Dry Services",
                        "Manage wash & dry service offerings.",
                        ServicesActivity::class.java,
                        permissions = listOf(EnumActionPermission.MODIFY_SERVICES)
                    ),
                    MenuItem(
                        "Products",
                        "Manage product offerings.",
                        ProductsActivity::class.java,
                        permissions = listOf(EnumActionPermission.MODIFY_INVENTORY)
                    ),
                    MenuItem(
                        "Extras",
                        "Manage additional service extras.",
                        ExtrasActivity::class.java,
                        permissions = listOf(EnumActionPermission.MODIFY_SERVICES)
                    ),
                    MenuItem(
                        "Pickup and Delivery",
                        "Configure pickup and delivery options.",
                        PickupAndDeliveriesActivity::class.java,
                        permissions = listOf(EnumActionPermission.MODIFY_SERVICES)
                    ),
                    MenuItem(
                        "Package",
                        "Manage package offerings.",
                        PackagesActivity::class.java,
                        permissions = listOf(EnumActionPermission.MODIFY_SERVICES)
                    ),
                    MenuItem(
                        "Discount",
                        "Manage discount options.",
                        DiscountsActivity::class.java,
                        permissions = listOf(EnumActionPermission.MODIFY_DISCOUNTS)
                    ),
                )
            ),
            MenuItem(
                "App Settings",
                "Configure application settings.",
                null,
                R.drawable.icon_app_settings,
                null,
                arrayListOf(
                    MenuItem(
                        "Job Orders",
                        "Customize job order settings.",
                        AppSettingsJobOrdersActivity::class.java,
                        permissions = listOf(EnumActionPermission.MODIFY_SETTINGS_JOB_ORDERS)
                    ),
                    MenuItem(
                        "Printer",
                        "Configure printer settings.",
                        SettingsPrinterActivity::class.java,
//                        permissions = listOf(EnumActionPermission.MODIFY_SETTINGS_PRINTERS),
                        menuItems = listOf(
                            MenuItem("Job Orders", "Configure Job Order format", null),
                            MenuItem("Claim Stub", "Configure Claim stub format", null),
                            MenuItem("Machine Activation Stub", "Configure Machine activation stub format", null),
                        )
                    ),
                    MenuItem(
                        "User Accounts",
                        "Manage user accounts.",
                        AppSettingsUserAccountsActivity::class.java,
                        permissions = listOf(EnumActionPermission.MODIFY_USERS)
                    ),
                    MenuItem(
                        "Network",
                        "Configure network and IP address settings.",
                        SettingsIPAddressActivity::class.java,
                        permissions = listOf(EnumActionPermission.MODIFY_SETTINGS_IPADDRESS)
                    ),
                    MenuItem(
                        "Developer",
                        "Technical properties and settings of the shop. Do not modify unless adviced.",
                        DeveloperSettingsActivity::class.java,
                        permissions = listOf(EnumActionPermission.MODIFY_SETTINGS_IPADDRESS)
                    ),
                )
            ),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        adapter.setData(
            menuItems
        )

        binding.recyclerMenu.adapter = adapter
        binding.recyclerMenu.layoutManager = GridLayoutManager(
            this, this.calculateSpanCount(R.dimen.menu_tile_width)
        )

        subscribeEvents()
    }

    private fun subscribeEvents() {
        mainViewModel.navigationState.observe(this, Observer {
            when(it) {
                is MainViewModel.NavigationState.OpenMenu -> {
                    openMenu(it.menuItem)
                    mainViewModel.resetState()
                }
                is MainViewModel.NavigationState.RequestAuthentication -> {
                    requestPermission(it.menuItem)
                    mainViewModel.resetState()
                }
            }
        })
        authLauncher.onOk = {
            when(it.data?.action) {
                AuthActionDialogActivity.AUTH_ACTION -> {
                    it.data?.getParcelableExtra<LoginCredentials>(AuthActionDialogActivity.RESULT)?.let {
                        println("auth passed")
                        mainViewModel.permissionGranted(it)
                    }
                }
            }
        }
        adapter.onItemClick = {
            mainViewModel.openMenu(it)
        }
    }

    private fun requestPermission(menuItem: MenuItem) {
        val intent = Intent(this, AuthActionDialogActivity::class.java).apply {
            action = AuthActionDialogActivity.AUTH_ACTION
            menuItem.permissions?.let {
                putParcelableArrayListExtra(AuthActionDialogActivity.PERMISSIONS_EXTRA, ArrayList(it))
            }
        }
        authLauncher.launch(intent)
    }

    private fun openMenu(menuItem: MenuItem) {
        val intent = if(menuItem.menuItems != null)
            Intent(this, SubMenuActivity::class.java).apply {
                putParcelableArrayListExtra(SubMenuActivity.SUB_MENU_ITEMS_EXTRA, ArrayList(menuItem.menuItems))
                putExtra(SubMenuActivity.SUB_MENU_TITLE_EXTRA, menuItem.text)
            }
        else
            Intent(this, menuItem.activityClass)

        startActivity(intent)
    }

//    private val receiver = object: BroadcastReceiver() {
//        override fun onReceive(p0: Context?, p1: Intent?) {
//            println("receiver received")
//            println(p1?.action)
//            if(p1?.action == "TestService") {
//                val data = p1.getStringExtra("data")
//                println("data from broadcast receiver")
//                println(data)
//            }
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        val intentFilter = IntentFilter("TestService")
//        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter)
////        registerReceiver(receiver, intentFilter)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
//    }
//
//    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
//        if(it) {
//            Toast.makeText(applicationContext, "Press teh button again", Toast.LENGTH_LONG).show()
//        } else {
//            Toast.makeText(applicationContext, "Fuck you", Toast.LENGTH_LONG).show()
//        }
//    }
//
//
//    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
//        // network is available for use
//        override fun onAvailable(network: Network) {
//            super.onAvailable(network)
//            println("Available")
//            network.describeContents()
//        }
//
//        // Network capabilities have changed for the network
//        override fun onCapabilitiesChanged(
//            network: Network,
//            networkCapabilities: NetworkCapabilities
//        ) {
//            super.onCapabilitiesChanged(network, networkCapabilities)
//            val unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
//            println(unmetered)
//        }
//
//        // lost network connection
//        override fun onLost(network: Network) {
//            super.onLost(network)
//            println("Connection lost")
//        }
//    }
//
//    private fun computeWindowSizeClasses() {
//        val metrics = WindowMetricsCalculator.getOrCreate()
//            .computeCurrentWindowMetrics(this)
//
//        val widthDp = metrics.bounds.width() /
//                resources.displayMetrics.density
//
////        binding.dpWidth.text = widthDp.toString() + " ID:" + binding.dpWidth.id.toString()
//        println(widthDp)
//    }
}