package com.csi.palabakosys.app.app_settings.ip

import com.csi.palabakosys.app.app_settings.SettingsViewModel
import com.csi.palabakosys.room.repository.DataStoreRepository
import com.csi.palabakosys.util.SettingsNavigationState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IPAddressSettingsViewModel

@Inject
constructor(
    private val repository: DataStoreRepository
) : SettingsViewModel(repository) {
    val prefix = repository.getStringAsLiveData(DataStoreRepository.MACHINE_IP_PREFIX, "192.168")
    val subnet = repository.getStringAsLiveData(DataStoreRepository.MACHINE_IP_SUBNET_MASK, "210")
    val endpoint = repository.getStringAsLiveData(DataStoreRepository.MACHINE_ACTIVATION_ENDPOINT, "activate")
    val timeout = repository.getLongAsLiveData(DataStoreRepository.MACHINE_ACTIVATION_TIMEOUT, 5)

    fun showEditPrefix() {
        (prefix.value ?: "").let {
            navigationState.value = SettingsNavigationState.OpenStringSettings(it, DataStoreRepository.MACHINE_IP_PREFIX, "IP Address Prefix", null)
        }
    }

    fun showEditSubnetMask() {
        (subnet.value ?: "").let {
            navigationState.value = SettingsNavigationState.OpenStringSettings(it, DataStoreRepository.MACHINE_IP_SUBNET_MASK, "IP Address Subnet Mask", null)
        }
    }

    fun showEditEndpoint() {
        (endpoint.value ?: "").let {
            navigationState.value = SettingsNavigationState.OpenStringSettings(it, DataStoreRepository.MACHINE_ACTIVATION_ENDPOINT, "Machine activation end point", null)
        }
    }

    fun showEditTimeout() {
        (timeout.value ?: 15).let {
            navigationState.value = SettingsNavigationState.OpenLongSettings(it, DataStoreRepository.MACHINE_ACTIVATION_TIMEOUT, "Machine activation timeout", null)
        }
    }

//    val ipSettings = MutableLiveData(preferenceRepository.ipSettings)
//    val urlSettings = MutableLiveData(preferenceRepository.urlSettings)

//    fun save() {
//        ipSettings.value?.let {
//            preferenceRepository.saveIpSettings(it)
//        }
//        urlSettings.value?.let {
//            preferenceRepository.saveUrlSettings(it)
//        }
//    }
}