package com.csi.palabakosys.app.machines.addedit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.model.EnumMachineType
import com.csi.palabakosys.room.entities.EntityMachine
import com.csi.palabakosys.room.repository.MachineRepository
import com.csi.palabakosys.util.DataState
import com.csi.palabakosys.util.InputValidation
import com.csi.palabakosys.util.toUUID
import com.csi.palabakosys.viewmodels.CreateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AddEditMachineViewModel
@Inject
constructor(
    private val repository: MachineRepository
) : CreateViewModel<EntityMachine>(repository) {

    val connecting = MutableLiveData(false)
//    val state = MutableLiveData<RemoteActivationState>()

    fun get(id: String?, machineType: EnumMachineType) {
        model.value.let {
            if(it != null) return
            viewModelScope.launch {
                val stackOrder = repository.getLastStackOrder(machineType) + 1
                super.get(id.toUUID(), EntityMachine(stackOrder, machineType, 0, stackOrder))
            }
        }
    }

    override fun save() {
        model.value?.let {
            val inputValidation = InputValidation()
//            inputValidation.addRules("name", it.name.toString(), arrayOf(Rule.REQUIRED))
            if(inputValidation.isInvalid()) {
                validation.value = inputValidation
                return@let
            }
            viewModelScope.launch {
                repository.save(it)?.let { product ->
                    model.value = product
                    dataState.value = DataState.SaveSuccess(product)
                }
            }
        }
    }

    fun testConnection() {
        connecting.value = true
//        viewModelScope.launch {
            model.value?.let {
                val client = OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build()

                val request = Request.Builder()
                    .url("http://192.168.210.${it.ipEnd}/details")
                    .build()

//                state.value = RemoteActivationState.Connecting

                client.newCall(request).enqueue(object: Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                        viewModelScope.launch {
                            connecting.value = false
//                            state.value = RemoteActivationState.Error(e.message.toString())
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        println("body")
                        val body = response.body()?.string()
                        println(body)
                        viewModelScope.launch {
                            connecting.value = false
//                            state.value = RemoteActivationState.Connected("Connection test success")
                        }
                    }
                })
        }
    }
}