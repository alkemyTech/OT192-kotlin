package com.melvin.ongandroid.repository

import com.melvin.ongandroid.services.OngAuthDataSource
import javax.inject.Inject

/**
 * ONG auth repository
 * created on 15 May 2022 by Leonel Gomez
 *
 * @property OngAuthDataSource Firebase Service
 * @constructor Create empty Ong auth repository
 */
class OngAuthRepository @Inject constructor(private val OngAuthDataSource: OngAuthDataSource) {

    suspend fun signInWithCredential(token: String) =
        OngAuthDataSource.signInWithCredential(token)

    fun signOut() {
        OngAuthDataSource.signOut()
    }

}