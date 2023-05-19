package com.csi.palabakosys.app.remote.shared_ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavHostController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.csi.palabakosys.R
import com.csi.palabakosys.databinding.ActivityRemoteActivationBinding
import com.csi.palabakosys.model.EnumMachineType
import com.csi.palabakosys.services.MachineActivationService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemoteActivationActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRemoteActivationBinding
    private val viewModel: RemoteActivationViewModel by viewModels()

    private lateinit var navHost: NavHostFragment
    private lateinit var navHostController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_remote_activation)

        navHost = supportFragmentManager.findFragmentById(R.id.navHostRemote) as NavHostFragment
        navHostController = navHost.navController as NavHostController
        subscribeObservers()
    }

    private fun navigate(destination: Int) {
        if(navHostController.currentDestination?.id != destination) {
            navHostController.navigate(destination)
        }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(this, Observer {
            when(it) {
                is RemoteActivationViewModel.DataState.SelectMachine -> {
                    navigate(R.id.remote_customerFragment)
                    viewModel.resetState()
                }
                is RemoteActivationViewModel.DataState.SelectCustomer -> {
                    navigate(R.id.remote_queuesFragment)
                    viewModel.resetState()
                }
                is RemoteActivationViewModel.DataState.SelectService -> {
                    navigate(R.id.remote_activateFragment)
                    viewModel.resetState()
                }
                is RemoteActivationViewModel.DataState.InitiateConnection -> {
//                    val machineId = it.machineId
//                    val serviceId = it.workerId
                    val intent = Intent(applicationContext, MachineActivationService::class.java).apply {
                        putExtra(MachineActivationService.MACHINE_ID_EXTRA, it.machineId.toString())
                        putExtra(MachineActivationService.JO_SERVICE_ID_EXTRA, it.workerId.toString())
                    }
                    ContextCompat.startForegroundService(applicationContext, intent)
                    viewModel.resetState()
                }
                else -> {}
            }
        })
    }
}