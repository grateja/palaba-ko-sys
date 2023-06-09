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
import com.csi.palabakosys.databinding.ActivityAuthActionDialogBinding
import com.csi.palabakosys.util.DataState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActionDialogActivity : AppCompatActivity() {
    companion object {
        const val MESSAGE = "message"

        @SuppressLint("Returns Login Credentials if Authentication succeeded")
        const val RESULT = "LoginCredential"
    }
    
    private lateinit var binding: ActivityAuthActionDialogBinding
    private val viewModel: AuthDialogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "Authentication Required"

        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth_action_dialog)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        subscribeEvents()
        subscribeListeners()
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
        viewModel.dataState.observe(this, Observer {
            when(it) {
                is DataState.ConfirmSave -> {
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