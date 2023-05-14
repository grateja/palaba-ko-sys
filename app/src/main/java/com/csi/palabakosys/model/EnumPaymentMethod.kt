package com.csi.palabakosys.model

enum class EnumPaymentMethod(val id: Int, val value: String) {
    CASH(1, "Cash"),
    CASHLESS(2,"Cashless");

    override fun toString(): String {
        return value
    }

    companion object {
        fun fromId(id: Int?) : EnumPaymentMethod? {
            return values().find {
                it.id == id
            }
        }
    }
}