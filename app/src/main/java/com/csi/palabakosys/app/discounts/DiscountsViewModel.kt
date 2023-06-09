package com.csi.palabakosys.app.discounts

import androidx.lifecycle.*
import com.csi.palabakosys.room.entities.EntityDiscount
import com.csi.palabakosys.room.repository.DiscountsRepository
import com.csi.palabakosys.room.repository.ExpensesRepository
import com.csi.palabakosys.viewmodels.ListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscountsViewModel

@Inject
constructor(
    private val repository: DiscountsRepository
) : ListViewModel<EntityDiscount>() {

    fun setKeyword(keyword: String?) {
        this.keyword.value = keyword
        filter()
    }

    fun filter() {
        job?.let {
            it.cancel()
            _loading.value = false
        }

        job = viewModelScope.launch {
            _loading.value = true
            delay(500)
            keyword.value?.let {
                items.value = repository.filter(it)
            }
        }
    }
}