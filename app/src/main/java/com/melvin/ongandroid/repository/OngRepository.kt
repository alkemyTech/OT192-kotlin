package com.melvin.ongandroid.repository


import com.melvin.ongandroid.model.*
import com.melvin.ongandroid.model.login.DataUser
import com.melvin.ongandroid.model.login.RegisterUser
import com.melvin.ongandroid.services.OngApiService
import com.melvin.ongandroid.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class OngRepository @Inject constructor(private val apiService: OngApiService) {

    // Function that emits a GenericResponse, containing a list of Testimonials
    suspend fun getTestimonials(): Flow<GenericResponse<MutableList<HomeTestimonials>>> = flow {
        emit(apiService.getTestimonials())
    }

    /*
    Function that emits a NewsResponse, containing list Of News
     */
    suspend fun fetchLatestNews(): Flow<Resource<NewsResponse?>> = flow {
        val response = apiService.fetchLatestNews()

        when (response.isSuccessful) {
            true -> emit(Resource.Success(response.body()))
            false -> emit(Resource.ErrorApi(response.errorBody().toString()))
        }
    }.catch { e: Throwable ->
        emit(Resource.ErrorThrowable(e))
    }


    // Function that emits a GenericResponse, containing a contact response
    suspend fun sendContact(contact: Contact): Flow<GenericResponse<List<Contact>>> = flow {
        emit(apiService.sendContact(contact))
    }

    /**
     * Get slides
     * Repository function that return the list of slides from the API
     * created on 24 April 2022 by Leonel Gomez
     *
     * @return the list of Slides emitted by upstream
     */
    suspend fun getSlides(): Flow<GenericResponse<List<Slide>>> = flow {
        //Collects the value emitted by the upstream
        emit(apiService.getSlides())

    }

    /**
     * Fetch Members from [https://ongapi.alkemy.org/api/members].
     * When:
     * [retrofit2.Response] is successfull and [GenericResponse] success is true -->
     * Emits [Resource.Success]
     *
     * [retrofit2.Response] is not successfull and [GenericResponse] success false ->
     * emits [Resource.ErrorApi]
     *
     * Exceptions  are caught with flow API [catch]. --> Emits [Resource.ErrorThrowable]
     */

    suspend fun fetchMembers() =
        flow<Resource<GenericResponse<List<Members>>>> {
            val responseMembers = apiService.fetchMembers()
            when {
                responseMembers.isSuccessful && responseMembers.body()!!.success -> {
                    emit(Resource.success(responseMembers.body()!!))
                }

                responseMembers.isSuccessful && !responseMembers.body()!!.success -> {
                    emit(Resource.errorApi("Error from API"))
                }

                else -> emit(Resource.errorApi(responseMembers.errorBody().toString()))
            }
        }.catch { e -> emit(Resource.errorThrowable(e)) }

    /**
     * Get activities
     * created on 1 May 2022 by Leonel Gomez
     *
     * @return emits a Resource, containing list Of Activities
     */
    suspend fun getActivities(): Flow<Resource<GenericResponse<List<Activity>>>> = flow {
        val response = apiService.getActivities()

        when (response.isSuccessful) {
            true -> {
                val body = response.body()
                if (body != null) {
                    if (body.success)
                        emit(Resource.Success(body))
                    else
                        emit(Resource.errorApi("Error from API"))
                }
            }
            false -> emit(Resource.ErrorApi(response.errorBody().toString()))
        }
    }.catch { e: Throwable ->
        emit(Resource.ErrorThrowable(e))
    }

    // Function that emit a Resource, containing a user response when response is successfully received
    suspend fun login(
        email: String,
        password: String
    ): Flow<Resource<GenericResponse<DataUser>>> = flow {
        val response = apiService.login(email, password)

        when (response.isSuccessful) {
            true -> {
                val body = response.body()
                if (body != null) {
                    if (body.success)
                        emit(Resource.Success(body))
                    else
                        emit(Resource.errorApi("Error from API"))
                }
            }
            false -> emit(Resource.ErrorApi(response.errorBody().toString()))
        }
    }.catch { e: Throwable ->
        emit(Resource.ErrorThrowable(e))
    }

    /**
     * Post new User with[registerUser]  and handles Api Response and Exceptions
     */

    suspend fun signUpUser(registerUser: RegisterUser) = flow {
        val response = apiService.postSignUp(registerUser)
        when(response.isSuccessful){
            true ->{
                val responseBody = response.body()

                if(responseBody != null){
                    if (responseBody.success) emit(Resource.success(response.body()!!))
                    else emit(Resource.errorApi("Emamil ya utilizado"))
                }
                else emit(Resource.errorApi("Error"))

            }
            false ->{
              emit(Resource.ErrorApi(response.errorBody().toString()))
            }
        }
    }.catch { e: Throwable ->
        emit(Resource.ErrorThrowable(e))
    }

}