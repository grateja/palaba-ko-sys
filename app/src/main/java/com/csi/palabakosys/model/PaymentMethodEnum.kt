package com.csi.palabakosys.model

enum class PaymentMethodEnum(val value: String) {
    CASH("Cash"),
    CASHLESS("Cashless");

    override fun toString(): String {
        return value
    }

    companion object {
        fun fromString(key: String) : PaymentMethodEnum? {
            val list = values()
            return list.find {
                it.toString() == key
            }
        }
    }
}