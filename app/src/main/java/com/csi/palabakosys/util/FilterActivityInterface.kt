package com.csi.palabakosys.util

interface FilterActivityInterface {
    var toolbarBackground: Int
    var filterHint: String
    var enableAdvancedFilter: Boolean
    fun onQuery(keyword: String?)
    fun onAdvancedSearchClick() { }
}