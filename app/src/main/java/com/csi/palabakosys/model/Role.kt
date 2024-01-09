package com.csi.palabakosys.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Role(val value: String) : Parcelable {
    STAFF("staff"),
    ADMIN("admin");
//    DEVELOPER("developer");

    override fun toString(): String {
        return value
    }

    companion object {
        fun fromString(key: String) : Role {
            val list = values()
            return list.first {
                it.toString() == key
            }
        }
    }
}