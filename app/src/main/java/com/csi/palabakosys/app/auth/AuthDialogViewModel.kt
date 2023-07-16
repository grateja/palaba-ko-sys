package com.csi.palabakosys.app.auth

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.app.preferences.user.AuthRepository
import com.csi.palabakosys.model.EnumActionPermission
import com.csi.palabakosys.model.Rule
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
): ViewModel() {
    private val _dataState = MutableLiveData<DataState<LoginCredentials>>()
    val dataState: LiveData<DataState<LoginCredentials>> = _dataState

    private val _inputValidation = MutableLiveData(InputValidation())
    val validation: LiveData<InputValidation> = _inputValidation

    val email = MutableLiveData(authRepository.getLastEmail())
    val password = MutableLiveData("")

    private val _permissions = MutableLiveData<List<EnumActionPermission>>()
    val permissions: LiveData<List<EnumActionPermission>> = _permissions

    fun clearError(key: String) {
        _inputValidation.value = _inputValidation.value?.removeError(key)
    }

    fun setPermissions(permissions: List<EnumActionPermission>) {
        _permissions.value = permissions
    }

    @SuppressLint("Get all the permissions not present on user privileges")
    private fun checkPermissions(userPrivileges: List<EnumActionPermission>) : List<EnumActionPermission> {
        if(userPrivileges.contains(EnumActionPermission.ALL)) return emptyList()

        val result = mutableListOf<EnumActionPermission>()

        _permissions.value?.let { permissions ->
            for (item in permissions) {
                if (!userPrivileges.contains(item)) {
                    result.add(item)
                }
            }
        }
        return result
    }

    fun validate() {
        viewModelScope.launch {
            InputValidation().apply {
                val email = email.value
                val password = password.value

                this.addRule("email", email, arrayOf(Rule.Required, Rule.IsEmail))
                this.addRule("password", password, arrayOf(Rule.Required))

                if(this.isInvalid()) {
                    _dataState.value = DataState.InvalidInput(this)
                } else {
                    authRepository.oneTimeLogin(email, password).let {
                        if(it != null) {
                            val deniedPermissions = checkPermissions(it.permissions)
                            if(deniedPermissions.isNotEmpty()) {
                                _dataState.value = DataState.Invalidate("You do not have the necessary permissions to perform this action.")
                            } else {
                                _dataState.value = DataState.SaveSuccess(
                                    LoginCredentials(it.email, it.password, it.id, it.name)
                                )
                            }
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