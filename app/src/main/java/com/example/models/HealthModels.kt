package com.example.models

/**
 * Student 4: Medication Reminders, Medical Records & Health Status Models
 */
data class MedicationReminder(
    val id: String = "",
    val medicineName: String = "",
    val dosage: String = "1 Tablet",
    val time: String = "08:00 AM",
    val frequency: String = "Daily",
    val startDate: String = "Today",
    val endDate: String = "Ongoing",
    val isTaken: Boolean = false
)

data class MedicalRecord(
    val id: String = "",
    val title: String = "",
    val recordType: String = "Prescription", // Prescription, Lab Reports, X-Ray Reports, Vaccination, Health Summary
    val doctorOrClinic: String = "City General Hospital",
    val date: String = "",
    val description: String = ""
)

data class HealthStatus(
    val heartRate: Int = 0,
    val heartStatus: String = "No Data",
    val bloodPressure: String = "",
    val bpStatus: String = "No Data",
    val waterGlasses: Int = 0,
    val maxWaterGlasses: Int = 8,
    val steps: Int = 0,
    val calories: Int = 0,
    val sleepHours: Int = 0,
    val sleepMinutes: Int = 0
)
