package com.csi.palabakosys.app.products

import androidx.lifecycle.*
import com.csi.palabakosys.room.entities.EntityProduct
import com.csi.palabakosys.room.repository.ProductRepository
import com.csi.palabakosys.viewmodels.ListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel

@Inject
constructor(
    private val repository: ProductRepository
) : ListViewModel<ProductItemFull>() {

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