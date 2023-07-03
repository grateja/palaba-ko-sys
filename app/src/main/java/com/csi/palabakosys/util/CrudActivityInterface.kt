package com.csi.palabakosys.util

import com.csi.palabakosys.app.auth.LoginCredentials
import java.util.*

interface CrudActivityInterface {
    fun get(id: UUID?)
    fun saveButtonClicked(loginCredentials: LoginCredentials?)
    fun confirmDelete(loginCredentials: LoginCredentials?)
    fun requestExit()
    fun confirmExit(entityId: UUID?)
    var requireAuthOnSave: Boolean
    var requireAuthOnDelete: Boolean
}