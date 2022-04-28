package com.melvin.ongandroid.services.firebase

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent

class FirebaseEvent {
    companion object{
        // firebase analytics instance to log events
        fun setEvent(context: Context, eventName: String){
            val analytics = FirebaseAnalytics.getInstance(context)
            analytics.logEvent(eventName){}
        }
    }
}