package com.csi.palabakosys.app.products

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.app.products.edit.ProductAddEditActivity
import com.csi.palabakosys.databinding.ActivityProductsBinding
import com.csi.palabakosys.util.ActivityLauncher
import com.csi.palabakosys.util.BaseActivity
import com.csi.palabakosys.util.FilterActivity
import com.csi.palabakosys.util.toUUID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsActivity : FilterActivity() {
    private lateinit var binding: ActivityProductsBinding
    private val viewModel: ProductsViewModel by viewModels()
    private val adapter = Adapter<ProductItemFull>(R.layout.recycler_item_products_full)
    private val addEditLauncher = ActivityLauncher(this)

    override var queryHint = "Search Discounts"

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_products)

        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.recycler.adapter = adapter

        subscribeEvents()
        subscribeListeners()
    }

    private fun subscribeEvents() {
        binding.buttonCreateNew.setOnClickListener {
            openAddEdit(null)
        }
        adapter.onItemClick = {
            openAddEdit(it)
        }
        addEditLauncher.onOk = {
            val expenseId = it.data?.getStringExtra(BaseActivity.ENTITY_ID).toUUID()
        }
    }

    private fun openAddEdit(item: ProductItemFull?) {
        val intent = Intent(this, ProductAddEditActivity::class.java).apply {
            putExtra(BaseActivity.ENTITY_ID, item?.product?.id.toString())
        }
        addEditLauncher.launch(intent)
    }

    private fun subscribeListeners() {
        viewModel.items.observe(this, Observer {
            adapter.setData(it)
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.filter()
    }

    override fun onQuery(keyword: String?) {
        viewModel.setKeyword(keyword)
    }
}