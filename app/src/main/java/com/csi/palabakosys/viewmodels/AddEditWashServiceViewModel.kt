package com.csi.palabakosys.viewmodels

import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.model.EnumMachineType
import com.csi.palabakosys.model.Rule
import com.csi.palabakosys.room.entities.EntityService
import com.csi.palabakosys.room.repository.WashServiceRepository
import com.csi.palabakosys.util.DataState
import com.csi.palabakosys.util.InputValidation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditWashServiceViewModel
@Inject
constructor(
    private val repository: WashServiceRepository
) : CreateViewModel<EntityService>(repository)
{
    fun get(id: String?) {
//        super.get(id, EntityServiceWash())
    }

    fun setType(type: EnumMachineType) {
//        model.value?.machineType = type
//        println(model.value?.machineType)
    }

    override fun save() {
        model.value?.let {
            val inputValidation = InputValidation()
            inputValidation.addRules("name", it.name.toString(), arrayOf(Rule.Required))
//            inputValidation.addRules("washType", it.washType.toString(), arrayOf(Rule.REQUIRED))
            if(inputValidation.isInvalid()) {
                validation.value = inputValidation
                return@let
            }

            viewModelScope.launch {
                repository.save(it)?.let { service ->
                    model.value = service
                    dataState.value = DataState.Save(service)
                }
            }
        }
    }
}