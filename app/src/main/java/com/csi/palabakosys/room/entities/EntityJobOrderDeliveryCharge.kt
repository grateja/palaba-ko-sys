package com.csi.palabakosys.room.entities

import androidx.room.*
import com.csi.palabakosys.model.DeliveryOption
import com.csi.palabakosys.model.DeliveryVehicle
import java.util.*

@Entity(tableName = "job_order_delivery_charges")
data class EntityJobOrderDeliveryCharge(
//    @ColumnInfo(name = "job_order_id")
//    val jobOrderId: UUID?,

    @ColumnInfo(name = "delivery_profile_id")
    val deliveryProfileId: UUID,

    val vehicle: DeliveryVehicle,

    @ColumnInfo(name = "delivery_option")
    val deliveryOption: DeliveryOption,

    val price: Float,

    val distance: Float,

    @PrimaryKey(autoGenerate = false)
    override var id: UUID
) : BaseEntity(id)