package com.csi.palabakosys.model

enum class EnumCRUDAction(private val value: String) {
    INSERT("insert"),
    UPDATE("update"),
    DELETE("delete"),
    CREATE_SHOP("create_shop");


    override fun toString(): String {
        return value
    }

    companion object {
        fun fromString(key: String) : EnumCRUDAction? {
            return values().find {
                it.name.uppercase() == key.uppercase()
            }
        }
    }
}

