package com.csi.palabakosys.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
//import com.csi.palabakosys.adapters.MainMenuAdapter
//import com.csi.palabakosys.datastates.MainViewStateEvent
import com.csi.palabakosys.preferences.AppPreferenceRepository
import com.csi.palabakosys.room.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class MainViewModel
@Inject
constructor(
    appPreferenceRepository: AppPreferenceRepository,
    private val userRepository: UserRepository
) : ViewModel()
{
//    val menuAdapter = MainMenuAdapter()
//    private val _dataState: MutableLiveData<MainViewStateEvent> = MutableLiveData()
//    val dataState: LiveData<MainViewStateEvent>
//        get() = _dataState

    private val activeUser = appPreferenceRepository.activeUser

    fun checkAll() {
        viewModelScope.launch {
            val admin = userRepository.getAdmin()

            if(admin == null) {
//                _dataState.value = MainViewStateEvent.NoAdminFound
            }

            if(admin != null) {
                activeUser.value.let {
//                    if(it == null) {
//                        _dataState.value = MainViewStateEvent.LoginRequired
//                    } else {
//                        _dataState.value = MainViewStateEvent.AllSet(it)
//                        menuAdapter.setRole(it.role)
//                    }
                }
            }
        }
    }
}