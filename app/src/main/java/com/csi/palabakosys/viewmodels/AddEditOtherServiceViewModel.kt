package com.csi.palabakosys.viewmodels

import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.model.Rule
import com.csi.palabakosys.room.entities.EntityServiceOther
import com.csi.palabakosys.util.DataState
import com.csi.palabakosys.util.InputValidation
import com.csi.palabakosys.room.repository.OtherServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditOtherServiceViewModel
@Inject
constructor(
    private val repository: OtherServiceRepository
) : CreateViewModel<EntityServiceOther>(repository)
{
    fun get(id: String?) {
        viewModelScope.launch {
            super.get(id, EntityServiceOther())
        }
    }

    fun save() {
        model.value?.let {
            val inputValidation = InputValidation()
            inputValidation.addRules("name", it.name.toString(), arrayOf(Rule.REQUIRED))
            if(inputValidation.isInvalid()) {
                validation.value = inputValidation
                return@let
            }

            viewModelScope.launch {
                repository.save(it)?.let { service ->
                    model.value = service
                    dataState.value = DataState.Success(service)
                }
            }
        }
    }
}