package com.example.sciflareapplication.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    val name: String?,
    val email: String?,
    val mobile: String?,
    val gender: String?,
    @PrimaryKey
    val _id: String
)



