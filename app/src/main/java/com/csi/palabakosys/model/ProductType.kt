package com.csi.palabakosys.model

import com.csi.palabakosys.R

enum class ProductType(val value: String, val icon: Int) {
    DETERGENT("Detergent", R.drawable.icon_detergent),
    FAB_CON("Fab-Con", R.drawable.icon_fabcon),
    OTHER("Other", R.drawable.icon_other_products);

    override fun toString(): String {
        return value
    }

    companion object {
        fun fromString(key: String?) : ProductType? {
            val list = values()
            return list.find {
                it.toString() == key
            }
        }
    }
}