package com.csi.palabakosys.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class EnumPrintState : Parcelable {
    READY,
    STARTED,
    CANCELED,
    FINISHED,
    ERROR
}