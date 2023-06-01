package com.csi.palabakosys.room.entities

import androidx.room.*
import com.csi.palabakosys.model.EnumPaymentMethod
import java.util.*

@Entity(tableName = "job_order_payments")
data class EntityJobOrderPayment(
    @PrimaryKey(autoGenerate = false)
    override var id: UUID,

    @ColumnInfo(name = "payment_method")
    var paymentMethod: EnumPaymentMethod,

    @ColumnInfo(name = "amount_due")
    var amountDue: Float = 0f,

    @ColumnInfo(name = "cash_received")
    var cashReceived: Float,

    @ColumnInfo(name = "userId")
    var userId: UUID,

    @ColumnInfo(name = "or_number")
    var orNumber: String? = null,

    @Embedded
    var entityCashless: EntityCashless? = null,

    @Embedded
    var jobPaymentVoid: EntityJobOrderVoid? = null,
) : BaseEntity() {
    fun change() : Float {
        return cashReceived - amountDue
    }
}