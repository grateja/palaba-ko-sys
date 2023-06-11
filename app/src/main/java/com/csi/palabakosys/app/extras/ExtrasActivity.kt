package com.csi.palabakosys.app.extras

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.extras.edit.ExtrasAddEditActivity
import com.csi.palabakosys.databinding.ActivityExtrasBinding
import com.csi.palabakosys.util.ActivityLauncher
import com.csi.palabakosys.util.CrudActivity
import com.csi.palabakosys.util.FilterActivity
import com.csi.palabakosys.util.toUUID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExtrasActivity : FilterActivity() {
    private lateinit var binding: ActivityExtrasBinding
    private val viewModel: ExtrasViewModel by viewModels()
    private val adapter = Adapter<ExtrasItemFull>(R.layout.recycler_item_extras_full)
//    private val addEditLauncher = ActivityLauncher(this)

    override var filterHint = "Search Expenses Remarks"

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_extras)
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.recyclerExpenses.adapter = adapter

        subscribeEvents()
        subscribeListeners()
    }
    override fun onResume() {
        super.onResume()
        viewModel.filter()
    }

    override fun onQuery(keyword: String?) {
        viewModel.setKeyword(keyword)
    }

    private fun subscribeEvents() {
        binding.buttonCreateNew.setOnClickListener {
            openAddEdit(null)
        }
        adapter.onItemClick = {
            openAddEdit(it)
        }
        addEditLauncher.onOk = {
            val expenseId = it.data?.getStringExtra(CrudActivity.ENTITY_ID).toUUID()
        }
    }

    private fun openAddEdit(item: ExtrasItemFull?) {
        val intent = Intent(this, ExtrasAddEditActivity::class.java).apply {
            putExtra(CrudActivity.ENTITY_ID, item?.extras?.id.toString())
        }
        addEditLauncher.launch(intent)
    }

    private fun subscribeListeners() {
        viewModel.items.observe(this, Observer {
            adapter.setData(it)
        })
    }
}