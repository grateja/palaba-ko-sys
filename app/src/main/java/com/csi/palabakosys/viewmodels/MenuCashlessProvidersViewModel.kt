package com.csi.palabakosys.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.R
import com.csi.palabakosys.adapters.Adapter
//import com.csi.palabakosys.adapters.RecyclerItem
import com.csi.palabakosys.room.entities.EntityCashlessProvider
import com.csi.palabakosys.room.repository.CashlessProvidersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuCashlessProvidersViewModel

@Inject
constructor(
    private val repository: CashlessProvidersRepository
) : ViewModel() {

//    val adapter = Adapter<EntityCashlessProvider>(R.layout.recycler_item_cashless_provider)
    val isEmpty = MutableLiveData(false);
//    val customer = MutableLiveData<EntityCashlessProvider>()

//    init {
//        adapter.onItemClick = {
//            customer.value = it
//        }
//    }

    fun getAll(keyword: String?) {
        viewModelScope.launch {
            repository.getAll(keyword ?: "").let { _list ->
//                adapter.setData(_list.map { RecyclerItem(it) })
                isEmpty.value = _list.isEmpty()
            }
        }
    }

}