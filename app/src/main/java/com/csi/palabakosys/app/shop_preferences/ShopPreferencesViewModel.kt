package com.csi.palabakosys.app.shop_preferences

import com.csi.palabakosys.app.app_settings.SettingsViewModel
import com.csi.palabakosys.room.repository.DataStoreRepository
import com.csi.palabakosys.util.SettingsNavigationState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShopPreferencesViewModel

@Inject
constructor(
    private val dataStoreRepository: DataStoreRepository
) : SettingsViewModel(dataStoreRepository) {
//    private val _dataState = MutableLiveData<DataState>()
//    val dataState: LiveData<DataState> = _dataState

    val shopName = dataStoreRepository.getStringAsLiveData(DataStoreRepository.SHOP_NAME)
    val address = dataStoreRepository.getStringAsLiveData(DataStoreRepository.ADDRESS)
    val contactNumber = dataStoreRepository.getStringAsLiveData(DataStoreRepository.CONTACT_NUMBER)
    val email = dataStoreRepository.getStringAsLiveData(DataStoreRepository.EMAIL)

//    fun updateShopName(shopName: String?) {
//        viewModelScope.launch {
//            dataStoreRepository.updateShopName(shopName)
//        }
//    }

    fun showEditShopName() {
        (shopName.value ?: "").let {
            navigationState.value = SettingsNavigationState.OpenStringSettings(it, DataStoreRepository.SHOP_NAME,"Edit shop name", "")
//            _dataState.value = DataState.ShowEditShopName(it)
        }
    }

//    fun updateAddress(address: String?) {
//        viewModelScope.launch {
//            dataStoreRepository.updateAddress(address)
//        }
//    }

    fun showEditAddress() {
        (address.value ?: "").let {
            navigationState.value = SettingsNavigationState.OpenStringSettings(it, DataStoreRepository.ADDRESS,"Edit shop address", "")
//            _dataState.value = DataState.ShowEditAddress(it)
        }
    }

//    fun updateContactNumber(contactNumber: String?) {
//        viewModelScope.launch {
//            dataStoreRepository.updateContactNumber(contactNumber)
//        }
//    }

    fun showEditContactNumber() {
        (contactNumber.value ?: "").let {
            navigationState.value = SettingsNavigationState.OpenStringSettings(it, DataStoreRepository.CONTACT_NUMBER,"Edit shop contact number", "")
//            _dataState.value = DataState.ShowEditContactNumber(it)
        }
    }

//    fun updateEmail(email: String?) {
//        viewModelScope.launch {
//            dataStoreRepository.updateEmail(email)
//        }
//    }

    fun showEditEmail() {
        (email.value ?: "").let {
            navigationState.value = SettingsNavigationState.OpenStringSettings(it, DataStoreRepository.EMAIL,"Edit shop email", "")
//            _dataState.value = DataState.ShowEditEmail(it)
        }
    }

//    fun resetState() {
//        _dataState.value = DataState.StateLess
//    }
//
//    sealed class DataState {
//        object StateLess: DataState()
//        data class ShowEditShopName(val text: String?): DataState()
//        data class ShowEditAddress(val text: String?): DataState()
//        data class ShowEditContactNumber(val text: String?): DataState()
//        data class ShowEditEmail(val text: String?): DataState()
//    }
}