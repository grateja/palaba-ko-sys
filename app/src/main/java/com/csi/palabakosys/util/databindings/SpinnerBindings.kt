package com.csi.palabakosys.util.databindings

import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.csi.palabakosys.util.EnumSortDirection

@BindingAdapter("app:text")
fun setText(spinner: Spinner, text: String?) {
    spinner.setSelection(spinner.selectedItemPosition)
}

@InverseBindingAdapter(
    attribute = "app:text",
    event = "android:selectedItemPositionAttrChanged"
)
fun getText(spinner: Spinner): String? {
    return spinner.selectedItem.toString()
}

@BindingAdapter("app:sortDirection")
fun setSortDirection(spinner: Spinner, sortDirection: EnumSortDirection?) {
    spinner.setSelection(spinner.selectedItemPosition)
}

@InverseBindingAdapter(
    attribute = "app:sortDirection",
    event = "android:selectedItemPositionAttrChanged"
)
fun getSortDirection(spinner: Spinner): EnumSortDirection? {
    return EnumSortDirection.values().find {
        it.itemIndex == spinner.selectedItemPosition
    }
}