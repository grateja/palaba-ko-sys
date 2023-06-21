package com.csi.palabakosys.app.shared_ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.csi.palabakosys.R
import com.csi.palabakosys.databinding.ActivityAdvancedSearchDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdvancedSearchDialogActivity : AppCompatActivity() {
    companion object {
        const val KEYWORD_EXTRA = "keyword"
        const val ORDER_BY_EXTRA = "orderBy"
        const val SORT_DIRECTION_EXTRA = "sortDirection"
        const val ITEM_PER_PAGE_EXTRA = "itemPerPage"
        const val PAGE_EXTRA = "page"
    }
    private lateinit var binding: ActivityAdvancedSearchDialogBinding
    private val viewModel: AdvancedSearchViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Advanced search"
        binding = DataBindingUtil.setContentView(this, R.layout.activity_advanced_search_dialog)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }
}