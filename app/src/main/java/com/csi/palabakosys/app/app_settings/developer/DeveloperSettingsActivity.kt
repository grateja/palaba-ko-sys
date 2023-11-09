package com.csi.palabakosys.app.app_settings.developer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.csi.palabakosys.R
import com.csi.palabakosys.databinding.ActivityDeveloperSettingsBinding
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
    }

    private fun subscribeEvents() {

    }

    private fun subscribeListeners() {

    }
}