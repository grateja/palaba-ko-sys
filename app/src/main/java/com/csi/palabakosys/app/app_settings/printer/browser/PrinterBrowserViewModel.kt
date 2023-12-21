package com.csi.palabakosys.app.app_settings.printer.browser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csi.palabakosys.settings.PrinterSettingsRepository
//import com.csi.palabakosys.room.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrinterBrowserViewModel

@Inject
constructor(
    val dataStoreRepository: PrinterSettingsRepository
) : ViewModel()
{
    val devices = MutableLiveData<List<PrinterDevice>>()
    private val currentDeviceAddress = dataStoreRepository.printerAddress
//    val foundDevices = MutableLiveData<List<PrinterDevice>>()
    val bluetoothEnabled = MutableLiveData(false)
    val locationEnabled = MutableLiveData(false)
//    val hasBluetoothPermission = MutableLiveData(false)

    fun setDevices(devices: List<PrinterDevice>) {
        val address = currentDeviceAddress.value
        devices.find { it.macAddress == address }?.selected = true
        this.devices.value = devices
    }

    fun setBluetoothState(state: Boolean) {
        bluetoothEnabled.value = state
    }

//    fun setBluetoothPermission(grant: Boolean) {
//        hasBluetoothPermission.value = grant
//    }

//    fun addFoundDevice(device: PrinterDevice) {
//        val devices = (devices.value ?: listOf()).toMutableList()
//        val found = devices.find { it.macAddress == device.macAddress }
//        if(found != null) {
//            device.inRange = true
//        } else {
//            device.inRange = true
//            devices.add(device)
//        }
//        this.devices.value = devices
//    }
    fun addFoundDevice(device: PrinterDevice) {
        devices.value = (devices.value.orEmpty().toMutableList().apply {
            find { it.macAddress == device.macAddress }?.inRange = true
            if (find { it.macAddress == device.macAddress } == null) {
                add(device)
            }
        })
    }

    fun setLocationState(state: Boolean) {
        locationEnabled.value = state
    }
}