package com.example.models

/**
 * Student 1: Authentication & User Profile Model
 */
data class UserProfile(
    val uid: String = "",
    val fullName: String = "Alex Morgan",
    val email: String = "alex.morgan@university.edu",
    val phone: String = "+1 (555) 234-5678",
    val age: Int = 21,
    val bloodGroup: String = "O+",
    val emergencyContact: String = "+1 (555) 987-6543 (Mom)"
)
