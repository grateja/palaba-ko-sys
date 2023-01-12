package com.csi.palabakosys.util

enum class CRUDActionEnum(val value: String) {
    CREATE("Create"),
    RETRIEVE("Retrieve"),
    UPDATE("Update"),
    DELETE("Delete");

    override fun toString(): String {
        return value
    }
}