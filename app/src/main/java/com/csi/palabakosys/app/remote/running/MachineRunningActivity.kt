package com.csi.palabakosys.app.remote.running

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.csi.palabakosys.R
import com.csi.palabakosys.databinding.ActivityMachineRunningBinding
import com.csi.palabakosys.services.MachineActivationService
import com.csi.palabakosys.util.Constants
import com.csi.palabakosys.util.toUUID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MachineRunningActivity : AppCompatActivity() {

    private val viewModel: MachineRunningViewModel by viewModels()
    private lateinit var binding: ActivityMachineRunningBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_machine_running)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        subscribeListeners()
        subscribeEvents()

        val machineId = intent.getStringExtra(Constants.MACHINE_ID_EXTRA).toUUID()

        viewModel.get(machineId)
    }

    private fun subscribeEvents() {

    }

    private fun subscribeListeners() {

    }
}