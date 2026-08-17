package com.example.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @PrimaryKey
    val id: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val password: String,
    val age: Int = 24,
    val bloodGroup: String = "O+",
    val emergencyContact: String = "+94 71 987 6543 (Amma)",
    val createdAt: Long = System.currentTimeMillis()
)
