package com.csi.palabakosys.app.preferences.ip

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

    fun save() {
        ipSettings.value?.let {
            preferenceRepository.saveIpSettings(it)
        }
    }
}