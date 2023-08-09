package com.csi.palabakosys.model

enum class EnumPaymentStatus(val id: Int, val prompt: String) {
    PAID(0, "Paid only"),
    UNPAID(1, "Unpaid only"),
    ALL(2, "Paid and Unpaid");

    companion object {
        fun fromId(id: Int?) : EnumPaymentStatus? {
            return values().find {
                it.id == id
            }
        }
    }
}