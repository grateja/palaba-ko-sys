package com.csi.palabakosys.util

import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.csi.palabakosys.R
import com.csi.palabakosys.model.EnumDiscountType
import com.csi.palabakosys.model.EnumPaymentMethod
import com.csi.palabakosys.model.EnumProductType
import com.csi.palabakosys.model.EnumWashType
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDate
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
fun setText(view: TextInputEditText, washType: EnumWashType?) {
    view.setText(washType?.toString())
}

@InverseBindingAdapter(attribute = "android:text", event = "android:textAttrChanged")
fun getWashTypeEnum(view: TextInputEditText) : EnumWashType? {
    return EnumWashType.fromName(view.text.toString())
}

@BindingAdapter("android:text")
fun setText(view: TextInputEditText, productType: EnumProductType?) {
    view.setText(productType?.toString())
}

@InverseBindingAdapter(attribute = "android:text", event = "android:textAttrChanged")
fun getProductType(view: TextInputEditText) : EnumProductType? {
    return EnumProductType.fromName(view.text.toString())
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

fun String?.isUUID(): Boolean {
    return try {
        UUID.fromString(this)
        true
    } catch (e: IllegalArgumentException) {
        false
    }
}

fun String?.toUUID() : UUID? {
    if(this?.isUUID() == true) {
        return UUID.fromString(this)
    }
    return null
}

fun Instant.isToday(): Boolean {
    val currentZone = ZoneId.systemDefault()
    val currentDate = LocalDate.now(currentZone)

    val currentStartOfDay = currentDate.atStartOfDay(currentZone).toInstant()
    val currentEndOfDay = currentDate.plusDays(1).atStartOfDay(currentZone).toInstant()

    return this.isAfter(currentStartOfDay) && this.isBefore(currentEndOfDay)
}


@BindingAdapter("app:selectedPaymentMethod")
fun setPaymentMethod(radioGroup: RadioGroup, paymentMethod: EnumPaymentMethod?) {
    val selectedId = when (paymentMethod) {
        EnumPaymentMethod.CASH -> R.id.radio_cash
        EnumPaymentMethod.CASHLESS -> R.id.radio_cashless
        else -> View.NO_ID
    }
    if (radioGroup.checkedRadioButtonId != selectedId) {
        radioGroup.check(selectedId)
    }
}

@InverseBindingAdapter(attribute = "app:selectedPaymentMethod", event = "android:checkedButtonAttrChanged")
fun getPaymentMethod(radioGroup: RadioGroup): EnumPaymentMethod? {
    return when (radioGroup.checkedRadioButtonId) {
        R.id.radio_cash -> EnumPaymentMethod.CASH
        R.id.radio_cashless -> EnumPaymentMethod.CASHLESS
        else -> null
    }
}

@BindingAdapter("app:selectedDiscountType")
fun setDiscountType(radioGroup: RadioGroup, discountType: EnumDiscountType?) {
    val selectedId = when (discountType) {
        EnumDiscountType.FIXED -> R.id.radio_cashless_discount_fixed
        EnumDiscountType.PERCENTAGE -> R.id.radio_discount_percentage
        else -> View.NO_ID
    }
    if (radioGroup.checkedRadioButtonId != selectedId) {
        radioGroup.check(selectedId)
    }
}

@InverseBindingAdapter(attribute = "app:selectedDiscountType", event = "android:checkedButtonAttrChanged")
fun getDiscountType(radioGroup: RadioGroup): EnumDiscountType? {
    return when (radioGroup.checkedRadioButtonId) {
        R.id.radio_cashless_discount_fixed -> EnumDiscountType.FIXED
        R.id.radio_discount_percentage -> EnumDiscountType.PERCENTAGE
        else -> null
    }
}

@BindingAdapter("android:checkedButtonAttrChanged")
fun setCheckedButtonListener(radioGroup: RadioGroup, listener: InverseBindingListener?) {
    radioGroup.setOnCheckedChangeListener { _, _ ->
        listener?.onChange()
    }
}