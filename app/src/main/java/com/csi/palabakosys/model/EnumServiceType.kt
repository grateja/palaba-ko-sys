package com.csi.palabakosys.model

enum class EnumServiceType(val id: Int, val value: String) {
    WASH(1, "Wash"),
    DRY(2, "Dry"),
    OTHER(3, "Other");

    override fun toString(): String {
        return value
    }

    companion object {
        fun fromId(id: Int?) : EnumServiceType? {
            return values().find {
                it.id == id
            }
        }

        fun fromName(name: String?) : EnumServiceType? {
            return values().find {
                it.value == name
            }
        }
    }
}