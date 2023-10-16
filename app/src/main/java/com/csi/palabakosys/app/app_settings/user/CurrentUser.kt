package com.csi.palabakosys.app.app_settings.user

import com.csi.palabakosys.model.Role

data class CurrentUser(
    val id: String,
    val name: String,
    val email: String,
    val role: Role
)