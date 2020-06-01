package com.example.superherosquadmaker.data.api

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T : Any> {

    data class Success<out T : Any>(val output : T) : Result<T>()
    data class Error(val exception: Exception)  : Result<Nothing>()
}
