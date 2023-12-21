package com.csi.palabakosys.app.app_settings.developer

import com.csi.palabakosys.app.app_settings.SettingsViewModel
import com.csi.palabakosys.room.repository.DataStoreRepository
import com.csi.palabakosys.util.SettingsNavigationState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeveloperSettingsViewModel
@Inject
constructor(
    private val repository: DataStoreRepository
): SettingsViewModel(repository) {
    val fakeConnectionMode = repository.getBooleanAsLiveData(DataStoreRepository.DEVELOPER_FAKE_CONNECTION_MODE_ON, false)
    val fakeConnectionDelay = repository.getLongAsLiveData(DataStoreRepository.DEVELOPER_ACTIVATION_DELAY, 3000) //MutableLiveData(appPreferenceRepository.testFakeDelay())

//    fun updateFakeConnectionMode() {
//        val fakeModeOn = fakeConnectionMode.value ?: false
//        appPreferenceRepository.setFakeConnectionMode(fakeModeOn)
//    }

//    fun updateFakeConnectionDelay(msDelay: Long) {
//        appPreferenceRepository.setFakeConnectionDelay(msDelay)
//    }

//    fun clearState() {
//        _navigationState.value = SettingsNavigationState.StateLess
//    }

    fun openConnectionDelayDialog() {
        val msDelay = fakeConnectionDelay.value ?: 1000
        navigationState.value = SettingsNavigationState.OpenLongSettings(
            msDelay,
            DataStoreRepository.DEVELOPER_ACTIVATION_DELAY,
            "Activation Delay", "Delay intervals in milliseconds")
    }
//
//    sealed class NavigationState {
//        object StateLess : NavigationState()
//        data class OpenConnectionDelay(val msDelay: Long): NavigationState()
//    }
}