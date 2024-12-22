package com.lucasbueno.basiclogin.di

import com.lucasbueno.basiclogin.data.remote.database.FirestoreDatabaseService
import com.lucasbueno.basiclogin.core.DatabaseService
import com.lucasbueno.basiclogin.data.repository.UserRepositoryImpl
import com.lucasbueno.basiclogin.domain.repository.UserRepository
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

    @Provides
    fun provideUserRepository(databaseService: DatabaseService): UserRepository {
        return UserRepositoryImpl(databaseService)
    }
}