package com.csi.palabakosys.app.auth

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.app.app_settings.user.AuthRepository
import com.csi.palabakosys.model.EnumActionPermission
import com.csi.palabakosys.model.EnumAuthMethod
import com.csi.palabakosys.model.Rule
import com.csi.palabakosys.room.repository.UserRepository
import com.csi.palabakosys.util.DataState
import com.csi.palabakosys.util.InputValidation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class AuthDialogViewModel

@Inject
constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
): ViewModel() {
    private val _authMethod = MutableLiveData(EnumAuthMethod.AUTH_BY_PATTERN)
    val authMethod: LiveData<EnumAuthMethod> = _authMethod

    private val _dataState = MutableLiveData<DataState<LoginCredentials>>()
    val dataState: LiveData<DataState<LoginCredentials>> = _dataState

    private val _inputValidation = MutableLiveData(InputValidation())
    val validation: LiveData<InputValidation> = _inputValidation

    val userName = MutableLiveData(authRepository.getLastEmail())
    val password = MutableLiveData("")

    private val _permissions = MutableLiveData<List<EnumActionPermission>>()
    val permissions: LiveData<List<EnumActionPermission>> = _permissions

    val emails = userRepository.getAllEmails()

    fun setAuthMethod(authMethod: EnumAuthMethod) {
        _authMethod.value = authMethod
    }

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

    fun validate(method: AuthMethod) {
        viewModelScope.launch {
            InputValidation().apply {
                val password = password.value
                val email = userName.value

                this.addRule("userName", email, arrayOf(Rule.Required, Rule.IsEmail))
                if(method is AuthMethod.AuthByPassword) {
                    this.addRule("password", password, arrayOf(Rule.Required))
                }

                if(this.isInvalid()) {
                    _dataState.value = DataState.InvalidInput(this)
                } else {
                    val user = when(method) {
                        is AuthMethod.AuthByPassword -> {
                            authRepository.oneTimeLogin(email, password)
                        }
                        is AuthMethod.AuthByPattern -> {
                            authRepository.oneTimeLogin(email, method.pattern)
                        }
                    }

                    user.let {
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

    sealed class AuthMethod {
        object AuthByPassword : AuthMethod()
        data class AuthByPattern(val pattern: ArrayList<Int>) : AuthMethod()
    }
}