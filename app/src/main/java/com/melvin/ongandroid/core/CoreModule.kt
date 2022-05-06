package com.melvin.ongandroid.core

import android.content.Context
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

}