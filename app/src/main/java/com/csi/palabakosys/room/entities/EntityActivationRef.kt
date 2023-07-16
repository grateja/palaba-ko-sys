package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Relation
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

data class EntityActivationRef(
    @ColumnInfo(name = "time_activated")
    var timeActivated: Instant?,

    @ColumnInfo(name = "total_minutes")
    var totalMinutes: Int?,

    @ColumnInfo(name = "jo_service_id")
    var joServiceId: UUID?,

    @ColumnInfo(name = "customer_id")
    var customerId: UUID?,
) {
    fun remainingTime() : Long {
        val totalMinutes = this.totalMinutes ?: 0

        timeActivated?.let {
            return ChronoUnit.MINUTES.between(Instant.now(), it.plus(totalMinutes.toLong(), ChronoUnit.MINUTES)) + 1
        }
        return 0
    }

    fun delayInMillis() : Long {
        val totalMinutes = this.totalMinutes ?: 0

        timeActivated?.let {
            return ChronoUnit.MILLIS.between(Instant.now(), it.plus(totalMinutes.toLong(), ChronoUnit.MINUTES))
        }
        return 0
    }

    fun running() : Boolean {
        return this.delayInMillis() > 0
    }
}