package com.csi.palabakosys.app.expenses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.room.entities.EntityExpense
import com.csi.palabakosys.room.repository.ExpensesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel

@Inject
constructor(
    private val expensesRepository: ExpensesRepository
) : ViewModel() {
    private val _expenses = MutableLiveData<List<EntityExpense>>()
    val expenses: LiveData<List<EntityExpense>> = _expenses

    val keyword = MutableLiveData("")

    fun filter() {
        viewModelScope.launch {
            keyword.value?.let {
                _expenses.value = expensesRepository.getAll(it)
            }
        }
    }

    fun setKeyword(keyword: String?) {
        this.keyword.value = keyword
        this.filter()
    }
}