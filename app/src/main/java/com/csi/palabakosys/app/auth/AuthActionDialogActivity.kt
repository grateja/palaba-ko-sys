package com.csi.palabakosys.app.auth

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.databinding.ActivityAuthActionDialogBinding
import com.csi.palabakosys.model.EnumActionPermission
import com.csi.palabakosys.util.DataState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActionDialogActivity : AppCompatActivity() {
    companion object {
        const val MESSAGE = "message"
        const val PERMISSIONS_EXTRA = "permissions"

        @SuppressLint("Returns Login Credentials if Authentication succeeded")
        const val RESULT = "LoginCredential"
    }
    
    private lateinit var binding: ActivityAuthActionDialogBinding
    private val viewModel: AuthDialogViewModel by viewModels()
    private val adapter = Adapter<String>(R.layout.recycler_item_simple_item)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "Authentication Required"

        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth_action_dialog)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.recyclerPermissions.adapter = adapter

        subscribeEvents()
        subscribeListeners()
    }

    override fun onResume() {
        super.onResume()

        intent.getParcelableArrayListExtra<EnumActionPermission>(PERMISSIONS_EXTRA)?.let {
            viewModel.setPermissions(it)
        }
    }

    private fun subscribeEvents() {
        binding.buttonOk.setOnClickListener {
            viewModel.validate()
        }
        binding.buttonCancel.setOnClickListener {
            finish()
        }
    }

    private fun subscribeListeners() {
        viewModel.permissions.observe(this, Observer {
            adapter.setData(it.map {
                it.description
            })
        })
        viewModel.dataState.observe(this, Observer {
            when(it) {
                is DataState.SaveSuccess -> {
                    setResult(RESULT_OK, Intent().apply {
                        action = intent.action
                        putExtra(RESULT, it.data)
                    })
                    finish()
                }
                is DataState.Invalidate -> {
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}