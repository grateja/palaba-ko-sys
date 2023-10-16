package com.csi.palabakosys.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class EnumJoFilterBy(val value: String) : Parcelable {
    DATE_PAID("paid"),
    DATE_CREATED("created");

    companion object {
        fun fromString(value: String) : EnumJoFilterBy? {
            return values().find {
                it.value == value
            }
        }
    }
}