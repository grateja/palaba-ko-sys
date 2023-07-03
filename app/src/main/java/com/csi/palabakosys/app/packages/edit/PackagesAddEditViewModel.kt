package com.csi.palabakosys.app.packages.edit

import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.model.Rule
import com.csi.palabakosys.room.entities.EntityPackage
import com.csi.palabakosys.room.repository.JobOrderPackageRepository
import com.csi.palabakosys.util.InputValidation
import com.csi.palabakosys.viewmodels.CreateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PackagesAddEditViewModel

@Inject
constructor(
    private val repository: JobOrderPackageRepository
) : CreateViewModel<EntityPackage>(repository) {
    fun get(id: UUID?) {
        viewModelScope.launch {
            val entity = super.get(id, EntityPackage())
        }
    }

    fun validate() {
        val inputValidation = InputValidation().apply {
            addRule("packageName", model.value?.packageName, arrayOf(Rule.Required))
        }

        super.validate(inputValidation)
    }
}