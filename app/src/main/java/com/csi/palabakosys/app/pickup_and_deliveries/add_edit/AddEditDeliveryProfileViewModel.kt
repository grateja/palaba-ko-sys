package com.csi.palabakosys.app.pickup_and_deliveries.add_edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.model.Rule
import com.csi.palabakosys.room.entities.EntityDeliveryProfile
import com.csi.palabakosys.room.repository.DeliveryProfilesRepository
import com.csi.palabakosys.util.InputValidation
import com.csi.palabakosys.viewmodels.CreateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditDeliveryProfileViewModel
@Inject
constructor(
    private val repository: DeliveryProfilesRepository
): CreateViewModel<EntityDeliveryProfile>(repository) {
    val baseFare = MutableLiveData<String>()
    val pricePerKm = MutableLiveData<String>()

    fun get(id: UUID?) {
        viewModelScope.launch {
            super.get(id, EntityDeliveryProfile()).let {
                baseFare.value = it.baseFare.toString()
                pricePerKm.value = it.pricePerKm.toString()
            }
        }
    }

    fun validate() {
        val baseFare = baseFare.value
        val pricePerKm = pricePerKm.value

        val inputValidation = InputValidation()
            inputValidation.addRule("baseFare", baseFare, arrayOf(Rule.Required, Rule.IsNumeric))
            inputValidation.addRule("pricePerKm", pricePerKm, arrayOf(Rule.Required, Rule.IsNumeric))



        if(!super.isInvalid(inputValidation)) {
            model.value = model.value.apply {
                this?.baseFare = baseFare!!.toFloat()
                this?.pricePerKm = pricePerKm!!.toFloat()
            }
        }
    }
}