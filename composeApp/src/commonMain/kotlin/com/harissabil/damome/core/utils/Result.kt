package com.harissabil.damome.core.utils


sealed interface Result<out T> {
    data class Success<out T>(val data: T) : Result<T>
    data class Error(val statusCode: Int? = null, val message: String) : Result<Nothing>
}