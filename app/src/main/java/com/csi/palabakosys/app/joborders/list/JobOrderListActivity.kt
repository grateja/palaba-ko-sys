package com.csi.palabakosys.app.joborders.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.joborders.create.JobOrderCreateActivity
import com.csi.palabakosys.app.joborders.preview.JobOrderPreviewActivity
import com.csi.palabakosys.databinding.ActivityJobOrderListBinding
import com.csi.palabakosys.room.entities.EntityJobOrder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobOrderListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJobOrderListBinding
    private val viewModel: JobOrderListViewModel by viewModels()
    private var searchBar: SearchView? = null
    private val adapter = Adapter<JobOrderListItem>(R.layout.recycler_item_job_order_list_item)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_job_order_list)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.recyclerJobOrders.adapter = adapter

        setSupportActionBar(binding.toolbar)

        subscribeEvents()
        subscribeListeners()
    }

    override fun onResume() {
        super.onResume()
        viewModel.filter()
    }

    private fun subscribeEvents() {
        adapter.onItemClick = {
            val intent = Intent(this, JobOrderCreateActivity::class.java).apply {
                action = JobOrderCreateActivity.ACTION_LOAD_BY_JOB_ORDER_ID
                putExtra(JobOrderCreateActivity.PAYLOAD_EXTRA, it)
            }
            startActivity(intent)
        }
    }
    

    private fun subscribeListeners() {
        viewModel.jobOrders.observe(this, Observer {
            adapter.setData(it)
        })
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        searchBar = menu?.findItem(R.id.menu_search)?.actionView as SearchView
        searchBar?.apply {
            maxWidth = Integer.MAX_VALUE
            queryHint = "Search Customer name or CRN"
            setOnQueryTextFocusChangeListener { view, b ->
                if(b) {
                    binding.toolbar.setBackgroundColor(applicationContext.getColor(R.color.white))
                } else {
                    binding.toolbar.setBackgroundColor(applicationContext.getColor(R.color.teal_700))
                }
            }
        }
        searchBar?.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchBar?.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setKeyword(newText)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
}