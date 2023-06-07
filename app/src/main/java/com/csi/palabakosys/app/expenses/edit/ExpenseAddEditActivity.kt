package com.csi.palabakosys.app.expenses.edit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.app.auth.AuthActionDialogActivity
import com.csi.palabakosys.app.auth.LoginCredentials
import com.csi.palabakosys.databinding.ActivityExpenseAddEditBinding
import com.csi.palabakosys.util.ActivityLauncher
import com.csi.palabakosys.util.DataState
import com.csi.palabakosys.util.toUUID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpenseAddEditActivity : AppCompatActivity() {
    companion object {
        const val EXPENSE_ID = "expense_id"
    }
    private lateinit var binding: ActivityExpenseAddEditBinding
    private val viewModel: ExpenseAddEditViewModel by viewModels()
    private val authLauncher = ActivityLauncher(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_expense_add_edit)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        subscribeListeners()
        subscribeEvents()

        intent.getStringExtra(EXPENSE_ID).toUUID().let {
            viewModel.get(it)
        }
    }

    private fun subscribeEvents() {
        binding.buttonSave.setOnClickListener {
            val intent = Intent(this, AuthActionDialogActivity::class.java)
            authLauncher.launch(intent)
        }

        authLauncher.onOk = {
            val loginCredentials = it.data?.getParcelableExtra<LoginCredentials>(AuthActionDialogActivity.RESULT)
            viewModel.save(loginCredentials?.userId)
        }
    }

    private fun subscribeListeners() {
        viewModel.dataState.observe(this, Observer {
            when(it) {
                is DataState.Save -> {
                    val intent = Intent().apply {
                        putExtra(EXPENSE_ID, it.data.id)
                    }
                    setResult(RESULT_OK, intent)
                    viewModel.resetState()
                    finish()
                }
            }
        })
    }
}