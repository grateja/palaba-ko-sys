package com.csi.palabakosys.app.customers.list

import androidx.room.Embedded
import com.csi.palabakosys.room.entities.EntityCustomer
import java.time.Instant

data class CustomerListItem(
    @Embedded
    val customer: EntityCustomer,

    val lastVisit: Instant?,
)