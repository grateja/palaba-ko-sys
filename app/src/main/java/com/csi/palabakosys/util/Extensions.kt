package com.csi.palabakosys.util

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.csi.palabakosys.R
import com.csi.palabakosys.databinding.AlertDialogTextInputBinding
import com.csi.palabakosys.model.EnumDiscountType
import com.csi.palabakosys.model.EnumPaymentMethod
import com.csi.palabakosys.model.EnumProductType
import com.csi.palabakosys.model.EnumWashType
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.NumberFormat
import java.time.Duration
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
    } else {
        view.text = ""
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

@BindingAdapter("android:localDate")
fun setDate(view: TextView, date: LocalDate?) {
    try {
        if(date != null) {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
                .withZone(ZoneId.systemDefault())
            view.text = formatter.format(date)
        } else {
            view.text = ""
        }
    } catch (e: Exception) {
        e.printStackTrace()
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
    val imm = ContextCompat.getSystemService(this.context, InputMethodManager::class.java) as InputMethodManager
    return imm.hideSoftInputFromWindow(this.windowToken, 0)
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

@BindingAdapter("app:momentAgo")
fun setMomentAgo(view: TextView, dateTime: Instant?) {
    if(dateTime == null) {
        view.text = ""
        return
    }
    val now = Instant.now()
    val duration = Duration.between(dateTime, now).abs()

    val text = when {
        duration.toDays() >= 365 -> {
            val years = duration.toDays() / 365
            "$years ${if (years == 1L) "year ago" else "years ago"}"
        }
        duration.toDays() >= 30 -> {
            val months = duration.toDays() / 30
            "$months ${if (months == 1L) "month ago" else "months ago"}"
        }
        duration.toDays() >= 7 -> {
            val weeks = duration.toDays() / 7
            "$weeks ${if (weeks == 1L) "week ago" else "weeks ago"}"
        }
        duration.toDays() >= 1 -> {
            val days = duration.toDays()
            "$days ${if (days == 1L) "day ago" else "days ago"}"
        }
        duration.toHours() >= 1 -> {
            val hours = duration.toHours()
            "$hours ${if (hours == 1L) "hour ago" else "hours ago"}"
        }
        duration.toMinutes() >= 1 -> {
            val minutes = duration.toMinutes()
            "$minutes ${if (minutes == 1L) "minute ago" else "minutes ago"}"
        }
        else -> "just now"
    }

    view.text = text
}

fun Context.showDeleteConfirmationDialog(title: String? = "Delete this item", message: String? = "Are you sure you want to proceed?", onDeleteConfirmed: () -> Unit) {
    AlertDialog.Builder(this).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton("Yes") { _, _ ->
            onDeleteConfirmed()
        }
        setNegativeButton("Cancel") { _, _ ->

        }
        create()
    }.show()
}

fun Context.showTextInputDialog(title: String?, message: String?, initialValue: String?, onOk: (value: String) -> Unit) {
    val binding: AlertDialogTextInputBinding = DataBindingUtil.inflate(
        LayoutInflater.from(this),
        R.layout.alert_dialog_text_input,
        null,
        false
    )

    binding.textInput.setText(initialValue)
    binding.textInput.hint = message

    AlertDialog.Builder(this).apply {
        setTitle(title)
        setView(binding.root)
        setPositiveButton("Ok") { _, _ ->
            onOk(binding.textInput.text.toString())
        }
        create()
    }.show()
}


fun View.showSnackBar(message: String, duration: Int = Snackbar.LENGTH_SHORT, actionText: String? = null, actionCallback: (() -> Unit)? = null) {
    val snackBar = Snackbar.make(this, message, duration)

    actionText?.let {
        snackBar.setAction(actionText) {
            actionCallback?.invoke()
        }
    }

    snackBar.show()
}

fun Context.loadThumbnailOrBitmap(uri: Uri, dimension: Int): Bitmap? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        println("get thumbnail")
        contentResolver.loadThumbnail(uri, Size(dimension, dimension), null)
    } else {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)

        var bitmap: Bitmap? = null

        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val imagePath = it.getString(columnIndex)
                bitmap = BitmapFactory.decodeFile(imagePath)
            }
        }

        cursor?.close()
        bitmap
    }
}

fun Context.calculateSpanCount(
    columnWidthId: Int,
    horizontalMarginId: Int? = null
): Int {
    val columnWidth = resources.getDimensionPixelSize(columnWidthId)

    val margin = if(horizontalMarginId == null) 0 else (resources.getDimension(horizontalMarginId) * 2)

    val parentWidthDp = resources.displayMetrics.widthPixels - margin.toInt()

    val spanCount = (parentWidthDp / columnWidth)
    return if (spanCount > 0) spanCount else 1
}

fun LocalDate.toShort(): String {
    val today = LocalDate.now()
    val yearFormat = if(this.year != today.year) { ", yyyy" } else { "" }
    val formatter = DateTimeFormatter.ofPattern("MMM dd$yearFormat") // You can change the pattern to your desired format
    return this.format(formatter)
}

fun TextInputEditText.selectAllOnFocus() {
    this.setOnFocusChangeListener { view, hasFocus ->
        if (hasFocus) {
            view.post {
                this.selectAll()
            }
        }
    }
}

fun Instant.toShort() : String {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a")
        .withZone(ZoneId.systemDefault())
    return formatter.format(this)
}