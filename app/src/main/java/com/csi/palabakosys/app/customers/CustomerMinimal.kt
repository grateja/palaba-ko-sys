package com.csi.palabakosys.app.customers

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CustomerMinimal(
    var id: String,
    var name: String,
    var crn: String,
    var address: String?,
) : Parcelable