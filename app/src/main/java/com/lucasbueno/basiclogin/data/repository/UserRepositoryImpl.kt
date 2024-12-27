package com.lucasbueno.basiclogin.data.repository

import com.google.firebase.auth.FirebaseUser
import com.lucasbueno.basiclogin.core.AuthProvider
import com.lucasbueno.basiclogin.core.DatabaseService
import com.lucasbueno.basiclogin.domain.FireStoreCollections.USER_PROFILE
import com.lucasbueno.basiclogin.domain.model.UserData
import com.lucasbueno.basiclogin.domain.repository.UserRepository

class UserRepositoryImpl(
    private val databaseService: DatabaseService,
    private val authProvider: AuthProvider
) :
    UserRepository {

    override suspend fun createUser(user: UserData): Result<Unit> {
        return databaseService.addDocument(
            collection = USER_PROFILE,
            userId = user.userId,
            data = user
        )
    }

    override suspend fun getUser(
        userId: String,
    ): Result<UserData> =
        databaseService.getDocument(
            collection = USER_PROFILE,
            documentId = userId,
            clazz = UserData::class.java
        )

    override suspend fun getUserId(): Result<String> = authProvider.getLoggedInUserId()

    override suspend fun getSignedInUser(): Result<FirebaseUser> = authProvider.getSignedInUser()

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return authProvider.sendPasswordResetEmail(email)
    }
}