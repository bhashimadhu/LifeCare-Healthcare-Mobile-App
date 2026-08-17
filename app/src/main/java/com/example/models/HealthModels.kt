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
    val heartRate: Int = 72,
    val heartStatus: String = "Normal",
    val bloodPressure: String = "120/80",
    val bpStatus: String = "Optimal",
    val waterGlasses: Int = 5,
    val maxWaterGlasses: Int = 8,
    val steps: Int = 8432,
    val calories: Int = 520,
    val sleepHours: Int = 7,
    val sleepMinutes: Int = 30
)
