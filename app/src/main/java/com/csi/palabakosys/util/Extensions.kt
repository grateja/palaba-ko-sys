package com.csi.palabakosys.util

import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.csi.palabakosys.model.ProductType
import com.csi.palabakosys.model.WashType
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@BindingAdapter("app:errorText")
fun setErrorText(view: TextInputLayout, errorMessage: String) {
    view.error = errorMessage
}

@BindingAdapter("android:visibility")
fun setVisibility(view: View, isVisible: Boolean) {
    view.visibility = if(isVisible) { View.VISIBLE } else { View.GONE }
}

@BindingAdapter("android:text")
fun setText(view: TextInputEditText, washType: WashType?) {
    view.setText(washType?.toString())
}

@InverseBindingAdapter(attribute = "android:text", event = "android:textAttrChanged")
fun getWashTypeEnum(view: TextInputEditText) : WashType? {
    return WashType.fromString(view.text.toString())
}

@BindingAdapter("android:text")
fun setText(view: TextInputEditText, productType: ProductType?) {
    view.setText(productType?.toString())
}

@InverseBindingAdapter(attribute = "android:text", event = "android:textAttrChanged")
fun getProductType(view: TextInputEditText) : ProductType? {
    return ProductType.fromString(view.text.toString())
}

@BindingAdapter("android:text")
fun setText(view: TextView, dateTime: Instant?) {
    if(dateTime != null) {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a")
            .withZone(ZoneId.systemDefault())
        view.text = formatter.format(dateTime)
    }
}

@BindingAdapter("android:date")
fun setDate(view: TextView, dateTime: Instant?) {
    if(dateTime != null) {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
            .withZone(ZoneId.systemDefault())
        view.text = formatter.format(dateTime)
    }
}

@BindingAdapter("android:time")
fun setTime(view: TextView, dateTime: Instant?) {
    if(dateTime != null) {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
            .withZone(ZoneId.systemDefault())
        view.text = formatter.format(dateTime)
    }
}

@BindingAdapter("android:peso")
fun setPeso(view: TextView, value: Float?) {
    if(value != null) {
        view.text = "P %s".format(NumberFormat.getNumberInstance(Locale.US).format(value))
    }
}

fun View.hideKeyboard() : Boolean {
    val imm = ContextCompat.getSystemService(context, InputMethodManager::class.java) as InputMethodManager
    return imm.hideSoftInputFromWindow(windowToken, 0)
}