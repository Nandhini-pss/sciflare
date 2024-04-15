package com.example.sciflareapplication.model


data class UserReq(
    val name: String?,
    val email: String?,
    val mobile: String?,
    val gender: String?,
)

data class UserResponse(
    val name: String,
    val email: String,
    val mobile: String,
    val gender: String,
    val _id: String
)
