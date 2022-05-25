package com.melvin.ongandroid.services

import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.melvin.ongandroid.model.login.DataUser
import com.melvin.ongandroid.model.login.User
import com.melvin.ongandroid.utils.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * ONG auth data source
 * Service to provide Firebase Auth functions
 * created on 15 May 2022 by Leonel Gomez
 *
 * @property auth injection of FirebaseAuth
 * @constructor Create empty ONG auth data source
 */
class OngAuthDataSource @Inject constructor(
    private val auth: FirebaseAuth,
) {

    // Register in firebase Auth the Facebook login - 15/05/2022 L.Gomez
    suspend fun signInWithCredential(token: String): Resource<DataUser> {
        // Translation from Facebook Login access token to Firebase Auth credential
        val credential = FacebookAuthProvider.getCredential(token)
        // Creation of empty Data User to return the object
        val fbUser = DataUser(User())

        try {
            // Firebase function to log in
            val result = auth.signInWithCredential(credential)
                .await()
            val myUser = auth.currentUser

            result.user.let {
                return if (it != null) {
                    fbUser.token = it.uid
                    fbUser.user.email = it.email.toString()
                    if (myUser != null) {
                        fbUser.user.name = myUser.displayName ?: ""
                    }
                    Resource.Success(fbUser)
                } else
                    Resource.ErrorThrowable(Throwable("Error"))

            }
        } catch (e: Exception) {
            return Resource.ErrorThrowable(Throwable(e))
        }
    }

    // Sign Out from firebase Auth - 15/05/2022 L.Gomez
    fun signOut() {
        auth.signOut()
    }
}