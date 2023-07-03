package com.csi.palabakosys.util

import com.csi.palabakosys.app.auth.LoginCredentials

sealed class DataState<out R> {
    object StateLess : DataState<Nothing>()
    data class SaveSuccess<out T> (val data: T) : DataState<T>()
    data class DeleteSuccess<out T>(val data: T) : DataState<T>()
    data class Invalidate(val message: String) : DataState<Nothing>()
    data class InvalidInput<out T>(val inputValidation: InputValidation) : DataState<T>()
    object ValidationPassed : DataState<Nothing>()
    data class AuthenticationPassed<out T>(val loginCredentials: LoginCredentials) : DataState<T>()
    data class RequestExit(val promptPass: Boolean) : DataState<Nothing>()
}