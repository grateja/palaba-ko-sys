package com.csi.palabakosys.model

enum class ServiceType(val value: String) {
    WASH("Wash"),
    DRY("Dry"),
    OTHER("Other");

    override fun toString(): String {
        return value
    }

    companion object {
        fun fromString(key: String?) : ServiceType? {
            val list = values()
            return list.find {
                it.toString() == key
            }
        }
    }
}