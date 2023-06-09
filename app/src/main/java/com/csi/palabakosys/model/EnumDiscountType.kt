package com.csi.palabakosys.model

enum class EnumDiscountType(val value: String) {
    FIXED("Fixed"),
    PERCENTAGE("Percentage");

    override fun toString() : String {
        return value
    }

    companion object {
        fun fromString(name: String?) : EnumDiscountType? {
            return values().find {
                it.value == name
            }
        }
    }
}