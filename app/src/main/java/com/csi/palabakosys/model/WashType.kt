package com.csi.palabakosys.model

enum class WashType(val value: String, val pulse: Int, val description: String, var defaultMinutes: Int, var selected: Boolean = false) {
    DELICATE(
        "Delicate Wash",
        1,
        "~21 Minutes to ~24 Minutes",
        21
    ),
    WARM(
        "Warm Wash",
        2,
        "",
        36
    ),
    COLD(
        "Cold Wash",
        2,
        "",
        36
    ),
    HOT(
        "Hot Wash",
        3,
        "",
        46
    ),
    SUPER_WASH(
        "Super Wash",
        4,
        "",
        47
    );

    override fun toString(): String {
        return value
    }

    companion object {
        fun fromString(key: String?) : WashType? {
            val list = values()
            return list.find {
                it.toString() == key
            }
        }
    }
}