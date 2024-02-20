package com.csi.palabakosys.app.joborders.create.delivery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.app.joborders.create.JobOrderCreateActivity
import com.csi.palabakosys.databinding.ActivityJoSelectDeliveryBinding
import com.csi.palabakosys.util.DataState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JOSelectDeliveryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJoSelectDeliveryBinding
    private val viewModel: DeliveryViewModel by viewModels()
    private val deliveryVehiclesAdapter = DeliveryVehiclesAdapter()
    private lateinit var deliveryProfileModal: DeliveryModalFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_jo_select_delivery)
        binding.lifecycleOwner = this
        binding.recyclerDeliveryVehicles.adapter = deliveryVehiclesAdapter
        binding.viewModel = viewModel

        subscribeEvents()
        subscribeListeners()
    }

    private fun subscribeListeners() {
        viewModel.dataState.observe(this, Observer {
            when(it) {
                is DataState.SaveSuccess -> {
                    setResult(RESULT_OK, Intent().apply {
                        action = intent.action
                        putExtra(JobOrderCreateActivity.PAYLOAD_EXTRA, it.data)
                    })
                    viewModel.resetState()
                    finish()
                }
                else -> {}
            }
        })

        viewModel.deliveryProfiles.observe(this, Observer {
            deliveryVehiclesAdapter.setData(it)
            intent.getParcelableExtra<DeliveryCharge>(JobOrderCreateActivity.PAYLOAD_EXTRA)?.let { deliveryCharge ->
                if(deliveryCharge.deletedAt == null) {
                    viewModel.setDeliveryCharge(deliveryCharge)
                }
            }
        })

        viewModel.profile.observe(this, Observer {
            deliveryVehiclesAdapter.notifySelection(it.deliveryProfileRefId)
        })
    }

    private fun subscribeEvents() {
        deliveryVehiclesAdapter.onItemClick = {
            viewModel.setDeliveryProfile(it.deliveryProfileRefId)
            showOptions()
        }

        binding.buttonCancel.setOnClickListener {
            finish()
        }

        binding.buttonRemove.setOnClickListener {
            viewModel.prepareDeliveryCharge(true)
        }
    }

    private fun showOptions() {
        deliveryProfileModal = DeliveryModalFragment.newInstance().apply {
            onOk = {
                viewModel.prepareDeliveryCharge(false)
            }
        }
        deliveryProfileModal.show(supportFragmentManager, this.toString())
    }
}