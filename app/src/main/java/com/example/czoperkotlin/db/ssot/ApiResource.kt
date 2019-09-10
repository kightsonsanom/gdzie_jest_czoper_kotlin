package com.example.czoperkotlin.db.ssot

sealed class ApiResource<out T> {
    class Loading<out T> : ApiResource<T>()
    data class Success<out T>(val data: T?) : ApiResource<T>()
    data class Failure<out T>(val throwable: Throwable) : ApiResource<T>()
    data class Invalid<out T>(val throwable: Throwable) : ApiResource<T>()
}