package com.csi.palabakosys.app.preferences.printer

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csi.palabakosys.app.preferences.printer.browser.PrinterDevice
import com.csi.palabakosys.model.Rule
import com.csi.palabakosys.preferences.AppPreferenceRepository
import com.csi.palabakosys.preferences.PrinterSettings
import com.csi.palabakosys.util.InputValidation
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PrinterSettingsViewModel

@Inject
constructor(
    private val preferenceRepository: AppPreferenceRepository,
    @ApplicationContext context: Context
) : ViewModel() {
    private val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // UUID for SPP (Serial Port Profile)
    val validation = MutableLiveData(InputValidation())

    private val currentSettings = preferenceRepository.printerSettings()
    private val _dataState = MutableLiveData<DataState>()
    val dataState: LiveData<DataState> = _dataState

    val printerName = MutableLiveData(currentSettings.name)
    val macAddress = MutableLiveData(currentSettings.address)
    val dpi = MutableLiveData(currentSettings.dpi.toString())
    val width = MutableLiveData(currentSettings.width.toString())
    val charactersPerLine = MutableLiveData(currentSettings.character.toString())
    val sampleText = MutableLiveData("Sample text")

    fun clearError(key: String = "") {
        validation.value = validation.value?.removeError(key)
    }

    fun resetState() {
        _dataState.value = DataState.StateLess
    }

    fun openPrinterBrowser() {
        _dataState.value = DataState.OpenPrinterBrowser(
            PrinterDevice(
                printerName.value, macAddress.value
            )
        )
    }

    fun update() {
        val name = printerName.value
        val address = macAddress.value
        val dpi = dpi.value
        val width = width.value
        val charactersPerLine = charactersPerLine.value


        val _validation = InputValidation().apply {
            addRule("printerName", name, arrayOf(Rule.Required))
            addRule("macAddress", address, arrayOf(Rule.Required))
            addRule("dpi", dpi, arrayOf(Rule.Required, Rule.IsNumeric))
            addRule("width", width, arrayOf(Rule.Required, Rule.IsNumeric))
            addRule("charactersPerLine", charactersPerLine, arrayOf(Rule.Required, Rule.IsNumeric))
        }

        validation.value = _validation

        if(_validation.isInvalid()) {
            return
        }

        val currentPrinter = PrinterSettings(
            printerName.value,
            macAddress.value,
            dpi?.toInt() ?: 203,
            width?.toFloat() ?: 58f,
            charactersPerLine?.toInt() ?: 32
        )
        preferenceRepository.setCurrentPrinter(currentPrinter)
        _dataState.value = DataState.Save
    }

    fun setPrinterDevice(printerDevice: PrinterDevice) {
        printerName.value = printerDevice.deviceName
        macAddress.value = printerDevice.macAddress
    }

    private val bluetoothManager: BluetoothManager by lazy {
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        bluetoothManager.adapter
    }

    private lateinit var printer: EscPosPrinter
    fun testPrint() {
        Thread(Runnable {
            try {
                val device = bluetoothAdapter.getRemoteDevice(macAddress.value)
                val bluetoothConnection = BluetoothConnection(device)

                val dpi = dpi.value?.toInt() ?: 203
                val width = width.value?.toFloat() ?: 58f
                val character = charactersPerLine.value?.toInt() ?: 32

                printer = EscPosPrinter(bluetoothConnection, dpi, width, character)

                printer.printFormattedTextAndCut(sampleText.value)
                printer.disconnectPrinter()
            } catch (e: Exception) {
                println("Problem establishing connection")
                e.printStackTrace()
            } finally {
                try {
                    printer.disconnectPrinter()
                } catch (_: Exception){}
            }
        }).start()
    }

    sealed class DataState {
        object StateLess: DataState()
        data class OpenPrinterBrowser(val currentPrinter: PrinterDevice) : DataState()
        object Save: DataState()
    }
}