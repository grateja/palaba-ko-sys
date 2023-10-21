package com.csi.palabakosys.app.joborders.create.shared_ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.csi.palabakosys.app.auth.AuthActionDialogActivity
import com.csi.palabakosys.app.joborders.create.CreateJobOrderViewModel
import com.csi.palabakosys.app.joborders.create.JobOrderCreateActivity
import com.csi.palabakosys.databinding.FragmentCreateJobOrderInfoBinding
import com.csi.palabakosys.model.EnumActionPermission
import com.csi.palabakosys.util.DateTimePicker
import com.csi.palabakosys.util.FragmentLauncher
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateJobOrderInfoFragment : Fragment() {
    private val viewModel: CreateJobOrderViewModel by activityViewModels()
    private lateinit var binding: FragmentCreateJobOrderInfoBinding

    val launcher = FragmentLauncher(this)

    private val dateTimePicker: DateTimePicker by lazy {
        DateTimePicker(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateJobOrderInfoBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        subscribeEvents()
        subscribeListeners()

        return binding.root
    }

    private fun subscribeEvents() {
        launcher.onOk = {
            viewModel.requestModifyDateTime()
        }
        binding.cardCreatedAt.setOnClickListener {
            openAuthRequestModifyDateTime()
        }
        binding.cardCustomer.setOnClickListener {
            viewModel.editCustomer()
        }
        dateTimePicker.setOnDateTimeSelectedListener {
            viewModel.applyDateTime(it)
        }
    }

    private fun subscribeListeners() {
        viewModel.dataState().observe(viewLifecycleOwner, Observer {
            when(it) {
                is CreateJobOrderViewModel.DataState.ModifyDateTime -> {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        dateTimePicker.show(it.createdAt)
                    }, 100)
                    viewModel.resetState()
                }
            }
        })
    }

    private fun openAuthRequestModifyDateTime() {
        val intent = Intent(context, AuthActionDialogActivity::class.java).apply {
            action = JobOrderCreateActivity.ACTION_MODIFY_DATETIME
            putExtra(
                AuthActionDialogActivity.PERMISSIONS_EXTRA, arrayListOf(
                EnumActionPermission.MODIFY_JOB_ORDERS,
                EnumActionPermission.MODIFY_SERVICES
            ))
        }
        launcher.launch(intent)
    }
}