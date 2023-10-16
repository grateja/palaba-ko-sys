package com.csi.palabakosys.app.expenses

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.dashboard.data.DateFilter
import com.csi.palabakosys.app.expenses.edit.ExpenseAddEditActivity
import com.csi.palabakosys.databinding.ActivityExpensesBinding
import com.csi.palabakosys.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpensesActivity : FilterActivity() {
    private lateinit var binding: ActivityExpensesBinding
    private val viewModel: ExpensesViewModel by viewModels()
    private val adapter = Adapter<ExpenseItemFull>(R.layout.recycler_item_expenses_full)
//    private val addEditLauncher = ActivityLauncher(this)

    override var filterHint = "Search Expenses Remarks"

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_expenses)

        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.recyclerExpenses.adapter = adapter

        subscribeEvents()
        subscribeListeners()

        intent.getParcelableExtra<DateFilter>(Constants.DATE_RANGE_FILTER)?.let {
            viewModel.setDates(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.filter(true)
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

    private fun openAddEdit(item: ExpenseItemFull?) {
        val intent = Intent(this, ExpenseAddEditActivity::class.java).apply {
            putExtra(CrudActivity.ENTITY_ID, item?.expense?.id.toString())
        }
        addEditLauncher.launch(intent)
    }

    private fun subscribeListeners() {
        viewModel.items.observe(this, Observer {
            adapter.setData(it)
        })
    }
}