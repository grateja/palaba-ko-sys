package com.csi.palabakosys.util

enum class MeasureUnit(private val value: String) {
    PCS("pcs"),
    SACHET("sachet"),
    MILLILITER("mL"),
    LITER("L"),
    LOAD("Load");
    override fun toString() : String {
        return value
    }
    companion object {
        fun fromString(key: String) : MeasureUnit? {
            val list = values()
            return list.find {
                it.toString() == key
            }
        }
    }
}