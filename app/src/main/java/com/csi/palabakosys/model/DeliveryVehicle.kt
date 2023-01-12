package com.csi.palabakosys.model

import com.csi.palabakosys.R

enum class DeliveryVehicle(val id: String, val value: String, val description: String, val icon: Int) {
    TRIKE_PEDAL("trike-pedal", "Trike Pedal", "Up to 50KG, Below 800 meters distance", R.drawable.icon_delivery_trike_pedal),
    TRIKE_ELECTRIC("trike-electric","Trike Electric", "Up to 100KG, Below 2KM distance", R.drawable.icon_delivery_trike_electric),
    MOTORCYCLE("motorcycle", "Motorcycle", "Up to 20KG, Below 5KM distance", R.drawable.icon_delivery_motorcycle),
    TRICYCLE("tricycle", "Tricycle", "Up to 100KG, Below 5KM distance", R.drawable.icon_delivery_tricycle),
    SEDAN("sedan","Sedan", "Up to 200KG, Good for delicate garments and long distance", R.drawable.icon_delivery_sedan),
    MPV("mpv","MPV", "Up to 300KG, Good for long distance", R.drawable.icon_delivery_mpv),
    SMALL_VAN("small-van","Small Van", "Up to 600KG, Good for long distance and bulk delivery", R.drawable.icon_delivery_small_van);

    override fun toString(): String {
        return value
    }

    companion object {
        fun fromString(key: String?) : DeliveryVehicle? {
            val list = values()
            return list.find {
                it.toString() == key
            }
        }
    }
}