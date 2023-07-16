package com.csi.palabakosys.app.joborders.cancel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.app.auth.AuthActionDialogActivity
import com.csi.palabakosys.app.auth.LoginCredentials
import com.csi.palabakosys.databinding.ActivityJobOrderCancelBinding
import com.csi.palabakosys.util.ActivityLauncher
import com.csi.palabakosys.util.DataState
import com.csi.palabakosys.util.toUUID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobOrderCancelActivity : AppCompatActivity() {

    companion object {
        const val JOB_ORDER_ID = "jobOrderId"
//        const val ACTION_DELETE_JOB_ORDER = "deleteJobOrder"
    }

    private lateinit var binding: ActivityJobOrderCancelBinding
    private val viewModel: JobOrderCancelViewModel by viewModels()
    private val authLauncher = ActivityLauncher(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_job_order_cancel)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.buttonSave.setOnClickListener {
            viewModel.validate()
        }
        authLauncher.onOk = { result ->
            result.data?.getParcelableExtra<LoginCredentials>(AuthActionDialogActivity.RESULT)?.let {
                viewModel.save(it.userId)
            }
        }
        intent.getStringExtra(JOB_ORDER_ID).toUUID().let {
            viewModel.loadJobOrder(it)
        }
        subscribeListeners()
    }

    private fun subscribeListeners() {
        viewModel.dataState.observe(this, Observer {
            when(it) {
                is DataState.ValidationPassed -> {
                    val intent = Intent(this, AuthActionDialogActivity::class.java)
                    authLauncher.launch(intent)
                    viewModel.resetState()
                }
                is DataState.SaveSuccess -> {
                    setResult(RESULT_OK, Intent().apply {
                        action = intent.action
                        putExtra(JOB_ORDER_ID, it.data.toString())
                    })
                    finish()
                }
                is DataState.Invalidate -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    viewModel.resetState()
                }
            }
        })
    }
}