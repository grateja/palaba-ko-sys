package com.csi.palabakosys.app.app_settings.job_orders

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.databinding.ActivityAppSettingsJobOrdersBinding
import com.csi.palabakosys.room.repository.DataStoreRepository
import com.csi.palabakosys.util.SettingsNavigationState
import com.csi.palabakosys.util.showTextInputDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppSettingsJobOrdersActivity : AppCompatActivity() {
    private val viewModel: AppSettingsJobOrdersViewModel by viewModels()
    private lateinit var binding: ActivityAppSettingsJobOrdersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_app_settings_job_orders)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        subscribeEvents()
        subscribeListeners()
    }

    private fun subscribeEvents() {
        binding.cardMaxUnpaidJo.card.setOnClickListener {
            viewModel.showMaxUnpaidJobOrder()
        }
        binding.checkboxRequireOrNumber.setOnCheckedChangeListener { _, checked ->
            viewModel.update(checked, DataStoreRepository.JOB_ORDER_REQUIRE_OR_NUMBER)
        }
    }

    private fun subscribeListeners() {
        viewModel.settingsNavigationState.observe(this, Observer {
            when(it) {
                is SettingsNavigationState.OpenIntSettings -> {
                    showTextInputDialog(it.title, it.message, it.value) { result ->
                        viewModel.update(result, it.key)
                    }
                    viewModel.resetState()
                }
            }
        })
    }
}