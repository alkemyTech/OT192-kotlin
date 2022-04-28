package com.melvin.ongandroid.model

import com.google.gson.annotations.SerializedName

// data class that represents a contact response body
data class Contact(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("phone") val phone: String = "",
    @SerializedName("message") val message: String = "",
)