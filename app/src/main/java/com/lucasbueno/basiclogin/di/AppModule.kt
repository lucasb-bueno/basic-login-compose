package com.lucasbueno.basiclogin.di

import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.lucasbueno.basiclogin.core.DatabaseService
import com.lucasbueno.basiclogin.core.auth.FirebaseAuthClient
import com.lucasbueno.basiclogin.core.auth.GoogleAuthUiClient
import com.lucasbueno.basiclogin.data.remote.database.FirestoreDatabaseService
import com.lucasbueno.basiclogin.data.repository.UserRepositoryImpl
import com.lucasbueno.basiclogin.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideUserRepository(
        databaseService: DatabaseService,
        authProvider: FirebaseAuthClient
    ): UserRepository {
        return UserRepositoryImpl(databaseService = databaseService, authProvider = authProvider)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthClient(): FirebaseAuthClient {
        return FirebaseAuthClient()
    }

    @Provides
    @Singleton
    fun provideGoogleSignInClient(
        @ApplicationContext context: Context
    ): SignInClient {
        return Identity.getSignInClient(context)
    }

    @Provides
    @Singleton
    fun provideGoogleAuthUiClient(
        @ApplicationContext context: Context,
        googleSignInClient: SignInClient
    ): GoogleAuthUiClient {
        return GoogleAuthUiClient(context, googleSignInClient)
    }
}