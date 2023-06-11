package com.csi.palabakosys.app.products.edit

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
import com.csi.palabakosys.databinding.ActivityProductAddEditBinding
import com.csi.palabakosys.util.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ProductAddEditActivity : CrudActivity() {
    private lateinit var binding: ActivityProductAddEditBinding
    private val viewModel: ProductAddEditViewModel by viewModels()
//    private val authLauncher = ActivityLauncher(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_add_edit)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        subscribeListeners()
        subscribeEvents()

//        intent.getStringExtra(ENTITY_ID).toUUID().let {
//            viewModel.get(it)
//        }
    }
    private fun subscribeEvents() {
//        binding.controls.buttonSave.setOnClickListener {
//            val intent = Intent(this, AuthActionDialogActivity::class.java).apply {
//                action = ACTION_SAVE
//            }
//            authLauncher.launch(intent)
//        }
//        binding.controls.buttonDelete.setOnClickListener {
//            val intent = Intent(this, AuthActionDialogActivity::class.java).apply {
//                action = ACTION_DELETE
//            }
//            authLauncher.launch(intent)
//        }
//        binding.controls.buttonCancel.setOnClickListener {
//            viewModel.requestExit()
//        }

//        authLauncher.onOk = {
//            val loginCredentials = it.data?.getParcelableExtra<LoginCredentials>(AuthActionDialogActivity.RESULT)
//            if(it.data?.action == ACTION_SAVE) {
//                viewModel.save()
//            } else if(it.data?.action == ACTION_DELETE) {
//                AlertDialog.Builder(this).apply {
//                    setTitle("Delete this item")
//                    setMessage("Are you sure you want to proceed?")
//                    setPositiveButton("Yes") { _, _ ->
//                        viewModel.confirmDelete(loginCredentials?.userId)
//                    }
//                    create()
//                }.show()
//            }
//        }
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

    override fun onBackPressed() {
        viewModel.requestExit()
    }

    override fun get(id: UUID?) {
        viewModel.get(id)
    }

    override fun save(loginCredentials: LoginCredentials?) {
        viewModel.save()
    }

    override fun confirmDelete(loginCredentials: LoginCredentials?) {
        viewModel.confirmDelete(loginCredentials?.userId)
    }

    override fun requestExit() {
        viewModel.requestExit()
    }

    override fun confirm(entityId: UUID?) {
        super.confirm(entityId)
        viewModel.resetState()
    }
}