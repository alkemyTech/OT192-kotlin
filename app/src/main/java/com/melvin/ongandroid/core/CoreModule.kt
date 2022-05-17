package com.melvin.ongandroid.core

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.melvin.ongandroid.repository.OngAuthRepository
import com.melvin.ongandroid.repository.OngRepository
import com.melvin.ongandroid.services.OngAuthDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    //Injection of the singleton ResourcesProvider getting context as parameter
    //created on 5 May 2022 by Leonel Gomez
    @Singleton
    @Provides
    fun provideResourcesProvider(@ApplicationContext context: Context): ResourcesProvider =
        ResourcesProvider(context)

    //Injection of the singleton FirebaseAuth
    //created on 15 May 2022 by Leonel Gomez
    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    // Data source provides
    // created on 15 May 2022 by Leonel Gomez
    @Provides
    fun provideOngAuthDataSource(auth: FirebaseAuth): OngAuthDataSource =
        OngAuthDataSource(auth)

    // Repository provides
    // created on 15 May 2022 by Leonel Gomez
    @Provides
    fun provideOngRepository(ongAuthDataSource: OngAuthDataSource) =
        OngAuthRepository(ongAuthDataSource)

}