package com.csi.palabakosys.app.app_settings.developer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.databinding.ActivityDeveloperSettingsBinding
import com.csi.palabakosys.util.showTextInputDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeveloperSettingsActivity : AppCompatActivity() {
    private val viewModel: DeveloperSettingsViewModel by viewModels()
    private lateinit var binding: ActivityDeveloperSettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_developer_settings)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        subscribeEvents()
        subscribeListeners()
    }

    private fun subscribeEvents() {
        binding.textFakeActivationDelay.setOnClickListener {
            viewModel.openConnectionDelayDialog()
        }
    }

    private fun showConnectionDelayDialog(initialValue: Long) {
        showTextInputDialog("Set activation delay", "Fake activation delay in ms", initialValue.toString()) {
            viewModel.updateFakeConnectionDelay(it.toLong())
        }
    }

    private fun subscribeListeners() {
        viewModel.navigationState.observe(this, Observer {
            when(it) {
                is DeveloperSettingsViewModel.NavigationState.OpenConnectionDelay -> {
                    showConnectionDelayDialog(it.msDelay)
                    viewModel.clearState()
                }
            }
        })
    }
}