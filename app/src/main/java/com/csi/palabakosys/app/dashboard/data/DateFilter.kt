package com.csi.palabakosys.app.dashboard.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class DateFilter(
    var dateFrom: LocalDate,
    var dateTo: LocalDate?
) : Parcelable {
    constructor() : this(LocalDate.now(), LocalDate.now())
    constructor(localDate: LocalDate) : this(localDate, localDate)
}
