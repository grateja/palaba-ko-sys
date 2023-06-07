package com.csi.palabakosys.app.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.app.preferences.user.AuthRepository
import com.csi.palabakosys.model.Rule
import com.csi.palabakosys.preferences.AppPreferenceRepository
import com.csi.palabakosys.room.repository.UserRepository
import com.csi.palabakosys.util.DataState
import com.csi.palabakosys.util.InputValidation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthDialogViewModel

@Inject
constructor(
    private val authRepository: AuthRepository
//    private val prefRepository: AppPreferenceRepository
): ViewModel() {
//    val currentUser = prefRepository.activeUser

    private val _dataState = MutableLiveData<DataState<LoginCredentials>>()
    val dataState: LiveData<DataState<LoginCredentials>> = _dataState

    private val _inputValidation = MutableLiveData(InputValidation())
    val validation: LiveData<InputValidation> = _inputValidation

    val email = MutableLiveData(authRepository.getLastEmail())
    val password = MutableLiveData("")

    fun clearError(key: String) {
        _inputValidation.value = _inputValidation.value?.removeError(key)
    }

    fun validate() {
        viewModelScope.launch {
            InputValidation().apply {
                val email = email.value
                val password = password.value

                this.addRules("email", email, arrayOf(Rule.Required, Rule.IsEmail(email)))
                this.addRules("password", password, arrayOf(Rule.Required))

                if(this.isInvalid()) {
                    _dataState.value = DataState.InvalidInput(this)
                } else {
                    authRepository.oneTimeLogin(email, password).let {
                        if(it != null) {
                            _dataState.value = DataState.Save(
                                LoginCredentials(it.email, it.password, it.id, it.name)
                            )
                        } else {
                            _dataState.value = DataState.Invalidate("Invalid login credentials")
                        }
                    }
                }

                _inputValidation.value = this
            }
        }
    }
}