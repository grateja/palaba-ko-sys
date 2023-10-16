package com.csi.palabakosys.app.expenses

import androidx.lifecycle.*
import com.csi.palabakosys.app.dashboard.data.DateFilter
import com.csi.palabakosys.room.repository.ExpensesRepository
import com.csi.palabakosys.viewmodels.ListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel

@Inject
constructor(
    private val repository: ExpensesRepository
) : ListViewModel<ExpenseItemFull>() {
    private val _dateFilter = MutableLiveData<DateFilter>()

    override fun filter(reset: Boolean) {
        job?.let {
            it.cancel()
            loading.value = false
        }

        job = viewModelScope.launch {
            loading.value = true
            delay(500)
            keyword.value?.let {
                items.value = repository.filter(it, _dateFilter.value)
            }
        }
    }

    fun setDates(dateFilter: DateFilter) {
        _dateFilter.value = dateFilter
    }
}