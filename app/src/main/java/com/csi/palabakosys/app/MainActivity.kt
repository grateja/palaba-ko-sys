package com.csi.palabakosys.app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.window.layout.WindowMetricsCalculator
import com.csi.palabakosys.R
import com.csi.palabakosys.app.joborders.create.customer.SelectCustomerActivity
import com.csi.palabakosys.app.preferences.ip.SettingsIPAddressActivity
import com.csi.palabakosys.app.remote.shared_ui.RemoteActivationActivity
import com.csi.palabakosys.databinding.ActivityMainBinding
import com.csi.palabakosys.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : EndingActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.btnTest.setOnClickListener {
            val intent = Intent(this, SelectCustomerActivity::class.java)
//            val intent = Intent(this, CreateJobOrderActivity::class.java)
            startActivity(intent)
        }
        binding.btnRemote.setOnClickListener {
            val intent = Intent(this, RemoteActivationActivity::class.java)
            startActivity(intent)
        }
        binding.btnIpSettings?.setOnClickListener {
            val intent = Intent(this, SettingsIPAddressActivity::class.java)
            startActivity(intent)
        }
        computeWindowSizeClasses()
    }

    private fun computeWindowSizeClasses() {
        val metrics = WindowMetricsCalculator.getOrCreate()
            .computeCurrentWindowMetrics(this)

        val widthDp = metrics.bounds.width() /
                resources.displayMetrics.density

        binding.dpWidth.text = widthDp.toString() + " ID:" + binding.dpWidth.id.toString()
        println(widthDp)
    }

    private var doubleclick = false
    override fun onBackPressed() {
        if(doubleclick) {
            moveTaskToBack(true)
            exitProcess(-1)
        }
        doubleclick = true
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleclick = false
        }, 2000)
    }
}