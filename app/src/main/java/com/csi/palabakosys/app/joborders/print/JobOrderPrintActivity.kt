package com.csi.palabakosys.app.joborders.print

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.printservice.PrintService
import android.text.Html
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.databinding.ActivityJobOrderPrintBinding
import com.csi.palabakosys.model.PrintItem
import com.csi.palabakosys.services.PrinterService
import com.csi.palabakosys.util.Constants
import com.csi.palabakosys.util.toUUID
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobOrderPrintActivity : AppCompatActivity() {
    private val viewModel: JobOrderPrintViewModel by viewModels()
    private lateinit var binding: ActivityJobOrderPrintBinding

    private val servicesAdapter = Adapter<PrintItem>(R.layout.recycler_item_print_item)
    private val productsAdapter = Adapter<PrintItem>(R.layout.recycler_item_print_item)
    private val extrasAdapter = Adapter<PrintItem>(R.layout.recycler_item_print_item)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_job_order_print)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        subscribeEvents()
        subscribeListeners()

        setupTab()

        intent.getStringExtra(Constants.ID)?.toUUID()?.let {
            viewModel.setJobOrderId(it)
        }

        binding.printItemsServices.items.adapter = servicesAdapter
        binding.printItemsProducts.items.adapter = productsAdapter
        binding.printItemsExtras.items.adapter = extrasAdapter
    }
    private fun setupTab() {
        binding.printTab.apply {
            addTab(newTab().setText("CLAIM STUB"))
            addTab(newTab().setText("MACHINE STUB"))
            addTab(newTab().setText("JOB ORDER"))
        }
    }

    private fun subscribeEvents() {
        binding.buttonPrint.setOnClickListener {
            viewModel.print(binding.printTab.getTabAt(binding.printTab.selectedTabPosition)?.text.toString())
        }
        binding.printTab.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.selectTab(tab?.text.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) { }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                viewModel.selectTab(tab?.text.toString())
            }
        })
    }

    private fun print(formattedText: String) {
        val intent = Intent(this, PrinterService::class.java).apply {
            putExtra(PrinterService.PAYLOAD_TEXT, formattedText)
        }
        startForegroundService(intent)
    }

    private fun subscribeListeners() {
        viewModel.dataState.observe(this, Observer {
            when(it) {
                is JobOrderPrintViewModel.DataState.Submit -> {
                    print(it.formattedText)
                    viewModel.resetState()
                }
            }
        })
        viewModel.services.observe(this, Observer {
            it?.let {
                servicesAdapter.setData(it)
            }
        })
        viewModel.products.observe(this, Observer {
            it?.let {
                productsAdapter.setData(it)
            }
        })
        viewModel.extras.observe(this, Observer {
            it?.let {
                extrasAdapter.setData(it)
            }
        })
    }
}