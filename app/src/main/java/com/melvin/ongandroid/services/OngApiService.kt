package com.melvin.ongandroid.services

import com.melvin.ongandroid.model.*
import com.melvin.ongandroid.model.login.DataUser
import com.melvin.ongandroid.model.login.RegisterUser
import retrofit2.Response
import retrofit2.http.*


interface OngApiService {

    // Get testimonials from the API
    @GET("testimonials")
    suspend fun getTestimonials(): GenericResponse<MutableList<HomeTestimonials>>

    @GET("news")
    suspend fun fetchLatestNews() : Response<NewsResponse>


    // Post a new contact to the API
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("contacts")
    suspend fun sendContact(@Body contact: Contact): GenericResponse<List<Contact>>

    /**
     * Get slides
     * slides request to API
     * created on 24 April 2022 by Leonel Gomez
     *
     * @param search    String: Term of search
     * @param skip  Integer: Total entries to skip in result
     * @param limit Integer: Total entries to retrieve
     * @return a GenericResponse (success, data, message) where data is of type list of slides
     */
    @GET("slides")
    suspend fun getSlides(
        @Query("search") search: String? = null,
        @Query("skip") skip: Int? = null,
        @Query("limit") limit: Int? = null,
    ): GenericResponse<List<Slide>>

    /**
     * Get activities
     * [https://ongapi.alkemy.org/api/activities]
     * created on 1 May 2022 by Leonel Gomez
     *
     * @return a [Response] (RetroFit) of [GenericResponse] with a [List] of [Activity]
     */
    @GET("activities")
    suspend fun getActivities(): Response<GenericResponse<List<Activity>>>

    /** Fetch Members from
     * [https://ongapi.alkemy.org/api/members]
     * Returns a [Response] (RetroFit) of [GenericResponse] with
     * a [List] of [Members]
     */
    @GET("members")
    suspend fun fetchMembers(): Response<GenericResponse<List<Members>>>

    // Login the user to the API
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<GenericResponse<DataUser>>

    /**
     * Makes post Request with new User
     */

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("register")
    suspend fun postSignUp(@Body registerUser: RegisterUser): Response<GenericResponse<DataUser>>

    suspend fun fetchTestimonials(): Response<GenericResponse<List<Testimonial>>>


}