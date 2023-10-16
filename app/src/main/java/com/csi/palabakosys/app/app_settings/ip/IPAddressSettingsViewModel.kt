package com.csi.palabakosys.app.app_settings.ip

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csi.palabakosys.preferences.AppPreferenceRepository
import com.csi.palabakosys.preferences.IpSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IPAddressSettingsViewModel

@Inject
constructor(private val preferenceRepository: AppPreferenceRepository)
: ViewModel() {
    val ipSettings = MutableLiveData(preferenceRepository.ipSettings)
    val urlSettings = MutableLiveData(preferenceRepository.urlSettings)

    fun save() {
        ipSettings.value?.let {
            preferenceRepository.saveIpSettings(it)
        }
        urlSettings.value?.let {
            preferenceRepository.saveUrlSettings(it)
        }
    }
}