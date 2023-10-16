package com.csi.palabakosys.app.app_settings.ip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.csi.palabakosys.R
import com.csi.palabakosys.databinding.ActivitySettingsIpAddressBinding
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
        binding.buttonSave.setOnClickListener {
            viewModel.save()
            finish()
        }
    }
}