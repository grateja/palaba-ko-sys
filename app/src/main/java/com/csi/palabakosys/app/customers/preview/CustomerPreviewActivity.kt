package com.csi.palabakosys.app.customers.preview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.csi.palabakosys.R
import com.csi.palabakosys.databinding.ActivityCustomerPreviewBinding
import com.csi.palabakosys.util.toUUID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomerPreviewActivity : AppCompatActivity() {
    companion object {
        const val CUSTOMER_ID_EXTRA = "customerId"
    }

    private lateinit var binding: ActivityCustomerPreviewBinding
    private val viewModel: CustomerPreviewViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_customer_preview)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        subscribeEvents()
        subscribeListeners()
    }

    override fun onResume() {
        super.onResume()

        intent.getStringExtra(CUSTOMER_ID_EXTRA)?.toUUID()?.let {
            viewModel.load(it)
        }
    }

    private fun subscribeEvents() {

    }

    private fun subscribeListeners() {
    }
}