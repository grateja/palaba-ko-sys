package com.csi.palabakosys.app.app_settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.room.repository.DataStoreRepository
import com.csi.palabakosys.util.SettingsNavigationState
import kotlinx.coroutines.launch

open class SettingsViewModel(private val repository: DataStoreRepository): ViewModel() {
    protected val navigationState = MutableLiveData<SettingsNavigationState>()
    val settingsNavigationState: LiveData<SettingsNavigationState> = navigationState

    fun resetState() {
        navigationState.value = SettingsNavigationState.StateLess
    }

    fun <T>update(value: T, key: String) {
        viewModelScope.launch {
            repository.update(value, key)
        }
    }
}