package com.csi.palabakosys.app.payment_list

import com.csi.palabakosys.room.entities.EntityJobOrderPaymentListItem
import com.csi.palabakosys.room.entities.QueryAggrResult

data class PaymentQueryResult(
    val items: List<EntityJobOrderPaymentListItem>,
    val aggResult: QueryAggrResult?
)
