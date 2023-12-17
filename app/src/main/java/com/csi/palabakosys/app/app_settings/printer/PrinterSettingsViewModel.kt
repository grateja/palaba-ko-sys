package com.csi.palabakosys.app.app_settings.printer

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.app.app_settings.printer.browser.PrinterDevice
import com.csi.palabakosys.model.Rule
import com.csi.palabakosys.preferences.AppPreferenceRepository
import com.csi.palabakosys.preferences.PrinterSettings
import com.csi.palabakosys.room.repository.DataStoreRepository
import com.csi.palabakosys.util.InputValidation
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PrinterSettingsViewModel

@Inject
constructor(
    private val dataStoreRepository: DataStoreRepository,
//    private val preferenceRepository: AppPreferenceRepository,
    @ApplicationContext context: Context
) : ViewModel() {
    private val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // UUID for SPP (Serial Port Profile)
    val validation = MutableLiveData(InputValidation())

//    private val currentSettings = preferenceRepository.printerSettings()
    private val _dataState = MutableLiveData<DataState>()
    val dataState: LiveData<DataState> = _dataState

    val printerName = dataStoreRepository.printerName //MutableLiveData(currentSettings.name)
    val macAddress = dataStoreRepository.printerAddress //MutableLiveData(currentSettings.address)
//    val dpi = MutableLiveData(currentSettings.dpi.toString())
    val width = dataStoreRepository.printerWidth //MutableLiveData(currentSettings.width.toString())
    val charactersPerLine = dataStoreRepository.printerCharactersPerLine //MutableLiveData(currentSettings.character.toString())
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
//        val dpi = dpi.value
        val width = width.value
//        val charactersPerLine = charactersPerLine.value


        val _validation = InputValidation().apply {
            addRule("printerName", name, arrayOf(Rule.Required))
            addRule("macAddress", address, arrayOf(Rule.Required))
//            addRule("dpi", dpi, arrayOf(Rule.Required, Rule.IsNumeric))
            addRule("width", width, arrayOf(Rule.Required, Rule.IsNumeric))
//            addRule("charactersPerLine", charactersPerLine, arrayOf(Rule.Required, Rule.IsNumeric))
        }

        validation.value = _validation

        if(_validation.isInvalid()) {
            return
        }

//        val currentPrinter = PrinterSettings(
//            printerName.value,
//            macAddress.value,
//            dpi?.toInt() ?: 203,
//            width?.toFloat() ?: 58f,
//            charactersPerLine?.toInt() ?: 32
//        )
//        preferenceRepository.setCurrentPrinter(currentPrinter)
//        _dataState.value = DataState.Save
    }

    fun setPrinterDevice(printerDevice: PrinterDevice) {
        viewModelScope.launch {
            dataStoreRepository.updatePrinterName(printerDevice.deviceName)
            dataStoreRepository.updatePrinterAddress(printerDevice.macAddress)
        }
//        printerName.value = printerDevice.deviceName
//        macAddress.value = printerDevice.macAddress
    }

    private val bluetoothManager: BluetoothManager by lazy {
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        bluetoothManager.adapter
    }

    private lateinit var printer: EscPosPrinter
    fun testPrint() {
//        val settings = PrinterSettings(
//            printerName.value,
//            macAddress.value,
//            dpi.value?.toInt() ?: 203,
//            width.value?.toFloat() ?: 58f,
//            charactersPerLine.value?.toInt() ?: 32
//        )
        val text = sampleText.value ?: "Sample text"
//        val address = macAddress.value
//        val width = width.value
//        val charactersPerLine = charactersPerLine.value
        _dataState.value = DataState.StartTestPrint(text)
    }

    fun openPrinterWidth() {
        val width = width.value ?: 32f
        _dataState.value = DataState.OpenPrinterWidth(width)
    }

    fun openPrinterCharactersPerLine() {
        val charactersPerLine = charactersPerLine.value ?: 58
        _dataState.value = DataState.OpenPrinterCharactersPerLine(charactersPerLine)
    }

    fun setPrinterWidth(width: Float) {
        viewModelScope.launch {
            dataStoreRepository.updatePrinterWidth(width)
        }
    }

    fun setPrinterCharactersPerLine(charactersPerLine: Int) {
        viewModelScope.launch {
            dataStoreRepository.updatePrinterCharacterLength(charactersPerLine)
        }
    }

    sealed class DataState {
        object StateLess: DataState()
        data class OpenPrinterBrowser(val currentPrinter: PrinterDevice) : DataState()
        data class OpenPrinterWidth(val width: Float): DataState()
        data class OpenPrinterCharactersPerLine(val charactersPerLine: Int?): DataState()
        data class StartTestPrint(val payload: String): DataState()
        object Save: DataState()
    }
}