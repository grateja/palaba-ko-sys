package com.csi.palabakosys.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsDao

@Inject
constructor(@ApplicationContext private val context: Context) {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    suspend fun getValue(key: String, defaultValue: String) : String {
        return context.dataStore.data
            .map { preferences ->
                preferences[stringPreferencesKey(key)] ?: defaultValue
            }
            .first()
    }

    suspend fun getValue(key: String, defaultValue: Int) : Int {
        return context.dataStore.data
            .map { preferences ->
                preferences[intPreferencesKey(key)] ?: defaultValue
            }
            .first()
    }

    suspend fun getValue(key: String, defaultValue: Boolean) : Boolean {
        return context.dataStore.data
            .map { preferences ->
                preferences[booleanPreferencesKey(key)] ?: defaultValue
            }
            .first()
    }

    suspend fun getValue(key: String, defaultValue: Float) : Float {
        return context.dataStore.data
            .map { preferences ->
                preferences[floatPreferencesKey(key)] ?: defaultValue
            }
            .first()
    }

    suspend fun getValue(key: String, defaultValue: Long) : Long {
        return context.dataStore.data
            .map { preferences ->
                preferences[longPreferencesKey(key)] ?: defaultValue
            }
            .first()
    }

    fun getAsLiveData(key: String, defaultValue: String) : LiveData<String> {
        val flow = context.dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(key)] ?: defaultValue
        }
        return flow.asLiveData()
    }

    fun getAsLiveData(key: String, defaultValue: Boolean) : LiveData<Boolean> {
        val flow = context.dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(key)] ?: defaultValue
        }
        return flow.asLiveData()
    }

    fun getAsLiveData(key: String, defaultValue: Int) : LiveData<Int> {
        val flow = context.dataStore.data.map { preferences ->
            preferences[intPreferencesKey(key)] ?: defaultValue
        }
        return flow.asLiveData()
    }

    fun getAsLiveData(key: String, defaultValue: Float) : LiveData<Float> {
        val flow = context.dataStore.data.map { preferences ->
            preferences[floatPreferencesKey(key)] ?: defaultValue
        }
        return flow.asLiveData()
    }

    fun getAsLiveData(key: String, defaultValue: Long) : LiveData<Long> {
        val flow = context.dataStore.data.map { preferences ->
            preferences[longPreferencesKey(key)] ?: defaultValue
        }
        return flow.asLiveData()
    }

    suspend fun <T> update(value: T, key: String) {
        if(value != null) {
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

    suspend fun  <T>writeData(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
}