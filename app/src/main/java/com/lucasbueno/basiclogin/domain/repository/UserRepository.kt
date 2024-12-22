package com.lucasbueno.basiclogin.domain.repository

import com.lucasbueno.basiclogin.domain.model.UserData

interface UserRepository {
    suspend fun createUser(user: UserData): Result<String>
}