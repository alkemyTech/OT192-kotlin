package com.melvin.ongandroid.model.login

import com.google.gson.annotations.SerializedName

// data class that represents the user data response from the server
data class DataUser(
    @SerializedName("user") val user: User,
    @SerializedName("token") var token: String = ""
)