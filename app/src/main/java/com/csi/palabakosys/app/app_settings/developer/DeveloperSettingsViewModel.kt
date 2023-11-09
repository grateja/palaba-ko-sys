package com.csi.palabakosys.app.app_settings.developer

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
    val fakeConnectionMode = MutableLiveData(appPreferenceRepository.testFakeConnect())

    fun updateFakeConnectionMode() {
        val fakeModeOn = fakeConnectionMode.value ?: false
        println("fake mode on")
        println(fakeModeOn)
        appPreferenceRepository.setFakeConnectionMode(fakeModeOn)
    }
}