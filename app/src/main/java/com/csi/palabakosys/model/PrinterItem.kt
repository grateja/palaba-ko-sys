package com.csi.palabakosys.model

sealed class PrinterItem {
    open fun formattedString() : String {
        return ""
    }
    data class SingleLine(val characterLength: Int) : PrinterItem() {
        override fun toString(): String {
            return "-".repeat(characterLength)
        }

        override fun formattedString(): String {
            return super.formattedString()
        }
    }

    data class Header(val text: String) : PrinterItem() {
        override fun toString(): String {
            return "<b>$text</b>"
        }
    }

    data class ListItem(
        val count: Int,
        val title: String,
        val price: Float,
        val characterLength: Int,
    ) : PrinterItem() {
        override fun toString(): String {
            val countStr = count.let { "(*$it)" } ?: ""
            val titleStr = title.let { "$title " } ?: ""
            val priceStr = price.let { String.format("P%.2f", it) } ?: ""
            return "[L]$countStr$titleStr[R]${priceStr}\n"
        }
    }

    data class DefinitionTerm(
        val term: String,
        val definition: String,
        val characterLength: Int
    ) : PrinterItem() {
        override fun toString(): String {
            return "[L]$term[R]${definition}\n"
        }
    }
}
