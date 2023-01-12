package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.csi.palabakosys.model.PaymentMethodEnum
import java.util.*

@Entity(tableName = "job_order_payments")
data class EntityJobOrderPayment(
    @ColumnInfo(name = "job_order_id")
    var jobOrderId: UUID? = null
) : BaseEntity() {
    @ColumnInfo(name = "payment_method")
    var paymentMethod: PaymentMethodEnum? = null

    @ColumnInfo(name = "cash")
    var cash: Float = 0f

    @ColumnInfo(name = "cash_back")
    var cashBack: Float = 0f

    @ColumnInfo(name = "discount_name")
    var discountName: String? = null

    @ColumnInfo(name = "discount_percentage")
    var discountPercentage: Float = 0f

    @ColumnInfo(name = "discount_in_peso")
    var discountInPeso: Float = 0f

    @ColumnInfo(name = "balance")
    var balance: Float = 0f

    @ColumnInfo(name = "change")
    var change: Float = 0f

    @ColumnInfo(name = "paid_to")
    var paidTo: String? = null

    @ColumnInfo(name = "or_number")
    var orNumber: String? = null

    @Embedded
    var entityCashless: EntityCashless? = null
}