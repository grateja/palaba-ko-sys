package com.csi.palabakosys.app.discounts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.discounts.edit.DiscountAddEditActivity
import com.csi.palabakosys.app.expenses.ExpenseItemFull
import com.csi.palabakosys.app.expenses.edit.ExpenseAddEditActivity
import com.csi.palabakosys.databinding.ActivityDiscountsBinding
import com.csi.palabakosys.room.entities.EntityDiscount
import com.csi.palabakosys.util.ActivityLauncher
import com.csi.palabakosys.util.BaseActivity
import com.csi.palabakosys.util.CrudActivity
import com.csi.palabakosys.util.FilterActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscountsActivity : FilterActivity() {
    private lateinit var binding: ActivityDiscountsBinding
    private val viewModel: DiscountsViewModel by viewModels()
    private val adapter = Adapter<EntityDiscount>(R.layout.recycler_item_discounts_full)
//    private val addEditLauncher = ActivityLauncher(this)

    override var filterHint = "Search Discounts"

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_discounts)
        super.onCreate(savedInstanceState)

        binding.recycler.adapter = adapter

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
        adapter.onItemClick = {
            openAddEdit(it)
        }
        binding.buttonCreateNew.setOnClickListener {
            openAddEdit(null)
        }
    }

    private fun subscribeListeners() {
        viewModel.items.observe(this, Observer {
            adapter.setData(it)
        })
    }

    private fun openAddEdit(item: EntityDiscount?) {
        val intent = Intent(this, DiscountAddEditActivity::class.java).apply {
            putExtra(CrudActivity.ENTITY_ID, item?.id.toString())
        }
        addEditLauncher.launch(intent)
    }
}