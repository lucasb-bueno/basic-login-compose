package com.lucasbueno.basiclogin.data.repository

import com.lucasbueno.basiclogin.core.AuthProvider
import com.lucasbueno.basiclogin.core.DatabaseService
import com.lucasbueno.basiclogin.domain.model.UserData
import com.lucasbueno.basiclogin.domain.repository.UserRepository

class UserRepositoryImpl(
    private val databaseService: DatabaseService,
    private val authProvider: AuthProvider
) :
    UserRepository {

    override suspend fun createUser(user: UserData): Result<Unit> {
        return databaseService.addDocument(collection = "userProfile", userId = user.userId, data = user)
    }

    override suspend fun getUser(
        userId: String,
    ): Result<UserData> =
        databaseService.getDocument(
            collection = "userProfile",
            documentId = userId,
            clazz = UserData::class.java
        )

    override suspend fun getUserId(): Result<String> = authProvider.getLoggedInUserId()
}