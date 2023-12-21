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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreRepository

@Inject
constructor(@ApplicationContext private val context: Context) {
    companion object {
        const val SHOP_NAME = "shopName"
        const val ADDRESS = "address"
        const val CONTACT_NUMBER = "contactNumber"
        const val EMAIL = "email"

        const val JOB_ORDER_DISCLAIMER = "jobOrderDisclaimer"
        const val JOB_ORDER_MAX_UNPAID = "jobOrderSettingsMaxUnpaid"
        const val JOB_ORDER_REQUIRE_OR_NUMBER = "requireOrNumber"

        const val PRINTER_NAME = "printerName"
        const val PRINTER_ADDRESS = "printerAddress"
        const val PRINTER_WIDTH = "printerWidth"
        const val PRINTER_CHARACTERS_PER_LINE = "printerCharactersPerLine"

        const val DEVELOPER_ACTIVATION_DELAY = "activationDelay"
        const val DEVELOPER_FAKE_CONNECTION_MODE_ON = "face_connection_mode_on"

        const val MACHINE_IP_PREFIX = "networkPrefix"
        const val MACHINE_IP_SUBNET_MASK = "subnetMask"
        const val MACHINE_ACTIVATION_ENDPOINT = "endpoint"
        const val MACHINE_ACTIVATION_TIMEOUT = "activationConnectionTimeout"
    }

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

    fun getInt(key: String, defaultValue: Int = 0) : LiveData<Int> {
        val flow = context.dataStore.data.map { preferences ->
            preferences[intPreferencesKey(key)] ?: defaultValue
        }
        return flow.asLiveData()
    }

    fun getStringAsLiveData(key: String, defaultValue: String = "") : LiveData<String> {
        val flow = context.dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(key)] ?: defaultValue
        }
        return flow.asLiveData()
    }

    fun getFloatAsLiveData(key: String, defaultValue: Float = 0f) : LiveData<Float> {
        val flow = context.dataStore.data.map { preferences ->
            preferences[floatPreferencesKey(key)] ?: defaultValue
        }
        return flow.asLiveData()
    }

    fun getLongAsLiveData(key: String, defaultValue: Long = 0) : LiveData<Long> {
        val flow = context.dataStore.data.map { preferences ->
            preferences[longPreferencesKey(key)] ?: defaultValue
        }
        return flow.asLiveData()
    }

    fun getDoubleAsLiveData(key: String, defaultValue: Double = 0.0) : LiveData<Double> {
        val flow = context.dataStore.data.map { preferences ->
            preferences[doublePreferencesKey(key)] ?: defaultValue
        }
        return flow.asLiveData()
    }

    fun getBooleanAsLiveData(key: String, defaultValue: Boolean = false) : LiveData<Boolean> {
        val flow = context.dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(key)] ?: defaultValue
        }
        return flow.asLiveData()
    }

    suspend fun getIntValue(key: String, defaultValue: Int = 0) : Int {
        val booleanKey = intPreferencesKey(key)

        return context.dataStore.data
            .map { preferences ->
                preferences[booleanKey] ?: defaultValue
            }
            .first()
    }

    suspend fun getStringValue(key: String, defaultValue: String = "") : String {
        val booleanKey = stringPreferencesKey(key)

        return context.dataStore.data
            .map { preferences ->
                preferences[booleanKey] ?: defaultValue
            }
            .first()
    }

    suspend fun getLongValue(key: String, defaultValue: Long = 0) : Long {
        val booleanKey = longPreferencesKey(key)

        return context.dataStore.data
            .map { preferences ->
                preferences[booleanKey] ?: defaultValue
            }
            .first()
    }

    suspend fun getBooleanValue(key: String, defaultValue: Boolean = false) : Boolean {
        val booleanKey = booleanPreferencesKey(key)

        return context.dataStore.data
            .map { preferences ->
                preferences[booleanKey] ?: defaultValue
            }
            .first()
    }

    val shopName: LiveData<String?> = readData(stringPreferencesKey(SHOP_NAME))
    val address: LiveData<String?> = readData(stringPreferencesKey(ADDRESS))
    val contactNumber: LiveData<String?> = readData(stringPreferencesKey(CONTACT_NUMBER))
    val email: LiveData<String?> = readData(stringPreferencesKey(EMAIL))
    val jobOrderDisclaimer: LiveData<String?> = readData(stringPreferencesKey(JOB_ORDER_DISCLAIMER),
        "Disclaimer: This document is provided for informational purposes only. It does not constitute an official receipt and should not be considered proof of payment.")

    val printerName: LiveData<String?> = readData(stringPreferencesKey(PRINTER_NAME))
    val printerAddress: LiveData<String?> = readData(stringPreferencesKey(PRINTER_ADDRESS))
    val printerWidth: LiveData<Float?> = readData(floatPreferencesKey(PRINTER_WIDTH), 58f)
    val printerCharactersPerLine: LiveData<Int?> = readData(intPreferencesKey(
        PRINTER_CHARACTERS_PER_LINE), 32)

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

    val jobOrderSettingsMaxUnpaid: LiveData<Int?> = readData(intPreferencesKey(JOB_ORDER_MAX_UNPAID), 5)

//    suspend fun updateShopName(shopName: String?) {
//        writeData(stringPreferencesKey(SHOP_NAME), shopName ?: "")
//    }
//
//    suspend fun updateAddress(address: String?) {
//        writeData(stringPreferencesKey(ADDRESS), address ?: "")
//    }
//
//    suspend fun updateContactNumber(contactNumber: String?) {
//        writeData(stringPreferencesKey(CONTACT_NUMBER), contactNumber ?: "")
//    }
//
//    suspend fun updateEmail(email: String?) {
//        writeData(stringPreferencesKey(EMAIL), email ?: "")
//    }
//
//    suspend fun updateDisclaimer(disclaimer: String?) {
//        writeData(stringPreferencesKey(JOB_ORDER_DISCLAIMER), disclaimer ?: "")
//    }

    suspend fun updatePrinterName(printerName: String?) {
        writeData(stringPreferencesKey(PRINTER_NAME), printerName ?: "")
    }

    suspend fun updatePrinterAddress(printerAddress: String?) {
        writeData(stringPreferencesKey(PRINTER_ADDRESS), printerAddress ?: "")
    }

    suspend fun updatePrinterWidth(printerWidth: Float?) {
        writeData(floatPreferencesKey(PRINTER_WIDTH), printerWidth ?: 58f)
    }

    suspend fun updatePrinterCharacterLength(printerCharactersPerLine: Int?) {
        writeData(intPreferencesKey(PRINTER_CHARACTERS_PER_LINE), printerCharactersPerLine ?: 32)
    }

    suspend fun <T> update(value: T, key: String) {
        when (value!!::class) {
            Int::class -> writeData(intPreferencesKey(key), value as Int)
            Long::class -> writeData(longPreferencesKey(key), value as Long)
            Float::class -> writeData(floatPreferencesKey(key), value as Float)
            Double::class -> writeData(doublePreferencesKey(key), value as Double)
            Boolean::class -> writeData(booleanPreferencesKey(key), value as Boolean)
            String::class -> writeData(stringPreferencesKey(key), value as String)
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }
}