package com.csi.palabakosys.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.util.InputValidation
import com.csi.palabakosys.model.Rule
import com.csi.palabakosys.preferences.AppPreferenceRepository
import com.csi.palabakosys.room.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LoginViewModel

@Inject
constructor(
    private val userRepository: UserRepository,
    private val appPreferenceRepository: AppPreferenceRepository
) : ViewModel()
{
    val validation = MutableLiveData(InputValidation())
    val email: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()

//    private val _dataSate: MutableLiveData<LoginViewModelState> = MutableLiveData()
//    val dataState: LiveData<LoginViewModelState>
//        get() = _dataSate

    fun clearError(key: String) {
        validation.value = validation.value?.removeError(key)
    }

    fun login() {
        val inputValidation = InputValidation()
        inputValidation.addRules("email", email.value.toString(), arrayOf(Rule.REQUIRED))
        inputValidation.addRules("password", password.value.toString(), arrayOf(Rule.REQUIRED))
        if(inputValidation.isInvalid()) {
            validation.value = inputValidation
            return
        }

        viewModelScope.launch {
            try {
                val user = userRepository.getByEmail(email.value.toString())

                if(user == null) {
                    inputValidation.addError("email", "Email not registered")
                } else if(user.password != password.value) {
                    inputValidation.addError("password", "Incorrect password")
                }

                if(inputValidation.isInvalid()) {
                    validation.value = inputValidation
                } else {
                    user?.let {
                        appPreferenceRepository.setCurrentUser(it)
//                        _dataSate.value = LoginViewModelState.Success(it)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
