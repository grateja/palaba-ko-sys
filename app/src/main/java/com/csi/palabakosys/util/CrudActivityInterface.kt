package com.csi.palabakosys.util

import com.csi.palabakosys.app.auth.LoginCredentials
import java.util.*

interface CrudActivityInterface {
    fun get(id: UUID?)
    fun save(loginCredentials: LoginCredentials?)
    fun confirmDelete(loginCredentials: LoginCredentials?)
    fun requestExit()
    fun confirm(entityId: UUID?)
}