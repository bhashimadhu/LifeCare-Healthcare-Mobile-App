package com.example.models

/**
 * Student 2: Appointment Model with Full CRUD Support
 */
data class Appointment(
    val id: String = "",
    val doctorId: String = "",
    val doctorName: String = "",
    val specialization: String = "",
    val patientName: String = "",
    val patientPhone: String = "",
    val reason: String = "General Consultation",
    val date: String = "",
    val time: String = "",
    val consultationFee: String = "Rs. 2,000",
    val status: String = "Confirmed" // Pending, Confirmed, Completed, Cancelled
)
