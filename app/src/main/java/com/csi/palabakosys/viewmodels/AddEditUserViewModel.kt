//package com.csi.palabakosys.viewmodels
//
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.viewModelScope
//import com.csi.palabakosys.model.Role
//import com.csi.palabakosys.util.InputValidation
//import com.csi.palabakosys.model.Rule
//import com.csi.palabakosys.preferences.AppPreferenceRepository
//import com.csi.palabakosys.room.repository.UserRepository
//import com.csi.palabakosys.room.entities.EntityUser
//import com.csi.palabakosys.model.CRUDActionEnum
//import com.csi.palabakosys.util.DataState
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import java.util.UUID
//import javax.inject.Inject
//
//@HiltViewModel
//class AddEditUserViewModel
//@Inject
//constructor(
//    private val userRepository: UserRepository,
//    private val preferenceRepository: AppPreferenceRepository
//) : CreateViewModel<EntityUser>(userRepository)
//{
//    val password = MutableLiveData<String>()
//    val retypePassword = MutableLiveData<String>()
//
////    fun getAdmin() {
////        model.value.let { userModel ->
////            if(userModel != null) return
////
////            viewModelScope.launch {
////                userRepository.getAdmin()?.let {
////                    model.value = it
////                    dataState.value = DataState.Loaded(it)
////                    crudActionEnum.value = CRUDActionEnum.UPDATE
////                    return@launch
////                }
////                super.get(null, EntityUser(Role.ADMIN))
////            }
////        }
////    }
//
////    fun getUser(id: UUID?) {
////        viewModelScope.launch {
////            super.get(id, EntityUser(Role.STAFF))
////        }
////    }
//
//    fun saveUser(isAdmin: Boolean) {
//        model.value?.let { user ->
//            val inputValidation = InputValidation()
//            inputValidation.addRules("name", user.name.toString(), arrayOf(Rule.Required))
//            inputValidation.addRules("email", user.email.toString(), arrayOf(Rule.Required, Rule.IsEmail(user.email)))
//
//            crudActionEnum.value?.let {
//                if(it == CRUDActionEnum.CREATE) {
//                    user.password = password.value.toString()
//                    inputValidation.addRules("password", password.value.toString(), arrayOf(Rule.Required))
//                    inputValidation.addRules("retypePassword", retypePassword.value.toString(),
//                        arrayOf(
//                            Rule.Required,
//                            Rule.Matched(password.value.toString(), "Retype password do not matched")))
//                }
//            }
//
//            if(inputValidation.isInvalid()) {
//                validation.value = inputValidation
//                return@let
//            }
//
//            viewModelScope.launch {
//                userRepository.save(user)?.let { _user ->
//                    model.value = _user
//                    dataState.value = DataState.Save(_user)
//                    if(isAdmin || preferenceRepository.getId() == _user.id) {
//                        preferenceRepository.setCurrentUser(_user)
//                    }
//                }
//            }
//        }
//    }
//}