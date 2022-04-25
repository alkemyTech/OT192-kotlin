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
}

/*
sealed class Resource<T>(
    val data: T? = null,
    val errorMessage: String? = null,
) {
    class Success<T>(data: T): Resource<T>(data)
    class Loading<T>(data: T? = null): Resource<T>(data)
    class ErrorThrowable<T>(errorMessage: String?, data: T? = null): Resource<T>(data, errorMessage)


    companion object{
        fun <T> success(data: T): Resource<T> = Success(data)
        fun <T> error(errorMessage: String): Resource<T> = ErrorThrowable( errorMessage)



    }

}
 */

/*
sealed class Resource<T>(
    val data: T? = null,
    val error: String? = null
) {
    class Success<T>(data: T): Resource<T>(data)
    class Loading<T>(data: T? = null): Resource<T>(data)
    class ErrorResource<T>(error: String , data: T? = null): Resource<T>(data, error)

    companion object{
        fun <T> success(data: T): Resource<T> = Success(data)
        fun <T> error(error: String): Resource<T> = ErrorResource( error)
        fun <T> loading(): Resource<T> = Loading()

    }

}
 */

/*
sealed class Resource<T>(
    val data: T? = null,
    val error: Throwable? = null
) {
    class Success<T>(data: T): Resource<T>(data)
    class Loading<T>(data: T? = null): Resource<T>(data)
    class ErrorResource<T>(throwable: Throwable, data: T? = null): Resource<T>(data, throwable)
    class LocalDb<T>(data: T): Resource<T>(data)

    companion object{
        fun <T> success(data: T): Resource<T> = Success(data)
        fun <T> error(error: Throwable): Resource<T> = ErrorResource( error)
        fun <T> local (data: T): Resource<T> = LocalDb(data)


    }

}
 */
/*
sealed class Resource2<T>(
    val data: T? = null,
    val message: String? = null
    //val error: Throwable? = null
) {
    class Success<T>(data: T): Resource<T>(data)
    class Loading<T>(data: T? = null): Resource<T>(data)
    class ErrorResource<T>(message: String, data: T? = null) : Resource<T>(data, message)
    //class ErrorResource<T>(throwable: Throwable, data: T? = null): Resource<T>(data, throwable)
    //class ErrorResource<T>(throwable: Throwable, data: T? = null): Resource<T>(data, throwable)
    //class Loading<T>(data: T? = null): Resource<T>(data)


}
*/

//sealed class Recurso(val data)

//sealed class Recurso<T>(val data: T? =null) {
//    data class Success(val data: T? =null) :Recurso<T>(data)
//    data class Local(val movies: List<Movie>) : UiHomeState()
//    object Loading : UiHomeState()
//    data class ErrorResource(val message: String, val moviesPopuular: List<Movie>)
//}
/*
sealed class UiHomeState {
    data class Success(val movieResponse: MovieResponse?) : UiHomeState()
    data class Local(val movies: List<Movie>) : UiHomeState()
    object Loading : UiHomeState()
    data class ErrorResource(val message: String, val moviesPopuular: List<Movie>)
}
 */

/*
sealed class Resource<T>(
    val data: T? = null,
    val errorMessage: String? = null,
    val throwable: Throwable? = null
) {
    class Success<T>(data: T): Resource<T>(data)
    class Loading<T>(data: T? = null): Resource<T>(data)
    class ErrorResource<T>(errorMessage: String, data: T? = null, throwable: Throwable): Resource<T>(data, errorMessage, throwable)

    companion object{
        fun <T> success(data: T): Resource<T> = Success(data)
        fun <T> error(error: String, throwable: Throwable, data: T? = null): Resource<T> = ErrorResource( error,throwable,data )
        fun <T> loading(): Resource<T> = Loading()

    }

}
 */