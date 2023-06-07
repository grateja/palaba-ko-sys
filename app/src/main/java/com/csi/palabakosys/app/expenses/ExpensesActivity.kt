package com.csi.palabakosys.app.expenses

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
import com.csi.palabakosys.app.expenses.edit.ExpenseAddEditActivity
import com.csi.palabakosys.databinding.ActivityExpensesBinding
import com.csi.palabakosys.room.entities.EntityExpense
import com.csi.palabakosys.util.ActivityLauncher
import com.csi.palabakosys.util.toUUID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpensesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExpensesBinding
    private val viewModel: ExpensesViewModel by viewModels()
    private val adapter = Adapter<EntityExpense>(R.layout.recycler_item_expenses_full)
    private var searchBar: SearchView? = null
    private val addEditLauncher = ActivityLauncher(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_expenses)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.recyclerExpenses.adapter = adapter

        setSupportActionBar(binding.toolbar)

        subscribeEvents()
        subscribeListeners()
    }

    override fun onResume() {
        super.onResume()
        viewModel.filter()
    }
    private fun subscribeEvents() {
        binding.buttonCreateNew.setOnClickListener {
            openAddEdit(null)
        }
        adapter.onItemClick = {
            openAddEdit(it)
        }
        addEditLauncher.onOk = {
            val expenseId = it.data?.getStringExtra(ExpenseAddEditActivity.EXPENSE_ID).toUUID()
        }
    }

    private fun openAddEdit(expense: EntityExpense?) {
        val intent = Intent(this, ExpenseAddEditActivity::class.java).apply {
            putExtra(ExpenseAddEditActivity.EXPENSE_ID, expense?.id.toString())
        }
        addEditLauncher.launch(intent)
//            startActivity(intent)
    }

    private fun subscribeListeners() {
        viewModel.expenses.observe(this, Observer {
            adapter.setData(it)
        })
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        searchBar = menu?.findItem(R.id.menu_search)?.actionView as SearchView
        searchBar?.apply {
            maxWidth = Integer.MAX_VALUE
            queryHint = "Search expenses remarks"
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