package com.csi.palabakosys.app.packages

import androidx.lifecycle.ViewModel
import com.csi.palabakosys.room.repository.JobOrderPackageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PackagesViewModel

@Inject
constructor(
    private val repository: JobOrderPackageRepository
) : ViewModel() {
    val packages = repository.getAllAsLiveData()
}