package com.csi.palabakosys.room.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData
import com.csi.palabakosys.preferences.PrinterSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreRepository

@Inject
constructor(@ApplicationContext private val context: Context) {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private suspend fun  <T>writeData(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    private fun <T>readData(key: Preferences.Key<T>, defaultValue: T? = null): LiveData<T?> {
        val flow = context.dataStore.data.map { preferences ->
            preferences[key] ?: defaultValue
        }
        return flow.asLiveData()
    }

    val shopName: LiveData<String?> = readData(stringPreferencesKey("shopName"))
    val address: LiveData<String?> = readData(stringPreferencesKey("address"))
    val contactNumber: LiveData<String?> = readData(stringPreferencesKey("contactNumber"))
    val email: LiveData<String?> = readData(stringPreferencesKey("email"))
    val jobOrderDisclaimer: LiveData<String?> = readData(stringPreferencesKey("jobOrderDisclaimer"), "Disclaimer: This document is provided for informational purposes only. It does not constitute an official receipt and should not be considered proof of payment.")

    val printerName: LiveData<String?> = readData(stringPreferencesKey("printerName"))
    val printerAddress: LiveData<String?> = readData(stringPreferencesKey("printerAddress"))
    val printerWidth: LiveData<Float?> = readData(floatPreferencesKey("printerWidth"), 58f)
    val printerCharactersPerLine: LiveData<Int?> = readData(intPreferencesKey("printerCharactersPerLine"), 32)

    val printerSettings = MediatorLiveData<PrinterSettings>().apply {
        fun update() {
            val name = printerName.value
            val address = printerAddress.value
            val width = printerWidth.value ?: 58f
            val characters = printerCharactersPerLine.value ?: 32
            value = PrinterSettings(name, address, width, characters)
        }
        addSource(printerName) {update()}
        addSource(printerAddress) {update()}
        addSource(printerWidth) {update()}
        addSource(printerCharactersPerLine) {update()}
    }

    suspend fun updateShopName(shopName: String?) {
        writeData(stringPreferencesKey("shopName"), shopName ?: "")
    }

    suspend fun updateAddress(address: String?) {
        writeData(stringPreferencesKey("address"), address ?: "")
    }

    suspend fun updateContactNumber(contactNumber: String?) {
        writeData(stringPreferencesKey("contactNumber"), contactNumber ?: "")
    }

    suspend fun updateEmail(email: String?) {
        writeData(stringPreferencesKey("email"), email ?: "")
    }

    suspend fun updateDisclaimer(disclaimer: String?) {
        writeData(stringPreferencesKey("jobOrderDisclaimer"), disclaimer ?: "")
    }

    suspend fun updatePrinterName(printerName: String?) {
        writeData(stringPreferencesKey("printerName"), printerName ?: "")
    }

    suspend fun updatePrinterAddress(printerAddress: String?) {
        writeData(stringPreferencesKey("printerAddress"), printerAddress ?: "")
    }

    suspend fun updatePrinterWidth(printerWidth: Float?) {
        writeData(floatPreferencesKey("printerWidth"), printerWidth ?: 58f)
    }

    suspend fun updatePrinterCharacterLength(printerCharactersPerLine: Int?) {
        writeData(intPreferencesKey("printerCharactersPerLine"), printerCharactersPerLine ?: 32)
    }
}