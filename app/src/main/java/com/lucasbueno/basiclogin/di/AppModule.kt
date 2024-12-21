package com.lucasbueno.basiclogin.di

import com.lucasbueno.basiclogin.data.remote.database.FirestoreDatabaseService
import com.lucasbueno.basiclogin.domain.DatabaseService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabaseService(): DatabaseService {
        return FirestoreDatabaseService()
    }
}