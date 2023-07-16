package com.csi.palabakosys.app.services.edit

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.app.auth.LoginCredentials
import com.csi.palabakosys.databinding.ActivityServicesAddEditBinding
import com.csi.palabakosys.model.EnumMachineType
import com.csi.palabakosys.util.CrudActivity
import com.csi.palabakosys.util.DataState
import com.csi.palabakosys.util.toUUID
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddEditServiceActivity(
//    override var requireAuthOnSave: Boolean = false,
//    override var requireAuthOnDelete: Boolean = false
) : CrudActivity() {
    companion object {
        const val SERVICE_ID_EXTRA = "serviceId"
        const val MACHINE_TYPE_EXTRA = "machineType"
    }
    private lateinit var binding: ActivityServicesAddEditBinding
    private val viewModel: AddEditServiceViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_services_add_edit)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        subscribeListeners()
//        subscribeEvents()

    }

    override fun get(id: UUID?) {
        val serviceId = intent.getStringExtra(SERVICE_ID_EXTRA)?.toUUID()
        val machineType = intent.getParcelableExtra(MACHINE_TYPE_EXTRA) ?: EnumMachineType.REGULAR_WASHER
        viewModel.get(serviceId, machineType)
    }

    override fun onSave() {
        viewModel.validate()
    }

    override fun confirmSave(loginCredentials: LoginCredentials?) {
        viewModel.save()
    }


    override fun confirmDelete(loginCredentials: LoginCredentials?) {
        viewModel.confirmDelete(loginCredentials?.userId)
    }

    override fun requestExit() {
        viewModel.requestExit()
    }

    override fun onBackPressed() {
        viewModel.requestExit()
    }

//    private fun subscribeEvents() {
//        binding.controls.buttonSave.setOnClickListener {
//            viewModel.validate()
//        }
//        binding.controls.buttonCancel.setOnClickListener {
//            viewModel.requestExit()
//        }
//    }

    private fun subscribeListeners() {
        viewModel.dataState.observe(this, Observer {
            when(it) {
                is DataState.ValidationPassed -> {
                    viewModel.save()
                    viewModel.resetState()
                }
                is DataState.SaveSuccess -> {
                    setResult(RESULT_OK, Intent().apply {
                        putExtra(MACHINE_TYPE_EXTRA, it.data.serviceRef.machineType as Parcelable)
                    })
                    finish()
                }
                is DataState.DeleteSuccess -> {
                    finish()
                }
                is DataState.RequestExit -> {
                    confirmExit(it.promptPass)
                    viewModel.resetState()
                }
            }
        })
    }
}