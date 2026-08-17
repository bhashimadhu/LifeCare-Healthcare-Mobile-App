package com.example.models

/**
 * Student 2: Doctor Model
 */
data class Doctor(
    val id: String = "",
    val name: String = "",
    val specialization: String = "",
    val rating: Double = 4.9,
    val reviewCount: Int = 124,
    val experience: String = "10+ Years Experience",
    val consultationFee: String = "Rs. 2,000",
    val availableDays: String = "Mon - Fri",
    val about: String = "",
    val isAvailable: Boolean = true,
    val imageRes: String = "doctor_sarah"
)
