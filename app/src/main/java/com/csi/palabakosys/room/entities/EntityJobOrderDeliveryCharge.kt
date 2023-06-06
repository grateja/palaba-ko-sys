package com.csi.palabakosys.room.entities

import androidx.room.*
import com.csi.palabakosys.model.EnumDeliveryOption
import com.csi.palabakosys.model.EnumDeliveryVehicle
import java.util.*

@Entity(tableName = "job_order_delivery_charges")
data class EntityJobOrderDeliveryCharge(
    @ColumnInfo(name = "delivery_profile_id")
    val deliveryProfileId: UUID,

    val vehicle: EnumDeliveryVehicle,

    @ColumnInfo(name = "delivery_option")
    val deliveryOption: EnumDeliveryOption,

    val price: Float,

    val distance: Float,

    @PrimaryKey(autoGenerate = false)
    override var id: UUID,
) : BaseEntity(id)