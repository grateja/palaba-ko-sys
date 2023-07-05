package com.csi.palabakosys.app.remote.shared_ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavHostController
import androidx.navigation.fragment.NavHostFragment
import com.csi.palabakosys.R
import com.csi.palabakosys.app.remote.activate.RemoteActivationPreviewActivity
import com.csi.palabakosys.app.remote.running.MachineRunningActivity
import com.csi.palabakosys.databinding.ActivityRemoteActivationBinding
import com.csi.palabakosys.model.MachineActivationQueues
import com.csi.palabakosys.model.MachineConnectionStatus
import com.csi.palabakosys.services.MachineActivationService
import com.csi.palabakosys.util.ActivityLauncher
import com.csi.palabakosys.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class RemoteActivationActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRemoteActivationBinding
    private val viewModel: RemoteActivationViewModel by viewModels()

    private lateinit var navHost: NavHostFragment
    private lateinit var navHostController: NavHostController
    private val launcher = ActivityLauncher(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_remote_activation)

        navHost = supportFragmentManager.findFragmentById(R.id.navHostRemote) as NavHostFragment
        navHostController = navHost.navController as NavHostController
        subscribeObservers()
        subscribeEvents()
    }

    private fun navigate(destination: Int) {
        if(navHostController.currentDestination?.id != destination) {
            navHostController.navigate(destination)
        }
    }

    private fun subscribeEvents() {
        launcher.onOk = { result ->
            if(result.resultCode == RESULT_OK) {
                navHostController.popBackStack(R.id.remotePanelFragment, false)
            }
        }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(this, Observer {
            when(it) {
                is RemoteActivationViewModel.DataState.SelectMachine -> {
                    navigate(R.id.remote_customerFragment)
//                    val intent = Intent(this, MachineRunningActivity::class.java).apply {
//                        putExtra(MachineActivationService.MACHINE_ID_EXTRA, it.toString())
//                    }
//                    startActivity(intent)
//                    if(it.entityMachine?.activationRef?.running() == true) {
//                        val intent = Intent(this, MachineRunningActivity::class.java).apply {
//                            putExtra(MachineActivationService.MACHINE_ID_EXTRA, it.entityMachine.id.toString())
//                        }
//                        startActivity(intent)
//                    } else if(it.entityMachine?.serviceActivationId != null) {
//                        openActivationPreview(it.entityMachine.id)
//                    } else {
//                        navigate(R.id.remote_customerFragment)
//                    }
                    viewModel.resetState()
                }
                is RemoteActivationViewModel.DataState.SelectCustomer -> {
                    navigate(R.id.remote_queuesFragment)
                    viewModel.resetState()
                }
//                is RemoteActivationViewModel.DataState.SelectService -> {
//                    navigate(R.id.remote_activateFragment)
//                    viewModel.resetState()
//                }
                is RemoteActivationViewModel.DataState.PrepareActivation -> {
                    // openActivationPreview(it.machineId)

                    val intent = Intent(applicationContext, RemoteActivationPreviewActivity::class.java).apply {
                        println("fucking ids : ${it.machineId} ; ${it.serviceId} ; ${it.customerId}")
                        putExtra(MachineActivationService.JO_SERVICE_ID_EXTRA, it.serviceId.toString())
                        putExtra(MachineActivationService.CUSTOMER_ID_EXTRA, it.customerId.toString())
                        putExtra(Constants.MACHINE_ID_EXTRA, it.machineId.toString())
                    }
                    launcher.launch(intent)
//                    startActivity(intent)
//                    activationPreviewLauncher.launch(intent)
//                    ContextCompat.startForegroundService(applicationContext, intent)
                    viewModel.resetState()
                }
                else -> {}
            }
        })
    }

    private fun openActivationPreview(machineId: UUID) {
        val intent = Intent(applicationContext, RemoteActivationPreviewActivity::class.java).apply {
            putExtra(Constants.MACHINE_ID_EXTRA, machineId.toString())
            putExtra(MachineActivationService.CHECK_ONLY_EXTRA, true)
        }
        startActivity(intent)
    }
}