package com.melvin.ongandroid.core

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Resources provider
 * created on 5 May 2022 by Leonel Gomez
 *
 * @property context Application Context
 * @constructor Create empty Resources provider
 */
@Singleton
class ResourcesProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /**
     * Get string
     * created on 5 May 2022 by Leonel Gomez
     *
     * @param stringResId resource to get a string from the string.xml file
     * @param args optional arguments to getString function
     * @return
     */
    fun getString(@StringRes stringResId: Int, args: ArrayList<Any>? = arrayListOf()): String {
        return when (args) {
            null -> context.getString(stringResId)
            else -> context.getString(stringResId, *args.toArray())
        }
    }
}