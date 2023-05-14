package com.csi.palabakosys.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.csi.palabakosys.model.EnumDeliveryVehicle

@Entity(tableName = "delivery_profiles")
data class EntityDeliveryProfile(
    val vehicle: EnumDeliveryVehicle,

    @ColumnInfo(name = "base_fare")
    val baseFare: Float,

    @ColumnInfo(name = "price_per_km")
    val pricePerKm: Float = 0f,

    @ColumnInfo(name = "min_distance")
    val minDistance: Float = 1f,
) : BaseEntity()