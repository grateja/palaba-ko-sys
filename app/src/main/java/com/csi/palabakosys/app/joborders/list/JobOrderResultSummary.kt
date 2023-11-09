package com.csi.palabakosys.app.joborders.list

data class JobOrderResultSummary(
    val paidCount: Int,
    val unpaidCount: Int,
    val totalResultCount: Int,
    val paidSum: Float,
    val unpaidSum: Float,
    val totalSum: Float,
)