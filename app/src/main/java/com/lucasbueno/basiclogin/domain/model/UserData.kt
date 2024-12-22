package com.lucasbueno.basiclogin.domain.model

data class UserData(
    val userId: String,
    val email: String,
    val userName: String?,
    val profilePictureUrl: String?
)
