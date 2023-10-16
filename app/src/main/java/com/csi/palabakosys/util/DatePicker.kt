package com.csi.palabakosys.util

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.Toast
import java.time.*
import java.time.format.DateTimeFormatter

class DatePicker(private val context: Context) {
    var onDateSelected: ((LocalDate, String?) -> Unit)? = null

    fun show(date: LocalDate, tag: String? = null) {
        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val localDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDayOfMonth)
                onDateSelected?.invoke(localDate, tag)
            },
            date.year,
            date.monthValue - 1,
            date.dayOfMonth
        )

        datePickerDialog.setCanceledOnTouchOutside(false)
        datePickerDialog.show()
    }

    fun setOnDateTimeSelectedListener(listener: (LocalDate, String?) -> Unit) {
        onDateSelected = listener
    }
}