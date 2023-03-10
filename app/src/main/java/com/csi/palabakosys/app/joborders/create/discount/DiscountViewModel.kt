package com.csi.palabakosys.app.joborders.create.discount

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.model.DiscountApplicable
import com.csi.palabakosys.model.DiscountTypeEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscountViewModel

@Inject
constructor() : ViewModel()
{
    val discounts = MutableLiveData<List<MenuDiscount>>()
    val discount = MutableLiveData<MenuDiscount?>()

    fun setDiscount(discount: MenuDiscount?) {
        discounts.value?.onEach {
            if(it.selected) {
                it.selected = false
            }
        }
        this.discount.value = discounts.value?.find { it.id == discount?.id }?.apply {
            selected = true
        }
    }

    fun loadDiscounts() {
        viewModelScope.launch {
            discounts.value = listOf(
                MenuDiscount("birthday", "Birthday", 5f, DiscountTypeEnum.PERCENTAGE, listOf(DiscountApplicable.WASH_DRY_SERVICES)),
                MenuDiscount("pwd", "PWD", 5f, DiscountTypeEnum.PERCENTAGE, listOf(DiscountApplicable.DELIVERY)),
                MenuDiscount("senior", "Senior", 5f, DiscountTypeEnum.PERCENTAGE, listOf(DiscountApplicable.TOTAL_AMOUNT)),
                MenuDiscount("opening", "Opening", 10f, DiscountTypeEnum.FIXED, listOf(DiscountApplicable.WASH_DRY_SERVICES, DiscountApplicable.PRODUCTS_CHEMICALS)),
            )
        }
    }
}