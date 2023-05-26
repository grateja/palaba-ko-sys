package com.csi.palabakosys.app.customers

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class CustomerMinimal(
    var id: UUID,
    var name: String,
    var crn: String,
    var address: String?,
    var unpaid: Int?,
) : Parcelable