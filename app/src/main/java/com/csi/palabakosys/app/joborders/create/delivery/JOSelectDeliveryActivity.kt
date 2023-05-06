package com.csi.palabakosys.app.joborders.create.delivery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.databinding.ActivityJoSelectDeliveryBinding
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
        subscribeEvents()
    }

    private fun subscribeEvents() {
        viewModel.deliveryProfiles.observe(this, Observer {
            deliveryVehiclesAdapter.setData(it)
            intent.getParcelableExtra<DeliveryCharge>("deliveryCharge")?.let { deliveryCharge ->
                if(deliveryCharge.deletedAt == null) {
                    viewModel.setDeliveryCharge(deliveryCharge)
                }
            }
        })

        viewModel.profile.observe(this, Observer {
            deliveryVehiclesAdapter.notifySelection(it.deliveryProfileRefId)
        })

//        deliveryVehiclesAdapter.onItemClick = {
//            viewModel.setDeliveryProfile(it)
//        }
//
        deliveryVehiclesAdapter.onItemClick = {
            viewModel.setDeliveryProfile(it.deliveryProfileRefId)
            showOptions()
        }

        binding.buttonCancel.setOnClickListener {
            finish()
        }

        binding.buttonRemove.setOnClickListener {
            setResult(RESULT_OK, Intent().apply {
                putExtra("deliveryCharge", viewModel.prepareDeliveryCharge(true))
            })
            finish()
        }
    }

    private fun showOptions() {
        deliveryProfileModal = DeliveryModalFragment.newInstance().apply {
            onOk = {
//                viewModel.selectDeliveryProfile(it)
                setResult(RESULT_OK, Intent().apply {
                    putExtra("deliveryCharge", viewModel.prepareDeliveryCharge(false))
                })
                finish()
            }
        }
        deliveryProfileModal.show(supportFragmentManager, this.toString())
    }
}