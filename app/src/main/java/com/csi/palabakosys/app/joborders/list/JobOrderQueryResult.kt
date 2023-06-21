package com.csi.palabakosys.app.joborders.list

data class JobOrderQueryResult(
    val items: List<JobOrderListItem>,
    val count: Int,
)