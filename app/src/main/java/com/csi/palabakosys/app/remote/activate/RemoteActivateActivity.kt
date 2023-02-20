package com.csi.palabakosys.app.remote.activate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.csi.palabakosys.R
import com.csi.palabakosys.databinding.ActivityRemoteActivateBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemoteActivateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRemoteActivateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_remote_activate)
        binding.lifecycleOwner = this
    }
}