package com.melvin.ongandroid.repository.preferences

import android.content.Context

// Class to manage the shared preferences
class Prefs (val context: Context) {
    private val SHARED_NAME = "DataBase"
    private val SHARED_USER_TOKEN = "userToken"

    private val storage = context.getSharedPreferences(SHARED_NAME, 0)

    // Save the user token in the shared preferences
    fun saveUserToken(token:String){
        storage.edit().putString(SHARED_USER_TOKEN, token).apply()
    }

    // Get the user token from the shared preferences
    fun getUserToken():String = storage.getString(SHARED_USER_TOKEN,"")!!
}