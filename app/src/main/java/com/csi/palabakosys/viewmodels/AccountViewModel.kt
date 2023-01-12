package com.csi.palabakosys.viewmodels

import androidx.lifecycle.ViewModel
import com.csi.palabakosys.preferences.AppPreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel
@Inject
constructor(
    private val pref: AppPreferenceRepository
): ViewModel() {

    val activeUser = pref.activeUser

    fun logout() {
        pref.logout()
    }
}