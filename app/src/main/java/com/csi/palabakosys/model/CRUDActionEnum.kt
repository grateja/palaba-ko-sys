package com.csi.palabakosys.model

enum class CRUDActionEnum(val value: String) {
    CREATE("Create"),
    RETRIEVE("Retrieve"),
    UPDATE("Update"),
    DELETE("Delete");

    override fun toString(): String {
        return value
    }
}