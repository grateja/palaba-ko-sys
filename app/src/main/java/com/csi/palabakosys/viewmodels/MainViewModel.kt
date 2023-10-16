package com.csi.palabakosys.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csi.palabakosys.app.auth.LoginCredentials
import com.csi.palabakosys.app.main_menu.MenuItem
//import com.csi.palabakosys.adapters.MainMenuAdapter
//import com.csi.palabakosys.datastates.MainViewStateEvent
import com.csi.palabakosys.preferences.AppPreferenceRepository
import com.csi.palabakosys.room.dao.DaoPackage
import com.csi.palabakosys.room.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
    val navigationState = MutableLiveData<NavigationState>()

    private var menuItem: MenuItem? = null

    fun openMenu(menuItem: MenuItem) {
        this.menuItem = menuItem
        if(menuItem.permissions != null) {
            navigationState.value = NavigationState.RequestAuthentication(menuItem)
        } else {
            navigationState.value = NavigationState.OpenMenu(menuItem, null)
        }
    }

    fun permissionGranted(loginCredentials: LoginCredentials) {
        menuItem?.let {
            navigationState.value = NavigationState.OpenMenu(it, loginCredentials)
        }
    }

    fun resetState() {
        navigationState.value = NavigationState.StateLess
    }

    sealed class NavigationState {
        object StateLess: NavigationState()
        data class OpenMenu(val menuItem: MenuItem, val loginCredentials: LoginCredentials?) : NavigationState()
        data class RequestAuthentication(val menuItem: MenuItem) : NavigationState()
    }
}