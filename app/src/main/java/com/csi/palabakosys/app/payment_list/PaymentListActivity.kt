package com.csi.palabakosys.app.payment_list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.dashboard.data.DateFilter
import com.csi.palabakosys.app.joborders.payment.JobOrderPaymentActivity.Companion.PAYMENT_ID
import com.csi.palabakosys.app.joborders.payment.preview.PaymentPreviewActivity
import com.csi.palabakosys.app.shared_ui.BottomSheetDateRangePickerFragment
import com.csi.palabakosys.databinding.ActivityPaymentListBinding
import com.csi.palabakosys.room.entities.EntityJobOrderPaymentListItem
import com.csi.palabakosys.util.Constants.Companion.DATE_RANGE_FILTER
import com.csi.palabakosys.util.FilterActivity
import com.csi.palabakosys.viewmodels.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class PaymentListActivity : FilterActivity() {
    private val viewModel: PaymentListViewModel by viewModels()
    private lateinit var binding: ActivityPaymentListBinding
    private val adapter = Adapter<EntityJobOrderPaymentListItem>(R.layout.recycler_item_job_order_payment_list_item)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_list)
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.recyclerPaymentList.adapter = adapter

        subscribeListeners()
        subscribeEvents()

        intent.getParcelableExtra<DateFilter>(DATE_RANGE_FILTER)?.let {
            viewModel.setDateRange(it)
        }

        viewModel.filter(true)
    }

    override var filterHint: String = "Enter OR Number or Customer name"

    override fun onQuery(keyword: String?) {
        viewModel.setKeyword(keyword)
    }

    private fun openPayment(paymentId: UUID) {
        val intent = Intent(this, PaymentPreviewActivity::class.java).apply {
            putExtra(PAYMENT_ID, paymentId.toString())
        }
        startActivity(intent)
    }

    private fun openDatePicker(dateFilter: DateFilter) {
        BottomSheetDateRangePickerFragment.getInstance(dateFilter).let {
            it.show(supportFragmentManager, null)
            it.onOk = {
                viewModel.setDateRange(it)
            }
        }
    }

    private fun subscribeEvents() {
        adapter.onScrollAtTheBottom = {
            viewModel.loadMore()
        }
        adapter.onItemClick = {
            openPayment(it.id)
        }
        binding.cardDateRange.setOnClickListener {
            viewModel.showDatePicker()
        }
        binding.buttonClearDateFilter.setOnClickListener {
            viewModel.clearDates()
        }
    }

    private fun subscribeListeners() {
        viewModel.dataState.observe(this, Observer {
            when(it) {
                is ListViewModel.DataState.LoadItems -> {
                    if(it.reset) {
                        adapter.setData(it.items)
                    } else {
                        adapter.addItems(it.items)
                    }
                    println("all items")
                    println(it.items)
                    viewModel.clearState()
                }
            }
        })
        viewModel.dateFilter.observe(this, Observer {
            viewModel.filter(true)
        })
        viewModel.navigationState.observe(this, Observer {
            when(it) {
                is PaymentListViewModel.NavigationState.OpenDateFilter -> {
                    openDatePicker(it.dateFilter)
                }
            }
        })
    }
}