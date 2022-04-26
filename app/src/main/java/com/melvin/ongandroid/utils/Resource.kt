package com.melvin.ongandroid.utils

/**
 * Wrapper class for handling states on API requests.
 * States:
 * [Success] ==> When Api Response is succesfull
 * [ErrorThrowable] ==> Handles Exception like IO
 * [ErrorApi] ==> Handles unsuccessfully request to API
 * [Loading] ==> Handles the state of Loading
 */
sealed class Resource<T>(
    val data: T? = null,
    val errorThrowable: Throwable? = null,
    val errorMessage:String?=null
) {
    class Success<T>(data: T): Resource<T>(data)
    class ErrorThrowable<T>(errorThrowable: Throwable, data: T? = null): Resource<T>(data, errorThrowable)
    class ErrorApi<T>(errorMessage:String): Resource<T>(errorMessage = errorMessage)
    class Loading<T>(data: T? = null): Resource<T>(data)

    /**
     * This Companion Object simplifies the uses of states, especially to handle errors in flow.
     */
    companion object{
        fun <T> success(data: T): Resource<T> = Success(data)
        fun <T> errorThrowable(errorThrowable: Throwable): Resource<T> = ErrorThrowable( errorThrowable)
        fun <T> errorApi(errorMessage:String): Resource<T> = ErrorApi(errorMessage)
        fun <T> loading(): Resource<T> = Loading()
    }
