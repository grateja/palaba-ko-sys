package com.csi.palabakosys.preferences

data class PrinterSettings(
    val name: String?,
    val address: String?,
    val dpi: Int,
    val width: Float,
    val character: Int
)