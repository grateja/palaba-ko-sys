package com.csi.palabakosys.util

interface FilterActivityInterface {
    var filterHint: String
    fun onQuery(keyword: String?)
}