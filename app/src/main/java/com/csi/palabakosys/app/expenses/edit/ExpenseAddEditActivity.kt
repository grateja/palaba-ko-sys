package com.csi.palabakosys.app.expenses.edit

import android.app.AlertDialog
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
import com.csi.palabakosys.util.BaseActivity
import com.csi.palabakosys.util.DataState
import com.csi.palabakosys.util.toUUID
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ExpenseAddEditActivity : BaseActivity() {
    companion object {
        const val EXPENSE_ID = "expense_id"
        const val ACTION_SAVE = "save"
        const val ACTION_DELETE = "delete"
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
        binding.controls.buttonSave.setOnClickListener {
            val intent = Intent(this, AuthActionDialogActivity::class.java).apply {
                action = ACTION_SAVE
            }
            authLauncher.launch(intent)
        }
        binding.controls.buttonDelete.setOnClickListener {
            val intent = Intent(this, AuthActionDialogActivity::class.java).apply {
                action = ACTION_DELETE
            }
            authLauncher.launch(intent)
        }
        binding.controls.buttonCancel.setOnClickListener {
            viewModel.requestExit()
        }

        authLauncher.onOk = {
            val loginCredentials = it.data?.getParcelableExtra<LoginCredentials>(AuthActionDialogActivity.RESULT)
            if(it.data?.action == ACTION_SAVE) {
                viewModel.save(loginCredentials?.userId)
            } else if(it.data?.action == ACTION_DELETE) {
                AlertDialog.Builder(this).apply {
                    setTitle("Delete this item")
                    setMessage("Are you sure you want to proceed?")
                    setPositiveButton("Yes") { _, _ ->
                        viewModel.confirmDelete(loginCredentials?.userId)
                    }
                    create()
                }.show()
            }
        }
    }

    private fun subscribeListeners() {
        viewModel.dataState.observe(this, Observer {
            when(it) {
                is DataState.ConfirmSave -> {
                    confirm(it.data.id)
                }
                is DataState.ConfirmDelete -> {
                    confirm(it.data.id)
                }
                is DataState.RequestExit -> {
                    confirmExit(it.promptPass)
                    viewModel.resetState()
                }
            }
        })
    }

    private fun confirm(expenseId: UUID?) {
        val intent = Intent().apply {
            putExtra(EXPENSE_ID, expenseId.toString())
        }
        setResult(RESULT_OK, intent)
        viewModel.resetState()
        finish()
    }

    override fun onBackPressed() {
        viewModel.requestExit()
    }
}