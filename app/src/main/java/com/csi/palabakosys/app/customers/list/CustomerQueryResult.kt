package com.csi.palabakosys.app.customers.list

import com.csi.palabakosys.util.QueryResult

data class CustomerQueryResult(
    val result: List<CustomerListItem>,
    val count: Int,
)