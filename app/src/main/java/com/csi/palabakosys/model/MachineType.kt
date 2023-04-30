package com.csi.palabakosys.model

import com.csi.palabakosys.R

enum class MachineType(val value: String, val abbr: String, val serviceType: ServiceType, val icon: Int) {
    REGULAR_WASHER(
        "8KG Washer",
        "8KG",
        ServiceType.WASH,
        R.drawable.icon_washer
    ),
    REGULAR_DRYER(
        "8KG Dryer",
        "8KG",
        ServiceType.WASH,
        R.drawable.icon_dryer
    ),
    TITAN_WASHER(
        "12KG Washer",
        "12KG",
        ServiceType.DRY,
        R.drawable.icon_washer
    ),
    TITAN_DRYER(
        "12KG Dryer",
        "12KG",
        ServiceType.DRY,
        R.drawable.icon_dryer
    );

    override fun toString(): String {
        return value
    }

    companion object {
        fun fromString(key: String?) : MachineType? {
            val list = values()
            return list.find {
                it.toString() == key
            }
        }
    }
}