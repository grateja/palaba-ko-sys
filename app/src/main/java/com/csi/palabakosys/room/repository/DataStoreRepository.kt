package com.csi.palabakosys.room.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreRepository

@Inject
constructor(@ApplicationContext private val context: Context) {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private suspend fun writeData(key: Preferences.Key<String>, value: String?) {
        context.dataStore.edit { preferences ->
            preferences[key] = value ?: ""
        }
    }

    private fun readData(key: Preferences.Key<String>): LiveData<String?> {
        val flow = context.dataStore.data.map { preferences ->
            preferences[key]
        }
        return flow.asLiveData()
    }

    fun observeData(key: Preferences.Key<String>): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[key]
        }
    }

    val shopName: LiveData<String?> = readData(stringPreferencesKey("shopName"))
    val address: LiveData<String?> = readData(stringPreferencesKey("address"))
    val contactNumber: LiveData<String?> = readData(stringPreferencesKey("contactNumber"))
    val email: LiveData<String?> = readData(stringPreferencesKey("email"))
    suspend fun updateShopName(shopName: String?) {
        writeData(stringPreferencesKey("shopName"), shopName)
    }

    suspend fun updateAddress(address: String?) {
        writeData(stringPreferencesKey("address"), address)
    }

    suspend fun updateContactNumber(contactNumber: String?) {
        writeData(stringPreferencesKey("contactNumber"), contactNumber)
    }

    suspend fun updateEmail(email: String?) {
        writeData(stringPreferencesKey("email"), email)
    }
}