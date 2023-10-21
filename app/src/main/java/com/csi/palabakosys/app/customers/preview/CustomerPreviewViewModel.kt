package com.csi.palabakosys.app.customers.preview

import androidx.lifecycle.*
import com.csi.palabakosys.room.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CustomerPreviewViewModel

@Inject
constructor(
    private val repository: CustomerRepository
) : ViewModel() {
    private val _customerId = MutableLiveData<UUID>()
    val customer = _customerId.switchMap { repository.getCustomerAsLiveData(it) }

    private val _navigationState = MutableLiveData<NavigationState>()
    val navigationState: LiveData<NavigationState> = _navigationState

    fun load(customerId: UUID) {
        _customerId.value = customerId
    }

    fun showCustomer() {
        _customerId.value?.let {
            _navigationState.value = NavigationState.EditCustomer(it)
        }
    }

    fun resetState() {
        _navigationState.value = NavigationState.StateLess
    }

    fun prepareNewJobOrder() {
        _customerId.value?.let {
            _navigationState.value = NavigationState.PrepareNewJobOrder(it)
        }
    }

    sealed class NavigationState {
        object StateLess: NavigationState()
        data class EditCustomer(val customerId: UUID): NavigationState()
        data class PrepareNewJobOrder(val customerId: UUID): NavigationState()
    }
}