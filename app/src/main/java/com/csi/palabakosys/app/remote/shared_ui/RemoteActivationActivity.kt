package com.csi.palabakosys.app.remote.shared_ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavHostController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.csi.palabakosys.R
import com.csi.palabakosys.databinding.ActivityRemoteActivationBinding
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

    private fun subscribeObservers() {
        viewModel.machine.observe(this, Observer {
            println("worker id")
            println(it.workerId)
            if(it.workerId == null) {
                navHostController.navigate(R.id.remote_customerFragment)
            } else {
                navHostController.navigate(R.id.remote_activateFragment)
            }
        })
        viewModel.customerQueue.observe(this, Observer {
            navHostController.navigate(R.id.remote_queuesFragment)
        })
        viewModel.service.observe(this, Observer {
            navHostController.navigate(R.id.remote_activateFragment)
        })
//        viewModel.dataState.observe(this, Observer {
//            when(it){
//                is RemoteActivationViewModel.DataState.ActivateRequest -> {
//                    navHostController.popBackStack(R.id.remotePanelFragment, false)
//                }
//                is RemoteActivationViewModel.DataState.Invalidate -> {
//                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
//                }
//            }
//        })
    }
}