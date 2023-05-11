package com.csi.palabakosys.preferences

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.csi.palabakosys.model.Role
import com.csi.palabakosys.room.entities.EntityUser
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferenceRepository
@Inject
constructor (@ApplicationContext context: Context) {
    private val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    val activeUser = MutableLiveData<EntityUser?>()
    var ipSettings = IpSettings(
        sharedPreferences.getString("networkPrefix", null) ?: "192.168",
        sharedPreferences.getInt("subnetId", 210)
    )

    var urlSettings = UrlSettings(
        sharedPreferences.getString("endPoint", "initiate") ?: "",
        sharedPreferences.getLong("connectionTimeout", 5)
    )

    val isAdmin = MutableLiveData<Boolean>()

    fun setCurrentUser(user: EntityUser) {
        activeUser.value = user
        isAdmin.value = user.role != Role.ADMIN
        println(Gson().toJson(user))
    }

    fun getUser() : EntityUser? {
        return activeUser.value
    }

    fun getId() : UUID? {
        return activeUser.value?.id
    }

    fun logout() {
        activeUser.value = null
        isAdmin.value = false
    }

    fun setCurrentPrinter(printerSettings: PrinterSettings) {
        with(sharedPreferences.edit()) {
            putString("printer_name", printerSettings.name)
            putString("printer_address", printerSettings.address)
            putInt("printer_dpi", printerSettings.dpi)
            putFloat("printer_width", printerSettings.width)
            putInt("printer_character", printerSettings.character)
            apply()
        }
    }

    fun printerSettings(): PrinterSettings {
        return PrinterSettings(
            sharedPreferences.getString("printer_name", ""),
            sharedPreferences.getString("printer_address", ""),
            sharedPreferences.getInt("printer_dpi", 203),
            sharedPreferences.getFloat("printer_width", 58f),
            sharedPreferences.getInt("printer_character", 32)
        )
    }

    fun saveIpSettings(ip: IpSettings) {
        with(sharedPreferences.edit()) {
            putString("networkPrefix", ip.networkPrefix)
            putInt("subnetId", ip.subnetId)
            apply()
        }
    }

    fun saveUrlSettings(urlSettings: UrlSettings) {
        with(sharedPreferences.edit()) {
            putString("endPoint", urlSettings.endPoint)
            apply()
        }
    }
}