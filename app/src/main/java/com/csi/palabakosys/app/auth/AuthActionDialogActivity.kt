package com.csi.palabakosys.app.auth

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
import com.csi.palabakosys.databinding.ActivityAuthActionDialogBinding
import com.csi.palabakosys.model.EnumActionPermission
import com.csi.palabakosys.model.EnumAuthMethod
import com.csi.palabakosys.util.DataState
import com.csi.palabakosys.util.showSnackBar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.itsxtt.patternlock.PatternLockView
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class AuthActionDialogActivity : AppCompatActivity() {
    companion object {
        const val AUTH_ACTION = "authAction"

        const val MESSAGE = "message"
        const val PERMISSIONS_EXTRA = "permissions"

        @SuppressLint("Returns Login Credentials if Authentication succeeded")
        const val RESULT = "LoginCredential"
    }
    
    private lateinit var binding: ActivityAuthActionDialogBinding
    private val viewModel: AuthDialogViewModel by viewModels()
    private val privilegeAdapter = Adapter<String>(R.layout.recycler_item_simple_item)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth_action_dialog)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

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
        binding.textInputCashlessProvider.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                view.post {
                    binding.textInputCashlessProvider.selectAll()
                }
            }
        }
        binding.buttonOk.setOnClickListener {
            viewModel.validate(AuthDialogViewModel.AuthMethod.AuthByPassword)
        }
        binding.buttonCancel.setOnClickListener {
            finish()
        }
        binding.buttonAuthMethodPassword.setOnClickListener {
            viewModel.setAuthMethod(EnumAuthMethod.AUTH_BY_PASSWORD)
        }
        binding.buttonAuthMethodPattern.setOnClickListener {
            viewModel.setAuthMethod(EnumAuthMethod.AUTH_BY_PATTERN)
        }
//        binding.buttonAuthMethodBiometric.setOnClickListener {
//            viewModel.setAuthMethod(EnumAuthMethod.AUTH_BY_BIOMETRIC)
//        }
        binding.buttonPrivilege.setOnClickListener {
            val requiredAuthPrivilegesFragment = RequiredAuthPrivilegesFragment.newInstance()
            requiredAuthPrivilegesFragment.show(supportFragmentManager, "privilege")
        }
        binding.patternLock.setOnPatternListener(object : PatternLockView.OnPatternListener {
            override fun onComplete(ids: ArrayList<Int>): Boolean {
                viewModel.validate(AuthDialogViewModel.AuthMethod.AuthByPattern(ids))
                return true
            }
        })
    }

    private fun subscribeListeners() {
        viewModel.emails.observe(this, Observer {
            val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_dropdown_item_1line, it)
            binding.textInputCashlessProvider.setAdapter(adapter)
        })
        viewModel.permissions.observe(this, Observer {
            privilegeAdapter.setData(it.map {
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
                    binding.root.showSnackBar(it.message)
                }
            }
        })
    }
}