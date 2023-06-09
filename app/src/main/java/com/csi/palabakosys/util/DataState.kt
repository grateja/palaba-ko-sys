package com.csi.palabakosys.util

sealed class DataState<out R> {
    object StateLess : DataState<Nothing>()
    data class ConfirmSave<out T> (val data: T) : DataState<T>()
    data class ConfirmDelete<out T>(val data: T) : DataState<T>()
    object Loading : DataState<Nothing>()
    data class Loaded<out T>(val data: T) : DataState<T>()
    object CancelOperation : DataState<Nothing>()
    data class Invalidate(val message: String) : DataState<Nothing>()
    data class InvalidInput<out T>(val inputValidation: InputValidation) : DataState<T>()
    data class Error(val exception: Exception) : DataState<Nothing>()
    data class RequestExit(val promptPass: Boolean) : DataState<Nothing>()
}