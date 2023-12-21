package com.csi.palabakosys.app.app_settings.job_orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.app.app_settings.SettingsViewModel
//import com.csi.palabakosys.room.repository.DataStoreRepository
import com.csi.palabakosys.settings.JobOrderSettingsRepository
import com.csi.palabakosys.util.SettingsNavigationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppSettingsJobOrdersViewModel

@Inject
constructor(
    private val repository: JobOrderSettingsRepository
): SettingsViewModel(repository) {
    val jobOrderMaxUnpaid = repository.maxUnpaidJobOrderLimit
    val requireOrNumber = repository.requireOrNumber

    fun updateRequireOrNumber(value: Boolean) {
        viewModelScope.launch {
            repository.updateRequireOrNumber(value)
        }
    }

//    private val _settingsNavigationState = MutableLiveData<SettingsNavigationState>()
//    val settingsNavigationState: LiveData<SettingsNavigationState> = _settingsNavigationState
//
//    fun resetState() {
//        _settingsNavigationState.value = SettingsNavigationState.StateLess
//    }
//
//    fun <T>update(value: T, key: String) {
//        viewModelScope.launch {
//            dataStoreRepository.update(value, key)
//        }
//    }

    fun showMaxUnpaidJobOrder() {
        jobOrderMaxUnpaid.value.let {
            navigationState.value = SettingsNavigationState.OpenIntSettings(it as Int,
                JobOrderSettingsRepository.JOB_ORDER_MAX_UNPAID,
                "Max unpaid JO",
                "Enter the maximum allowed number of unpaid Job Order per customer")
        }
    }
}