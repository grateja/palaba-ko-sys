package com.csi.palabakosys.app.services.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.model.EnumMachineType
import com.csi.palabakosys.model.EnumWashType
import com.csi.palabakosys.model.Rule
import com.csi.palabakosys.room.entities.EntityService
import com.csi.palabakosys.room.entities.EntityServiceRef
import com.csi.palabakosys.room.repository.WashServiceRepository
import com.csi.palabakosys.util.InputValidation
import com.csi.palabakosys.viewmodels.CreateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditServiceViewModel

@Inject
constructor(
    private val repository: WashServiceRepository
) : CreateViewModel<EntityService>(repository) {
    val machineType = MutableLiveData<EnumMachineType>()
    val washType = MutableLiveData<EnumWashType?>()
    val minutes = MutableLiveData<String>()
    val price = MutableLiveData<String>()

    fun get(serviceId: UUID?, enumMachineType: EnumMachineType) {
        viewModelScope.launch {
            super.get(serviceId, EntityService(enumMachineType)).let {
                machineType.value = it.serviceRef.machineType
                washType.value = it.serviceRef.washType
                minutes.value = it.serviceRef.minutes.toString()
                price.value = it.price.toString()

                println("machine type")
                println(it.serviceRef.machineType)
            }
        }
    }

    fun validate() {
        val service = model.value ?: return
        val machineType = machineType.value ?: return
        val washType = washType.value
        val minutes = minutes.value?.toIntOrNull()
        val price = price.value?.toFloatOrNull()

        val inputValidation = InputValidation()

        inputValidation.addRule(
            "name",
            service.name,
            arrayOf(Rule.Required)
        )

        inputValidation.addRule(
            "minutes",
            minutes,
            arrayOf(
                Rule.Required,
                Rule.IsNumeric
            )
        )

        inputValidation.addRule(
            "price",
            price,
            arrayOf(
                Rule.Required,
                Rule.IsNumeric
            )
        )

        if(machineType == EnumMachineType.REGULAR_WASHER || machineType == EnumMachineType.TITAN_WASHER) {
            inputValidation.addRule(
                "washType",
                washType,
                arrayOf(
                    Rule.Required
                )
            )
        } else {
            inputValidation.addRule(
                "minutes",
                minutes,
                arrayOf(
                    Rule.Required,
                    Rule.DivisibleBy10,
                    Rule.Min(10f)
                )
            )
        }

        super.validate(inputValidation)
    }

    override fun save() {
        val machineType = machineType.value ?: return
        val washType = washType.value
        val minutes = minutes.value?.toIntOrNull()
        val price = price.value?.toFloatOrNull()

        if(minutes == null || price == null) return

        model.value?.apply {
            serviceRef = EntityServiceRef(
                machineType, washType, minutes
            )
            this.price = price
        }

        super.save()
    }
}