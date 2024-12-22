package com.lucasbueno.basiclogin.data.repository

import com.lucasbueno.basiclogin.core.DatabaseService
import com.lucasbueno.basiclogin.domain.model.UserData
import com.lucasbueno.basiclogin.domain.repository.UserRepository

class UserRepositoryImpl(private val databaseService: DatabaseService): UserRepository {

    override suspend fun createUser(user: UserData): Result<String> {
        return databaseService.addDocument("userProfile", user)
    }
}