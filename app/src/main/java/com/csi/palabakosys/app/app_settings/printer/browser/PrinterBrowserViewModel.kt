package com.csi.palabakosys.app.app_settings.printer.browser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrinterBrowserViewModel

@Inject
constructor(

) : ViewModel()
{
    val devices = MutableLiveData<List<PrinterDevice>>()
//    val foundDevices = MutableLiveData<List<PrinterDevice>>()
    val bluetoothEnabled = MutableLiveData(false)
    val locationEnabled = MutableLiveData(false)
//    val hasBluetoothPermission = MutableLiveData(false)

    fun setDevices(devices: List<PrinterDevice>) {
        this.devices.value = devices
    }

    fun setBluetoothState(state: Boolean) {
        bluetoothEnabled.value = state
    }

//    fun setBluetoothPermission(grant: Boolean) {
//        hasBluetoothPermission.value = grant
//    }

    fun addFoundDevice(device: PrinterDevice) {
        val devices = (devices.value ?: listOf()).toMutableList()
        if(!devices.any{device.macAddress == it.macAddress}) {
            devices.add(device)
        }
        this.devices.value = devices
    }

    fun setLocationState(state: Boolean) {
        locationEnabled.value = state
    }
}