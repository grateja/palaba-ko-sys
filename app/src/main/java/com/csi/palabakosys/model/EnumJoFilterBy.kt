package com.csi.palabakosys.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class EnumJoFilterBy(val itemIndex: Int, val value: String) : Parcelable {
    DATE_PAID(0, "paid"),
    DATE_CREATED(1, "created");

    companion object {
        fun fromString(value: String) : EnumJoFilterBy? {
            return values().find {
                it.value == value
            }
        }
    }
}