package com.csi.palabakosys.settings

import android.content.Context
import androidx.lifecycle.LiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrinterSettingsRepository

@Inject
constructor(
    private val dao: SettingsDao
) : BaseSettingsRepository(dao) {
    companion object {
        const val PRINTER_NAME = "printerName"
        const val PRINTER_ADDRESS = "printerAddress"
        const val PRINTER_WIDTH = "printerWidth"
        const val PRINTER_CHARACTERS_PER_LINE = "printerCharactersPerLine"
        const val JOB_ORDER_DISCLAIMER = "disclaimer"
    }
    val printerName = getAsLiveData(PRINTER_NAME, "Not set")
    val printerAddress = getAsLiveData(PRINTER_ADDRESS, "Not set")
    val printerWidth = getAsLiveData(PRINTER_WIDTH, 58f)
    val printerCharactersPerLine = getAsLiveData(
        PRINTER_CHARACTERS_PER_LINE, 32)
    val jobOrderDisclaimer = getAsLiveData(JOB_ORDER_DISCLAIMER,
        "Disclaimer: This document is provided for informational purposes only. It does not constitute an official receipt and should not be considered proof of payment.")

    suspend fun getPrinterAddress() = getValue(PRINTER_ADDRESS, "")
    suspend fun getPrinterWidth() = getValue(PRINTER_WIDTH, 58f)
    suspend fun getPrinterCharacters() = getValue(PRINTER_CHARACTERS_PER_LINE, 32)

    suspend fun updatePrinterName(value: String?) {
        update(value, PRINTER_NAME)
    }

    suspend fun updatePrinterAddress(value: String?) {
        update(value, PRINTER_ADDRESS)
    }

    suspend fun updatePrinterWidth(value: Float?) {
        update(value, PRINTER_WIDTH)
    }

    suspend fun updatePrinterCharacters(value: Int?) {
        update(value, PRINTER_CHARACTERS_PER_LINE)
    }
}