package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

data class EntityActivationRef(
    @ColumnInfo(name = "time_activated")
    var timeActivated: Instant?,

    @ColumnInfo(name = "total_minutes")
    var totalMinutes: Int?,

//    @ColumnInfo(name = "customer_id")
//    var customerId: String?,
//
    @ColumnInfo(name = "jo_service_id")
    var joServiceId: UUID?,
) {
    fun remainingTime() : Long {
        val _totalMinutes = this.totalMinutes ?: 0

        timeActivated?.let {
            return ChronoUnit.MINUTES.between(Instant.now(), it.plus(_totalMinutes.toLong(), ChronoUnit.MINUTES))
        }
        return 0
    }

    fun running() : Boolean {
        return this.remainingTime() > 0
    }
}