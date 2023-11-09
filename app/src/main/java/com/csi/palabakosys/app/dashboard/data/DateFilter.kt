package com.csi.palabakosys.app.dashboard.data

import android.os.Parcelable
import com.csi.palabakosys.util.toHumanReadableString
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Parcelize
data class DateFilter(
    var dateFrom: LocalDate,
    var dateTo: LocalDate?
) : Parcelable {
    constructor() : this(LocalDate.now(), LocalDate.now())
    constructor(localDate: LocalDate) : this(localDate, localDate)

    override fun toString(): String {
        val dateFromString = dateFrom.toHumanReadableString()
        val dateToString = dateTo?.toHumanReadableString()

        return if (dateTo != null) {
            "Dates from $dateFromString to $dateToString"
        } else {
            "Date from $dateFromString"
        } + (
            dateTo?.let {
                dateTo?.let {
                    val days = ChronoUnit.DAYS.between(dateFrom, dateTo)
                    " (${days + 1} days)"
                } ?: ""
            } ?: ""
        )
    }
}
