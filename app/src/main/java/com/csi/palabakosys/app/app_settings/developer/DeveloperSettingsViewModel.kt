package com.csi.palabakosys.app.app_settings.developer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csi.palabakosys.preferences.AppPreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeveloperSettingsViewModel
@Inject
constructor(
    private val appPreferenceRepository: AppPreferenceRepository
): ViewModel() {
    private val _navigationState = MutableLiveData<NavigationState>()
    val navigationState: LiveData<NavigationState> = _navigationState

    val fakeConnectionMode = MutableLiveData(appPreferenceRepository.testFakeConnect())
    val fakeConnectionDelay = MutableLiveData(appPreferenceRepository.testFakeDelay())

    fun updateFakeConnectionMode() {
        val fakeModeOn = fakeConnectionMode.value ?: false
        appPreferenceRepository.setFakeConnectionMode(fakeModeOn)
    }

    fun updateFakeConnectionDelay(msDelay: Long) {
        appPreferenceRepository.setFakeConnectionDelay(msDelay)
    }

    fun clearState() {
        _navigationState.value = NavigationState.StateLess
    }

    fun openConnectionDelayDialog() {
        val msDelay = fakeConnectionDelay.value ?: 1000
        _navigationState.value = NavigationState.OpenConnectionDelay(msDelay)
    }

    sealed class NavigationState {
        object StateLess : NavigationState()
        data class OpenConnectionDelay(val msDelay: Long): NavigationState()
    }
}