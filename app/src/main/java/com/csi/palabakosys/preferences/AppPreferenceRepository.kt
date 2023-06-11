package com.csi.palabakosys.preferences

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.csi.palabakosys.app.preferences.user.CurrentUser
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

//    private val _activeUser = MutableLiveData<EntityUser?>()
//    val activeUser: LiveData<EntityUser?> = _activeUser

    var ipSettings = IpSettings(
        sharedPreferences.getString("networkPrefix", null) ?: "192.168",
        sharedPreferences.getInt("subnetId", 210)
    )

    var urlSettings = UrlSettings(
        sharedPreferences.getString("endPoint", "initiate") ?: "",
        sharedPreferences.getLong("connectionTimeout", 5)
    )

//    val isAdmin = MutableLiveData<Boolean>()
//
//    fun setCurrentUser(user: EntityUser) {
//        _activeUser.value = user
//        isAdmin.value = user.role != Role.ADMIN
//        println(Gson().toJson(user))
//    }

//    fun getId() : UUID? {
//        return _activeUser.value?.id
//    }

//    fun logout() {
//        _activeUser.value = null
//        isAdmin.value = false
//    }

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
            sharedPreferences.getString("printer_name", "Tap to select printer") ?: "keme",
            sharedPreferences.getString("printer_address", "Tap to select printer"),
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

    fun requireORNumber(): Boolean {
        return sharedPreferences.getBoolean("requireORNumber", true)
    }
}