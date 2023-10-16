package com.csi.palabakosys.app.customers.list

data class CustomerQueryResult(
    val result: List<CustomerListItem>,
    val count: Int,
)