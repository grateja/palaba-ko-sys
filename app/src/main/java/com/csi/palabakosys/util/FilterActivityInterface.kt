package com.csi.palabakosys.util

interface FilterActivityInterface {
    var filterHint: String
    var enableAdvancedFilter: Boolean
    fun onQuery(keyword: String?)
    fun onAdvancedSearchClick() { }
}