package com.melvin.ongandroid.model.login


import com.google.gson.annotations.SerializedName

data class RegisterUser(
    var name: String = "",
    var email: String = "",
    var password: String = ""
)