package com.csi.palabakosys.util

abstract class QueryResult<T> (
    val result: List<T>,
    val count: Int,
)