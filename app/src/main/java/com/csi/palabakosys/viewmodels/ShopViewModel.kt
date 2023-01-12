package com.csi.palabakosys.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.model.Rule
import com.csi.palabakosys.room.entities.EntityShop
import com.csi.palabakosys.room.repository.ShopRepository
import com.csi.palabakosys.util.DataState
import com.csi.palabakosys.util.InputValidation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel
@Inject
constructor(
    private val shopRepository: ShopRepository
) : ViewModel()
{
    val validation = MutableLiveData(InputValidation())
    val mutableShop = MutableLiveData(EntityShop())

    private val _dataState: MutableLiveData<DataState<EntityShop>> = MutableLiveData()
    val dataState: LiveData<DataState<EntityShop>>
        get() = _dataState

    fun clearError(key: String) {
        validation.value = validation.value?.removeError(key)
    }

    fun getShop() {
        viewModelScope.launch {
            val shop = shopRepository.get()
            if(shop != null) {
                mutableShop.value = shop
            }
        }
    }

    fun save() {
        mutableShop.value?.let {
            val inputValidation = InputValidation()
            inputValidation.addRules("name", it.name.toString(), arrayOf(Rule.REQUIRED))
            if(inputValidation.isInvalid()) {
                validation.value = inputValidation
                return@let
            }
            viewModelScope.launch {
                shopRepository.save(it)
                mutableShop.value = it
                _dataState.value = DataState.Success(it)
            }
        }
    }
}