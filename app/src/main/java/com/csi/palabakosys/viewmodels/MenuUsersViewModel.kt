package com.csi.palabakosys.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.R
import com.csi.palabakosys.room.repository.UserRepository
import com.csi.palabakosys.adapters.Adapter
//import com.csi.palabakosys.adapters.RecyclerItem
import com.csi.palabakosys.room.entities.EntityUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuUsersViewModel
@Inject
constructor(
    private val repository: UserRepository
) : ViewModel() {
//    val adapter = Adapter<EntityUser>(R.layout.recycler_item_user_full)
    val user = MutableLiveData<EntityUser>()
    val isEmpty = MutableLiveData(false);

    init {
//        adapter.onItemClick = {
//            user.value = it.getItem()
//        }
    }

    fun getAll() {
        viewModelScope.launch {
            repository.getAll().let {
//                adapter.setData(it.map { _item -> RecyclerItem(_item) })
                isEmpty.value = it.isEmpty()
            }
        }
    }
}