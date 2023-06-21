package com.csi.palabakosys.app.preferences.printer.browser

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PrinterDevice(
    val deviceName: String?,
    val macAddress: String?,
    var selected: Boolean = false
) : Parcelable