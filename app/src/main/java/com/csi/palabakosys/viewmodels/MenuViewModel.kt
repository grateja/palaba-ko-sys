package com.csi.palabakosys.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csi.palabakosys.preferences.AppPreferenceRepository
import com.csi.palabakosys.room.entities.EntityUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuViewModel
@Inject
constructor(
    private val appPreferenceRepository: AppPreferenceRepository
) : ViewModel()
{
//    val activeUser: MutableLiveData<EntityUser?>
//        get() = appPreferenceRepository.activeUser
}