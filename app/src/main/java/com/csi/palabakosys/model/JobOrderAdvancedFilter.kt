package com.csi.palabakosys.model

import android.os.Parcelable
import com.csi.palabakosys.app.dashboard.data.DateFilter
import kotlinx.parcelize.Parcelize

@Parcelize
data class JobOrderAdvancedFilter(
    var filterBy: EnumJoFilterBy = EnumJoFilterBy.DATE_CREATED,
//    var nonVoidOnly: Boolean = true,
    var paymentStatus: EnumPaymentStatus = EnumPaymentStatus.ALL,
    override var dateFilter: DateFilter? = null
): Parcelable, BaseFilterParams()