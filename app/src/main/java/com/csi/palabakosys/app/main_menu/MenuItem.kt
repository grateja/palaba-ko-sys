package com.csi.palabakosys.app.main_menu

import android.os.Parcelable
import com.csi.palabakosys.R
import com.csi.palabakosys.model.EnumActionPermission
import com.csi.palabakosys.model.Role
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuItem(
    val text: String,
    val description: String?,
    val activityClass: Class<*>?,
    val imageResource: Int? = null,
    val permissions: List<EnumActionPermission>? = null,
//    val roles: List<Role>? = null,
    val menuItems: List<MenuItem>? = null,
    val backgroundColor: Int? = -1
) : Parcelable
