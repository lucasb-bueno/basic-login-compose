package com.lucasbueno.basiclogin.core

sealed interface DataState<out T> {
    data object Default : DataState<Nothing>
    data object Loading : DataState<Nothing>
    data class Success<T>(val data: T?) : DataState<T>
    data class Error(val message: String) : DataState<Nothing>
}