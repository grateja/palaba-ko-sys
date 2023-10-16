package com.csi.palabakosys.app.app_settings.printer.browser

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
    val savedDevices = MutableLiveData<List<PrinterDevice>>()
    val foundDevices = MutableLiveData<List<PrinterDevice>>()

    fun addFoundDevice(device: PrinterDevice) {
        val devices = (foundDevices.value ?: listOf()).toMutableList()
        if(!devices.any{device.macAddress == it.macAddress}) {
            devices.add(device)
        }
        foundDevices.value = devices
    }
}