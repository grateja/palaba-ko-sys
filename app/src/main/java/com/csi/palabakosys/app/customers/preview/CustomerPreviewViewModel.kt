package com.csi.palabakosys.app.customers.preview

import androidx.lifecycle.*
import com.csi.palabakosys.room.repository.CustomerRepository
import com.csi.palabakosys.room.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CustomerPreviewViewModel

@Inject
constructor(
    private val repository: CustomerRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    private val unpaidCount = MutableLiveData<Int>()

    private val _customerId = MutableLiveData<UUID>()
    val customer = _customerId.switchMap { repository.getCustomerAsLiveData(it) }

    private val _navigationState = MutableLiveData<NavigationState>()
    val navigationState: LiveData<NavigationState> = _navigationState

    private val maxUnpaidJobOrder = dataStoreRepository.jobOrderSettingsMaxUnpaid
    private val argument = MediatorLiveData<Pair<UUID, Int>>().apply {
        fun update() {
            val customerId = _customerId.value
            val limit = maxUnpaidJobOrder.value ?: 0
            if(customerId != null) {
                value = Pair(customerId, limit)
            }
        }
        addSource(_customerId) {update()}
        addSource(maxUnpaidJobOrder) {update()}
    }

    val canCreateJobOrder = argument.switchMap {
        val customerId = it.first
        val limit = it.second
        repository.canCreateJobOrder(customerId, limit)
    }

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