package com.csi.palabakosys.util.databindings

import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.csi.palabakosys.model.EnumItemsPerPage

//package com.csi.palabakosys.util.databindings
//
//import android.widget.Spinner
//import androidx.databinding.BindingAdapter
//
//class SpinnerBindings {
//
//    @BindingAdapter("entries")
//    fun Spinner.setEntries(entries: List<Any>?) {
//        setSpinnerEntries(entries)
//    }
//
//    @BindingAdapter("onItemSelected")
//    fun Spinner.setItemSelectedListener(itemSelectedListener: ItemSelectedListener?) {
//        setSpinnerItemSelectedListener(itemSelectedListener)
//    }
//
//    @BindingAdapter("newValue")
//    fun Spinner.setNewValue(newValue: Any?) {
//        setSpinnerValue(newValue)
//    }
//}

@BindingAdapter("app:itemsPerPage")
fun setItemsPerPage(spinner: Spinner, itemsPerPage: EnumItemsPerPage?) {
    itemsPerPage?.let {
        spinner.setSelection(it.itemIndex)
    }
//    spinner.setSelection(itemsPerPage?.itemIndex)
//    val selectedId = when (measureUnit) {
//        EnumMeasureUnit.PCS -> R.id.radio_measure_unit_pcs
//        EnumMeasureUnit.SACHET -> R.id.radio_measure_unit_sachet
//        EnumMeasureUnit.MILLILITER -> R.id.radio_measure_unit_milliliter
//        EnumMeasureUnit.LITER -> R.id.radio_measure_unit_liter
//        EnumMeasureUnit.LOAD -> R.id.radio_measure_unit_load
//        else -> View.NO_ID
//    }
//    if (radioGroup.checkedRadioButtonId != selectedId) {
//        radioGroup.check(selectedId)
//    }
}
@InverseBindingAdapter(attribute = "app:itemsPerPage", event = "android:selectedItemPositionAttrChanged")
fun getItemsPerPage(spinner: Spinner): EnumItemsPerPage? {
    return EnumItemsPerPage.values().find {
        it.itemIndex == spinner.selectedItemPosition
    }
//    return when (radioGroup.checkedRadioButtonId) {
//        R.id.radio_measure_unit_pcs -> EnumMeasureUnit.PCS
//        R.id.radio_measure_unit_sachet -> EnumMeasureUnit.SACHET
//        R.id.radio_measure_unit_milliliter -> EnumMeasureUnit.MILLILITER
//        R.id.radio_measure_unit_liter -> EnumMeasureUnit.LITER
//        R.id.radio_measure_unit_load -> EnumMeasureUnit.LOAD
//        else -> null
//    }
}
