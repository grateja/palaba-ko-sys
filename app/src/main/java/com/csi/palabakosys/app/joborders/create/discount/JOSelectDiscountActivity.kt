package com.csi.palabakosys.app.joborders.create.discount

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.databinding.ActivityJoSelectDiscountBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JOSelectDiscountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJoSelectDiscountBinding
    private val viewModel: DiscountViewModel by viewModels()
    private val adapter = DiscountsAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_jo_select_discount)
        binding.lifecycleOwner = this
        binding.recyclerDiscounts.adapter = adapter

        subscribeEvents()
        viewModel.loadDiscounts()
    }

    private fun subscribeEvents() {
        viewModel.discounts.observe(this, Observer {
            adapter.setData(it)
            intent.getParcelableExtra<MenuDiscount>("discount")?.let { discount ->
                if(discount.deletedAt == null) {
                    viewModel.setDiscount(discount)
                }
            }
        })

        adapter.onItemClick = {
            setResult(RESULT_OK, Intent().putExtra("discount", it))
            finish()
        }

        binding.buttonCancel.setOnClickListener {
            finish()
        }

        binding.buttonRemove.setOnClickListener {
            setResult(RESULT_OK, Intent().putExtra("discount", ""))
            finish()
        }
    }
}