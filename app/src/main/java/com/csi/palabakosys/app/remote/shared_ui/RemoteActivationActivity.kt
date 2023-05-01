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

    private fun navigate(destination: Int) {
        if(navHostController.currentDestination?.id != destination) {
            navHostController.navigate(destination)
        }
    }

    private fun subscribeObservers() {
        viewModel.machine.observe(this, Observer {
            if(it.workerId == null) {
                navigate(R.id.remote_customerFragment)
            } else {
                navigate(R.id.remote_activateFragment)
            }
        })
        viewModel.customerQueue.observe(this, Observer {
            navigate(R.id.remote_queuesFragment)
        })
        viewModel.service.observe(this, Observer {
            navigate(R.id.remote_activateFragment)
        })
    }
}