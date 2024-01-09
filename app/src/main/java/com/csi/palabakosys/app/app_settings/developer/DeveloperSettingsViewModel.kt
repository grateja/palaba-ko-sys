package com.csi.palabakosys.app.app_settings.developer

import com.csi.palabakosys.app.app_settings.SettingsViewModel
//import com.csi.palabakosys.room.repository.DataStoreRepository
import com.csi.palabakosys.settings.BaseSettingsRepository
import com.csi.palabakosys.settings.DeveloperSettingsRepository
import com.csi.palabakosys.util.SettingsNavigationState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeveloperSettingsViewModel
@Inject
constructor(
    private val repository: DeveloperSettingsRepository
): SettingsViewModel(repository) {
    val fakeConnectionMode = repository.fakeConnectionMode
    val fakeConnectionDelay = repository.fakeConnectionDelay

    val prefix = repository.prefix
    val subnet = repository.subnet
    val endpoint = repository.endpoint
    val timeout = repository.timeout

    fun showEditPrefix() {
        (prefix.value ?: "").let {
            navigationState.value = SettingsNavigationState.OpenStringSettings(it, DeveloperSettingsRepository.MACHINE_IP_PREFIX, "IP Address Prefix", null)
        }
    }

    fun showEditSubnetMask() {
        (subnet.value ?: "").let {
            navigationState.value = SettingsNavigationState.OpenStringSettings(it, DeveloperSettingsRepository.MACHINE_IP_SUBNET_MASK, "IP Address Subnet Mask", null)
        }
    }

    fun showEditEndpoint() {
        (endpoint.value ?: "").let {
            navigationState.value = SettingsNavigationState.OpenStringSettings(it, DeveloperSettingsRepository.MACHINE_ACTIVATION_ENDPOINT, "Machine activation end point", null)
        }
    }

    fun showEditTimeout() {
        (timeout.value ?: 15L).let {
            navigationState.value = SettingsNavigationState.OpenLongSettings(it, DeveloperSettingsRepository.MACHINE_ACTIVATION_TIMEOUT, "Machine activation timeout", null)
        }
    }

    fun openConnectionDelayDialog() {
        val msDelay = fakeConnectionDelay.value ?: 1000
        navigationState.value = SettingsNavigationState.OpenLongSettings(
            msDelay,
            DeveloperSettingsRepository.DEVELOPER_ACTIVATION_DELAY,
            "Activation Delay", "Delay intervals in milliseconds")
    }
}