package com.csi.palabakosys.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csi.palabakosys.app.customers.list.CustomerListItem
import com.csi.palabakosys.util.EnumSortDirection
import com.csi.palabakosys.util.ListViewModelInterface
import kotlinx.coroutines.Job

abstract class ListViewModel<T> : ViewModel(), ListViewModelInterface {
    protected val _dataState = MutableLiveData<DataState<T>>()
    val dataState: LiveData<DataState<T>> = _dataState

    override val keyword = MutableLiveData("")

    val items = MutableLiveData<List<T>>()
    val orderBy = MutableLiveData("created_at")
    val sortDirection = MutableLiveData(EnumSortDirection.DESC)
    val page = MutableLiveData(1)
    val loading = MutableLiveData(false)

    protected var job: Job? = null

    sealed class DataState<out R> {
        data class LoadItems<R>(val items: List<R>, val reset: Boolean) : DataState<R>()
    }
}