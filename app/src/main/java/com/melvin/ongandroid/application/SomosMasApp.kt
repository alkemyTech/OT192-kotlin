package com.melvin.ongandroid.application

import android.annotation.SuppressLint
import android.app.Application
import com.melvin.ongandroid.repository.preferences.Prefs
import dagger.hilt.android.HiltAndroidApp

/**
 * Class used for DI with Hilt
 */
@HiltAndroidApp
class SomosMasApp: Application() {

    // Initialize the preferences class with the application context.
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var prefs: Prefs
    }

    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
    }
}