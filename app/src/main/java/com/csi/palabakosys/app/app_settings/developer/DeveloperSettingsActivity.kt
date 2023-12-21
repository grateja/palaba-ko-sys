package com.csi.palabakosys.app.app_settings.developer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.databinding.ActivityDeveloperSettingsBinding
import com.csi.palabakosys.settings.DeveloperSettingsRepository
//import com.csi.palabakosys.room.repository.DataStoreRepository
import com.csi.palabakosys.util.SettingsNavigationState
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
        binding.cardActivationDelay.card.setOnClickListener {
            viewModel.openConnectionDelayDialog()
        }
        binding.checkboxFakeConnectionModeOn.setOnCheckedChangeListener { _, checked ->
            viewModel.update(checked, DeveloperSettingsRepository.DEVELOPER_FAKE_CONNECTION_MODE_ON)
        }
    }

//    private fun showConnectionDelayDialog(initialValue: Long) {
//        showTextInputDialog("Set activation delay", "Fake activation delay in ms", initialValue) { result, key ->
//            result?.let {
//                viewModel.updateFakeConnectionDelay(it)
//            }
//        }
//    }

    private fun subscribeListeners() {
        viewModel.settingsNavigationState.observe(this, Observer {
            when(it) {
                is SettingsNavigationState.OpenLongSettings -> {
                    showTextInputDialog(it.title, it.message, it.value) { result ->
                        viewModel.update(result, it.key)
//                        result?.let {
////                            viewModel.updateFakeConnectionDelay(it)
//                        }
                    }
                    viewModel.resetState()
                }
            }
        })
    }
}