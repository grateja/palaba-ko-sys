package com.csi.palabakosys.util

interface FilterActivityInterface {
    var filterHint: String
    var enableAdvancedSearch: Boolean
    fun onQuery(keyword: String?)
    fun onAdvancedSearchClicked() : Boolean
}