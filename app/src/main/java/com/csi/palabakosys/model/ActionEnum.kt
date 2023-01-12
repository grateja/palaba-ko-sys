package com.csi.palabakosys.model

enum class ActionEnum(private val value: String) {
    INSERT("insert"),
    UPDATE("update"),
    DELETE("delete"),
    CREATE_SHOP("create_shop");


    override fun toString(): String {
        return value
    }

    companion object {
        fun fromString(key: String) : ActionEnum? {
            val list = values()
            return list.find {
                it.toString() == key
            }
        }
    }
}

