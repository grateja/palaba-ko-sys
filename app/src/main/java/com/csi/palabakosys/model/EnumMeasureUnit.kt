package com.csi.palabakosys.model

enum class EnumMeasureUnit(val value: String) {
    PCS("pcs"),
    SACHET("sachet"),
    MILLILITER("mL"),
    LITER("L"),
    LOAD("Load"),
    PACK("Pack");
    override fun toString() : String {
        return value
    }
    companion object {
        fun fromString(key: String) : EnumMeasureUnit? {
            val list = values()
            return list.find {
                it.toString() == key
            }
        }
    }
}