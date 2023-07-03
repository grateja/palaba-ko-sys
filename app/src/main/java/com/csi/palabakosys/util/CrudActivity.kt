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
            if(requireAuthOnSave) {
                val intent = Intent(this, AuthActionDialogActivity::class.java).apply {
                    action = ACTION_SAVE
                }
                authLauncher.launch(intent)
            } else {
                this.saveButtonClicked(null)
            }
        }

        buttonDelete?.setOnClickListener {
            if(requireAuthOnDelete) {
                val intent = Intent(this, AuthActionDialogActivity::class.java).apply {
                    action = ACTION_DELETE
                }
                authLauncher.launch(intent)
            } else {
                this.showDeleteDialog(null)
            }
        }

        buttonCancel?.setOnClickListener {
            requestExit()
        }

        authLauncher.onOk = {
            val loginCredentials = it.data?.getParcelableExtra<LoginCredentials>(
                AuthActionDialogActivity.RESULT)
            when (it.data?.action) {
                ACTION_SAVE -> this.saveButtonClicked(loginCredentials)
                ACTION_DELETE -> {
                    showDeleteDialog(loginCredentials)
                }
            }
        }
    }

    private fun showDeleteDialog(loginCredentials: LoginCredentials?) {
        AlertDialog.Builder(this).apply {
            setTitle("Delete this item")
            setMessage("Are you sure you want to proceed?")
            setPositiveButton("Yes") { _, _ ->
                confirmDelete(loginCredentials)
            }
            setNegativeButton("Cancel") { _, _ ->

            }
            create()
        }.show()
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