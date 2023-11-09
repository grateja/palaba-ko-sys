package com.csi.palabakosys.util

import androidx.lifecycle.MutableLiveData
import com.csi.palabakosys.model.FilterParamsInterface

interface ListViewModelInterface {
    val keyword: MutableLiveData<String>
    fun filter(reset: Boolean)
    fun setKeyword(keyword: String?) {
        this.keyword.value = keyword
        filter(true)
    }
}