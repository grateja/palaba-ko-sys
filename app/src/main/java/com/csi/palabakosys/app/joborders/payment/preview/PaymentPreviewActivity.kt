package com.csi.palabakosys.app.joborders.payment.preview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.csi.palabakosys.R
import com.csi.palabakosys.databinding.ActivityPaymentPreviewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentPreviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentPreviewBinding
    private val viewModel: PaymentPreviewViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_preview)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun subscribeEvents() {

    }

    private fun subscribeListeners() {

    }
}