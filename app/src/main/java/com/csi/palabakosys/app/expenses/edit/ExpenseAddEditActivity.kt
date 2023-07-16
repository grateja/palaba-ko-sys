package com.csi.palabakosys.app.expenses.edit

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.csi.palabakosys.R
import com.csi.palabakosys.app.auth.AuthActionDialogActivity
import com.csi.palabakosys.app.auth.LoginCredentials
import com.csi.palabakosys.databinding.ActivityExpenseAddEditBinding
import com.csi.palabakosys.util.*
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.util.*

@AndroidEntryPoint
class ExpenseAddEditActivity(
//    override var requireAuthOnSave: Boolean = true,
//    override var requireAuthOnDelete: Boolean = true
) : CrudActivity() {
    companion object {
        const val MODIFY_DATE_ACTION = "modifyDate"
    }

    private lateinit var binding: ActivityExpenseAddEditBinding
    private val viewModel: ExpenseAddEditViewModel by viewModels()
    private val dateTimePicker = DateTimePicker(this)
     private val launcher = ActivityLauncher(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_expense_add_edit)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        subscribeListeners()
        subscribeEvents()
    }

    private fun subscribeEvents() {
        binding.textDatePaid.setOnClickListener {
            val intent = Intent(this, AuthActionDialogActivity::class.java).apply {
                action = MODIFY_DATE_ACTION
            }
            launcher.launch(intent)
        }

        launcher.onOk = {
            if(it.data?.action == MODIFY_DATE_ACTION) {
                dateTimePicker.show(viewModel.getDateCreated())
            }
        }

        dateTimePicker.onDateTimeSelected = {
            viewModel.setDateCreated(it)
        }
    }

    private fun subscribeListeners() {
        viewModel.dataState.observe(this, Observer {
            when(it) {
                is DataState.ValidationPassed -> {
                    authenticate(ACTION_SAVE)
                    viewModel.resetState()
                }
                is DataState.SaveSuccess -> {
                    confirmExit(it.data.id)
                    viewModel.resetState()
                }
                is DataState.DeleteSuccess -> {
                    confirmExit(it.data.id)
                    viewModel.resetState()
                }
                is DataState.RequestExit -> {
                    confirmExit(it.promptPass)
                    viewModel.resetState()
                }
            }
        })
    }

    override fun onBackPressed() {
        viewModel.requestExit()
    }

    override fun get(id: UUID?) {
        viewModel.get(id)
    }

    override fun onSave() {
        viewModel.validate()
    }

    override fun confirmDelete(loginCredentials: LoginCredentials?) {
        viewModel.confirmDelete(loginCredentials?.userId)
    }

    override fun confirmSave(loginCredentials: LoginCredentials?) {
        viewModel.save(loginCredentials?.userId)
    }

    override fun requestExit() {
        viewModel.requestExit()
    }

    override fun confirmExit(entityId: UUID?) {
        super.confirmExit(entityId)
        viewModel.resetState()
    }
}