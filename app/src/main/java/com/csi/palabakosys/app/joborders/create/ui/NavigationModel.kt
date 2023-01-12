package com.csi.palabakosys.app.joborders.create.ui

data class NavigationModel(
    val label: String,
    val destination: Int,
    var selected: Boolean = false,
    var active: Boolean = false,
)