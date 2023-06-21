package com.csi.palabakosys.app.extras

import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.room.repository.ExtrasRepository
import com.csi.palabakosys.viewmodels.ListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExtrasViewModel

@Inject
constructor(
    private val repository: ExtrasRepository
) : ListViewModel<ExtrasItemFull>() {

    override fun filter(reset: Boolean) {
        job?.let {
            it.cancel()
            loading.value = false
        }

        job = viewModelScope.launch {
            loading.value = true
            delay(500)
            keyword.value?.let {
                items.value = repository.filter(it)
            }
        }
    }
}