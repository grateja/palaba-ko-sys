package com.csi.palabakosys.app.app_settings.ip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.databinding.ActivitySettingsIpAddressBinding
import com.csi.palabakosys.util.SettingsNavigationState
import com.csi.palabakosys.util.showTextInputDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsIPAddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsIpAddressBinding
    private val viewModel: IPAddressSettingsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings_ip_address)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
//        binding.buttonSave.setOnClickListener {
//            viewModel.save()
//            finish()
//        }
        subscribeEvents()
        subscribeListeners()
    }

    private fun subscribeEvents() {
        binding.cardPrefix.card.setOnClickListener {
            viewModel.showEditPrefix()
        }
        binding.cardSubnet.card.setOnClickListener {
            viewModel.showEditSubnetMask()
        }
        binding.cardEndpoint.card.setOnClickListener {
            viewModel.showEditEndpoint()
        }
        binding.cardTimeout.card.setOnClickListener {
            viewModel.showEditTimeout()
        }
    }

    private fun subscribeListeners() {
        viewModel.settingsNavigationState.observe(this, Observer {
            when(it) {
                is SettingsNavigationState.OpenStringSettings -> {
                    showTextInputDialog(it.title, it.message, it.value) { result ->
                        viewModel.update(result, it.key)
                    }
                    viewModel.resetState()
                }
                is SettingsNavigationState.OpenLongSettings -> {
                    showTextInputDialog(it.title, it.message, it.value) { result ->
                        viewModel.update(result, it.key)
                    }
                    viewModel.resetState()
                }
            }
        })
    }
}