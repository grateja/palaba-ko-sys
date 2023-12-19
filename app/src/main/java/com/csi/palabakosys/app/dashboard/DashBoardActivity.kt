package com.csi.palabakosys.app.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.customers.list.CustomersActivity
import com.csi.palabakosys.app.dashboard.data.DateFilter
import com.csi.palabakosys.app.expenses.ExpensesActivity
import com.csi.palabakosys.app.joborders.list.JobOrderListActivity
import com.csi.palabakosys.app.machines.usage.MachineUsageActivity
import com.csi.palabakosys.app.payment_list.PaymentListActivity
import com.csi.palabakosys.app.shared_ui.BottomSheetDateRangePickerFragment
import com.csi.palabakosys.databinding.ActivityDashboardBinding
import com.csi.palabakosys.model.EnumJoFilterBy
import com.csi.palabakosys.model.JobOrderAdvancedFilter
import com.csi.palabakosys.room.entities.*
import com.csi.palabakosys.util.Constants
import com.csi.palabakosys.util.DatePicker
import com.csi.palabakosys.util.showSnackBar
import com.google.android.material.appbar.AppBarLayout
import com.sangcomz.fishbun.util.setStatusBarColor
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import kotlin.math.abs

@AndroidEntryPoint
class DashBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private val viewModel: DashboardViewModel by viewModels()
//    private val dateNavigationAdapter = DateNavigationAdapter()
    private val expensesAdapter = Adapter<EntityExpensesAggrResult>(R.layout.recycler_item_dashboard_expenses)
    private val cashlessPaymentsAdapter = Adapter<EntityCashlessPaymentAggrResult>(R.layout.recycler_item_dashboard_cashless_payments)
    private val extrasAdapter = Adapter<EntityJobOrderExtrasAggrResult>(R.layout.recycler_item_dashboard_jo_extras)
    private val servicesAdapter = Adapter<EntityJobOrderServiceAggrResult>(R.layout.recycler_item_dashboard_jo_services)
    private val productsAdapter = Adapter<EntityJobOrderProductAggrResult>(R.layout.recycler_item_dashboard_jo_products)
    private val pickupDeliveryAdapter = Adapter<EntityJobOrderPickupDeliveryAggrResult>(R.layout.recycler_item_dashboard_jo_deliveries)
    private val machineUsageAdapter = Adapter<EntityMachineUsageAggrResult>(R.layout.recycler_item_dashboard_machine_usages)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(resources.getColor(R.color.color_code_dashboard, null))
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.dashboardExpenses.recyclerView.adapter = expensesAdapter
        binding.dashboardServices.recyclerView.adapter = servicesAdapter
        binding.dashboardProducts.recyclerView.adapter = productsAdapter
        binding.dashboardDeliveries.recyclerView.adapter = pickupDeliveryAdapter
        binding.dashboardMachineUsage.recyclerView.adapter = machineUsageAdapter
        binding.dashboardExtras.recyclerView.adapter = extrasAdapter
        binding.recyclerCashlessPayments.adapter = cashlessPaymentsAdapter

        subscribeListeners()
        subscribeEvents()
    }

    private fun subscribeEvents() {
        binding.cardNewCustomers.setOnClickListener {
            viewModel.openCustomers()
        }
        binding.cardJobOrders.setOnClickListener {
            viewModel.openJobOrders(EnumJoFilterBy.DATE_CREATED)
        }
        binding.cardPaidJobOrders.setOnClickListener {
            viewModel.openPayments()
        }
        binding.dashboardExpenses.card.setOnClickListener {
            viewModel.openExpenses()
        }
        machineUsageAdapter.onItemClick = {
            intent = Intent(this, MachineUsageActivity::class.java).apply {
                putExtra(Constants.MACHINE_ID_EXTRA, it.machineId.toString())
            }
            startActivity(intent)
        }
    }

    private fun subscribeListeners() {
        viewModel.cashlessPayments.observe(this, Observer {
            cashlessPaymentsAdapter.setData(it)
        })
        viewModel.expenses.observe(this, Observer {
            expensesAdapter.setData(it)
        })
        viewModel.jobOrderServices.observe(this, Observer {
            servicesAdapter.setData(it.sortedBy { it.machineType })
        })
        viewModel.jobOrderProducts.observe(this, Observer {
            productsAdapter.setData(it)
        })
        viewModel.jobOrderExtras.observe(this, Observer {
            extrasAdapter.setData(it)
        })
        viewModel.machineUsage.observe(this, Observer {
            machineUsageAdapter.setData(it)
        })
        viewModel.jobOrderPickupDeliveries.observe(this, Observer {
            pickupDeliveryAdapter.setData(it)
        })

        viewModel.navigationState.observe(this, Observer{
            when(it) {
                is DashboardViewModel.NavigationState.OpenDateRangePicker -> {
                    openDatePicker(it.dateFilter)
                    viewModel.resetState()
                }
                is DashboardViewModel.NavigationState.OpenCustomers -> {
                    val intent = Intent(this, CustomersActivity::class.java).apply {
                        putExtra(Constants.DATE_RANGE_FILTER, it.dateFilter)
                    }
                    startActivity(intent)
                    viewModel.resetState()
                }
                is DashboardViewModel.NavigationState.OpenJobOrders -> {
                    val intent = Intent(this, JobOrderListActivity::class.java).apply {
                        val advancedFilter = JobOrderAdvancedFilter(filterBy = it.filterBy, dateFilter = it.dateFilter)
                        putExtra(JobOrderListActivity.ADVANCED_FILTER, advancedFilter)
                        println("datefilter")
                        println(it.dateFilter)
//                        putExtra(Constants.DATE_RANGE_FILTER, it.dateFilter)
//                        putExtra(JobOrderListActivity.FILTER_BY, it.filterBy as Parcelable)
                    }
                    startActivity(intent)
                    viewModel.resetState()
                }
                is DashboardViewModel.NavigationState.OpenJobOrdersPayments -> {
                    val intent = Intent(this, PaymentListActivity::class.java).apply {
                        putExtra(Constants.DATE_RANGE_FILTER, it.dateFilter)
                    }
                    startActivity(intent)
                    viewModel.resetState()
                }
                is DashboardViewModel.NavigationState.OpenExpenses -> {
                    val intent = Intent(this, ExpensesActivity::class.java).apply {
                        putExtra(Constants.DATE_RANGE_FILTER, it.dateFilter)
                    }
                    startActivity(intent)
                    viewModel.resetState()
                }
                is DashboardViewModel.NavigationState.Invalidate -> {
                    binding.root.showSnackBar(it.message)
                    viewModel.resetState()
                }
            }
        })
    }

    private fun openDatePicker(dateFilter: DateFilter) {
//        val datePicker = BottomSheetDateRangePickerFragment.getInstance(dateFilter)
//        datePicker.show(supportFragmentManager, null)
//        datePicker.onOk = {
//            if(it.dateTo.isEqual(it.dateFrom)) {
////                viewModel.setStartDate(it.dateFrom)
//                dateNavigationAdapter.setCurrentDate(it.dateFrom)
//            }
//            viewModel.setDates(it)
//        }
    }
}