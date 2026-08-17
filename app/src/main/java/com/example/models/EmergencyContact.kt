package com.example.models

/**
 * Student 5: Emergency Contacts Model
 */
data class EmergencyContact(
    val id: String = "",
    val name: String = "",
    val relationship: String = "",
    val phone: String = "",
    val isPrimary: Boolean = false
)
