package com.csi.palabakosys.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
//import com.csi.palabakosys.adapters.MainMenuAdapter
//import com.csi.palabakosys.datastates.MainViewStateEvent
import com.csi.palabakosys.preferences.AppPreferenceRepository
import com.csi.palabakosys.room.dao.DaoPackage
import com.csi.palabakosys.room.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel

class MainViewModel
@Inject
constructor(
    appPreferenceRepository: AppPreferenceRepository,
    private val userRepository: UserRepository,
    private val daoPackage: DaoPackage
) : ViewModel()
{
//    val menuAdapter = MainMenuAdapter()
//    private val _dataState: MutableLiveData<MainViewStateEvent> = MutableLiveData()
//    val dataState: LiveData<MainViewStateEvent>
//        get() = _dataState

    private val activeUser = appPreferenceRepository.activeUser
    init {

        viewModelScope.launch {
            println("get package")
            daoPackage.getByIds(
                listOf(
                    UUID.fromString("7c039191-74d4-4d49-8b23-734128859164"),
                    UUID.fromString("504337e7-49bc-4f1b-8fa4-18b66946b8c2")
                )
            ).forEach {
                println(it.prePackage.packageName)
                it.services?.forEach {
                    println("quantity")
                    println(it.serviceCrossRef.quantity)
                    println(it.service.name)
                }
            }
//            val ids = listOf(
//                UUID.fromString("6901e100-a310-40a1-bd11-37edf6b1935c"),
//                UUID.fromString("60044d82-aadb-4e2f-99b9-163628173fa1"),
//                UUID.fromString("8344d137-cdb0-4499-aa44-b0ab5b939d9b"),
//                UUID.fromString("82da685e-0b6c-4b95-9d53-d92fc214632e"),
//            )
//            println("test package")
//            daoPackage.getServicesByIds(ids).forEach {
//                println(it.name)
//                println(it)
//            }
        }
    }

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