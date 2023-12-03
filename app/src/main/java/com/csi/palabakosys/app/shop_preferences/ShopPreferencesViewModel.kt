package com.csi.palabakosys.app.shop_preferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.room.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopPreferencesViewModel

@Inject
constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    private val _dataState = MutableLiveData<DataState>()
    val dataState: LiveData<DataState> = _dataState

    val shopName = dataStoreRepository.shopName
    val address = dataStoreRepository.address
    val contactNumber = dataStoreRepository.contactNumber
    val email = dataStoreRepository.email

    fun updateShopName(shopName: String?) {
        viewModelScope.launch {
            dataStoreRepository.updateShopName(shopName)
        }
    }

    fun showEditShopName() {
        shopName.value.let {
            _dataState.value = DataState.ShowEditShopName(it)
        }
    }

    fun updateAddress(address: String?) {
        viewModelScope.launch {
            dataStoreRepository.updateAddress(address)
        }
    }

    fun showEditAddress() {
        address.value.let {
            _dataState.value = DataState.ShowEditAddress(it)
        }
    }

    fun updateContactNumber(contactNumber: String?) {
        viewModelScope.launch {
            dataStoreRepository.updateContactNumber(contactNumber)
        }
    }

    fun showEditContactNumber() {
        contactNumber.value.let {
            _dataState.value = DataState.ShowEditContactNumber(it)
        }
    }

    fun updateEmail(email: String?) {
        viewModelScope.launch {
            dataStoreRepository.updateEmail(email)
        }
    }

    fun showEditEmail() {
        email.value.let {
            _dataState.value = DataState.ShowEditEmail(it)
        }
    }

    fun resetState() {
        _dataState.value = DataState.StateLess
    }

    sealed class DataState {
        object StateLess: DataState()
        data class ShowEditShopName(val text: String?): DataState()
        data class ShowEditAddress(val text: String?): DataState()
        data class ShowEditContactNumber(val text: String?): DataState()
        data class ShowEditEmail(val text: String?): DataState()
    }
}