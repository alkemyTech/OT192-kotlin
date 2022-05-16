package com.melvin.ongandroid.model

import com.google.gson.annotations.SerializedName

sealed class RegisterResponse(
    @SerializedName("email") val email: List<String>? = null,
    @SerializedName("name") val name: List<String>? = null,
    @SerializedName("password") val password: List<String>? = null,
)
