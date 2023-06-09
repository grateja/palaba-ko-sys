package com.csi.palabakosys.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.room.entities.BaseEntity
import com.csi.palabakosys.room.repository.IRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

open class ListViewModel<T> : ViewModel() {
    val items = MutableLiveData<List<T>>()
    val keyword = MutableLiveData("")

    protected val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading
    protected var job: Job? = null
}