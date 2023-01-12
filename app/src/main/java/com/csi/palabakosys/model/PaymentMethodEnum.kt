package com.csi.palabakosys.model

enum class PaymentMethodEnum(val value: String) {
    CASH("Cash"),
    CASHLESS("Cashless"),
    CASH_BACK("Cashback");

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