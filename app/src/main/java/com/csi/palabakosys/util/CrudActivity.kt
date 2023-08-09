package com.csi.palabakosys.util

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.csi.palabakosys.R
import com.csi.palabakosys.app.auth.AuthActionDialogActivity
import com.csi.palabakosys.app.auth.LoginCredentials
import java.util.*

abstract class CrudActivity : BaseActivity(), CrudActivityInterface {
    companion object {
        const val ENTITY_ID = "entity_id"
        const val ACTION_SAVE = "save"
        const val ACTION_DELETE = "delete"
    }

    private lateinit var authLauncher: ActivityLauncher
    private var buttonSave: Button? = null
    private var buttonDelete: Button? = null
    private var buttonCancel: Button? = null

    override fun onStart() {
        super.onStart()

        buttonSave = findViewById(R.id.buttonSave)
        buttonDelete = findViewById(R.id.buttonDelete)
        buttonCancel = findViewById(R.id.buttonCancel)

        buttonSave?.setOnClickListener {
            onSave()
        }

        buttonDelete?.setOnClickListener {
            onDelete()
        }

        buttonCancel?.setOnClickListener {
            requestExit()
        }

        authLauncher.onOk = {
            val loginCredentials = it.data?.getParcelableExtra<LoginCredentials>(
                AuthActionDialogActivity.RESULT)

            when (it.data?.action) {
                ACTION_SAVE -> this.confirmSave(loginCredentials)
                ACTION_DELETE ->  this.confirmDelete(loginCredentials)
            }
        }
    }

    override fun onSave() {
        authenticate(ACTION_SAVE)
    }

    override fun onDelete() {
        showDeleteDialog()
    }

    protected open fun authenticate(action: String) {
        val intent = Intent(this, AuthActionDialogActivity::class.java).apply {
            this.action = action
        }
        authLauncher.launch(intent)
    }

    private fun showDeleteDialog() {
        showDeleteConfirmationDialog {
            authenticate(ACTION_DELETE)
        }
//        AlertDialog.Builder(this).apply {
//            setTitle("Delete this item")
//            setMessage("Are you sure you want to proceed?")
//            setPositiveButton("Yes") { _, _ ->
//                authenticate(ACTION_DELETE)
//            }
//            setNegativeButton("Cancel") { _, _ ->
//
//            }
//            create()
//        }.show()
    }

    override fun onStop() {
        super.onStop()
        buttonSave?.setOnClickListener(null)
        buttonDelete?.setOnClickListener(null)
        buttonCancel?.setOnClickListener(null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authLauncher = ActivityLauncher(this)

        intent.getStringExtra(ENTITY_ID).toUUID().let {
            this.get(it)
        }
    }

    override fun confirmExit(entityId: UUID?) {
        val intent = Intent().apply {
            action = intent.action
            putExtra(ENTITY_ID, entityId.toString())
        }
        setResult(RESULT_OK, intent)
        finish()
    }
}