package com.csi.palabakosys.preferences

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.csi.palabakosys.model.PrinterConfig
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

    fun setCurrentPrinter(printerConfig: PrinterConfig) {
        with(sharedPreferences.edit()) {
            putString("printer_name", printerConfig.name)
            putString("printer_address", printerConfig.address)
            putInt("printer_dpi", printerConfig.dpi)
            putFloat("printer_width", printerConfig.width)
            putInt("printer_character", printerConfig.character)
            apply()
        }
    }

    fun printer(): PrinterConfig {
        return PrinterConfig(
            sharedPreferences.getString("printer_name", ""),
            sharedPreferences.getString("printer_address", ""),
            sharedPreferences.getInt("printer_dpi", 203),
            sharedPreferences.getFloat("printer_width", 58f),
            sharedPreferences.getInt("printer_character", 32)
        )
    }
}