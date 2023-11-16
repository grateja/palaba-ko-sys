package com.csi.palabakosys.app.pickup_and_deliveries.add_edit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.csi.palabakosys.R
import com.csi.palabakosys.app.auth.LoginCredentials
import com.csi.palabakosys.databinding.ActivityAddEditDeliveryProfileBinding
import com.csi.palabakosys.databinding.ActivityPickupAndDeliveriesBinding
import com.csi.palabakosys.util.CrudActivity
import com.csi.palabakosys.util.DataState
import com.csi.palabakosys.util.selectAllOnFocus
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddEditDeliveryProfileActivity : CrudActivity() {
    private val viewModel: AddEditDeliveryProfileViewModel by viewModels()
    private lateinit var binding: ActivityAddEditDeliveryProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit_delivery_profile)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.textBaseFare.selectAllOnFocus()
        binding.textPricePerKm.selectAllOnFocus()

        subscribeListeners()
    }

    private fun subscribeListeners() {
        viewModel.dataState.observe(this, androidx.lifecycle.Observer {
            when(it) {
                is DataState.ValidationPassed -> {
                    viewModel.save()
                    viewModel.resetState()
                }
                is DataState.SaveSuccess -> {
                    confirmExit(it.data.id)
                }
                is DataState.DeleteSuccess -> {
                    confirmExit(it.data.id)
                }
                is DataState.RequestExit -> {
                    confirmExit(it.promptPass)
                    viewModel.resetState()
                }
            }
        })
    }

    override fun onSave() {
        viewModel.validate()
    }

    override fun get(id: UUID?) {
        viewModel.get(id)
    }

    override fun confirmDelete(loginCredentials: LoginCredentials?) {
        viewModel.confirmDelete(loginCredentials?.userId)
    }

    override fun confirmSave(loginCredentials: LoginCredentials?) {
        viewModel.save()
    }

    override fun requestExit() {
        viewModel.requestExit()
    }

    override fun onBackPressed() {
        viewModel.requestExit()
    }

    override fun confirmExit(entityId: UUID?) {
        super.confirmExit(entityId)
        viewModel.resetState()
    }
}