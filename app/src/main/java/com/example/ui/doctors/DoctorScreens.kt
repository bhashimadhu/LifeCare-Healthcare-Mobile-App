package com.example.ui.doctors

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.models.Appointment
import com.example.models.Doctor
import com.example.models.UserProfile
import com.example.ui.theme.LifeCareBackground
import com.example.ui.theme.LifeCareBorder
import com.example.ui.theme.LifeCareEmergency
import com.example.ui.theme.LifeCarePeach
import com.example.ui.theme.LifeCarePeachLight
import com.example.ui.theme.LifeCareSurface
import com.example.ui.theme.LifeCareTeal
import com.example.ui.theme.LifeCareTealDark
import com.example.ui.theme.LifeCareTealLight
import com.example.ui.theme.LifeCareTextMuted
import com.example.ui.theme.LifeCareTextPrimary
import com.example.ui.theme.LifeCareTextSecondary

/**
 * Student 2: Find Doctors Screen
 */
@Composable
fun DoctorListScreen(
    doctors: List<Doctor>,
    onSelectDoctorForBooking: (Doctor) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var viewingDoctorDetails by remember { mutableStateOf<Doctor?>(null) }

    val categories = listOf("All", "Cardiologist", "Dentist", "General", "Pediatrician", "Dermatologist")

    val filteredDoctors = doctors.filter { doc ->
        val matchesCategory = if (selectedCategory == "All") true else doc.specialization.contains(selectedCategory, ignoreCase = true)
        val matchesSearch = doc.name.contains(searchQuery, ignoreCase = true) || doc.specialization.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LifeCareBackground)
            .padding(16.dp)
    ) {
        Text(
            text = "Find Doctors",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = LifeCareTextPrimary
        )
        Text(
            text = "Book consultations with verified medical specialists",
            fontSize = 13.sp,
            color = LifeCareTextSecondary,
            modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
        )

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search doctors or specialists...", color = LifeCareTextMuted) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = LifeCareTeal) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear", tint = LifeCareTextSecondary)
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = LifeCareSurface,
                unfocusedContainerColor = LifeCareSurface,
                focusedBorderColor = LifeCareTeal,
                unfocusedBorderColor = LifeCareBorder
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("doctor_search_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Category Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { cat ->
                val isSelected = selectedCategory == cat
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) LifeCareTeal else LifeCareSurface,
                    border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, LifeCareBorder),
                    modifier = Modifier.clickable { selectedCategory = cat }
                ) {
                    Text(
                        text = cat,
                        color = if (isSelected) Color.White else LifeCareTextSecondary,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Doctor List
        if (filteredDoctors.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.MedicalServices,
                        contentDescription = null,
                        tint = LifeCareTextMuted,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No doctors found",
                        fontWeight = FontWeight.Bold,
                        color = LifeCareTextSecondary
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredDoctors, key = { it.id }) { doctor ->
                    DoctorCardItem(
                        doctor = doctor,
                        onViewDetails = { viewingDoctorDetails = doctor },
                        onBook = { onSelectDoctorForBooking(doctor) }
                    )
                }
            }
        }
    }

    // Doctor Details Dialog
    if (viewingDoctorDetails != null) {
        val doc = viewingDoctorDetails!!
        AlertDialog(
            onDismissRequest = { viewingDoctorDetails = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = LifeCareTealLight,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Image(
                            painter = painterResource(
                                id = if (doc.imageRes == "doctor_alex") R.drawable.doctor_alex else R.drawable.doctor_sarah
                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(doc.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(doc.specialization, color = LifeCareTealDark, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                }
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Rating", fontSize = 12.sp, color = LifeCareTextSecondary)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("${doc.rating} (${doc.reviewCount})", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                        Column {
                            Text("Experience", fontSize = 12.sp, color = LifeCareTextSecondary)
                            Text(doc.experience, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Column {
                            Text("Consultation Fee", fontSize = 12.sp, color = LifeCareTextSecondary)
                            Text(doc.consultationFee, fontWeight = FontWeight.Bold, color = LifeCareTealDark, fontSize = 14.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = LifeCareBorder)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Available Timing", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = LifeCareTextPrimary)
                    Text(doc.availableDays, fontSize = 13.sp, color = LifeCareTextSecondary, modifier = Modifier.padding(top = 2.dp))

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("About Doctor", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = LifeCareTextPrimary)
                    Text(doc.about, fontSize = 13.sp, color = LifeCareTextSecondary, lineHeight = 18.sp, modifier = Modifier.padding(top = 2.dp))
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val selected = viewingDoctorDetails
                        viewingDoctorDetails = null
                        if (selected != null) onSelectDoctorForBooking(selected)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Book Appointment", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewingDoctorDetails = null }) {
                    Text("Close", color = LifeCareTextSecondary)
                }
            }
        )
    }
}

@Composable
fun DoctorCardItem(
    doctor: Doctor,
    onViewDetails: () -> Unit,
    onBook: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = LifeCareSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Doctor Photo
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = LifeCareTealLight,
                    modifier = Modifier.size(68.dp)
                ) {
                    Image(
                        painter = painterResource(
                            id = if (doctor.imageRes == "doctor_alex") R.drawable.doctor_alex else R.drawable.doctor_sarah
                        ),
                        contentDescription = doctor.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                // Info Column
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = doctor.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = LifeCareTextPrimary
                        )

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFFF9E6),
                            modifier = Modifier.padding(start = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFB300),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "${doctor.rating}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB78103)
                                )
                            }
                        }
                    }

                    Text(
                        text = doctor.specialization,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LifeCareTealDark
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = doctor.experience,
                            fontSize = 12.sp,
                            color = LifeCareTextSecondary
                        )
                        Text(
                            text = " • ",
                            fontSize = 12.sp,
                            color = LifeCareTextMuted
                        )
                        Text(
                            text = doctor.consultationFee,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = LifeCareTextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = LifeCareBorder)
            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons: View & Book
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onViewDetails,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = LifeCareTealDark),
                    border = androidx.compose.foundation.BorderStroke(1.dp, LifeCareTeal),
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .testTag("view_doctor_${doctor.id}")
                ) {
                    Text("View Profile", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onBook,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal),
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .testTag("book_doctor_${doctor.id}")
                ) {
                    Text("Book Visit", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

/**
 * Student 2: Appointment Booking Screen
 */
@Composable
fun BookAppointmentScreen(
    doctor: Doctor,
    currentUser: UserProfile,
    onConfirmBooking: (Doctor, String, String, String, String, String) -> Unit,
    onBack: () -> Unit
) {
    val dates = listOf(
        Pair("Mon", "27 Aug"),
        Pair("Tue", "28 Aug"),
        Pair("Wed", "29 Aug"),
        Pair("Thu", "30 Aug"),
        Pair("Fri", "31 Aug"),
        Pair("Sat", "01 Sep")
    )
    var selectedDateIndex by remember { mutableStateOf(1) } // 28 Aug

    val timeSlots = listOf(
        "09:00 AM", "10:00 AM", "11:00 AM",
        "02:00 PM", "03:00 PM", "04:00 PM", "05:00 PM"
    )
    var selectedTime by remember { mutableStateOf("10:00 AM") }

    var patientName by remember { mutableStateOf(currentUser.fullName) }
    var patientPhone by remember { mutableStateOf(currentUser.phone) }
    var visitReason by remember { mutableStateOf("Routine Consultation") }
    var isBookingSuccess by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LifeCareBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Back Button & Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = LifeCareTextPrimary)
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Book Appointment",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = LifeCareTextPrimary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Doctor Card Summary
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = LifeCareSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = LifeCareTealLight,
                    modifier = Modifier.size(60.dp)
                ) {
                    Image(
                        painter = painterResource(
                            id = if (doctor.imageRes == "doctor_alex") R.drawable.doctor_alex else R.drawable.doctor_sarah
                        ),
                        contentDescription = doctor.name,
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(doctor.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(doctor.specialization, color = LifeCareTealDark, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("${doctor.rating} Rating", fontSize = 12.sp, color = LifeCareTextSecondary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(doctor.consultationFee, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = LifeCareTeal)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Select Date Section
        Text("Select Date", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = LifeCareTextPrimary)
        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(dates.size) { index ->
                val (dayName, dayDate) = dates[index]
                val isSelected = selectedDateIndex == index
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) LifeCareTeal else LifeCareSurface,
                    border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, LifeCareBorder),
                    modifier = Modifier
                        .clickable { selectedDateIndex = index }
                        .size(width = 72.dp, height = 76.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = dayName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isSelected) Color.White.copy(alpha = 0.8f) else LifeCareTextSecondary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = dayDate,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else LifeCareTextPrimary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Select Time Section
        Text("Select Time", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = LifeCareTextPrimary)
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            timeSlots.forEach { slot ->
                val isSelected = selectedTime == slot
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) LifeCareTealLight else LifeCareSurface,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) LifeCareTeal else LifeCareBorder
                    ),
                    modifier = Modifier.clickable { selectedTime = slot }
                ) {
                    Text(
                        text = slot,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) LifeCareTealDark else LifeCareTextPrimary,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Patient Details Form
        Text("Patient Information", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = LifeCareTextPrimary)
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = LifeCareSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = patientName,
                    onValueChange = { patientName = it },
                    label = { Text("Patient Full Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = LifeCareTeal) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("patient_name_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = patientPhone,
                    onValueChange = { patientPhone = it },
                    label = { Text("Contact Phone") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = LifeCareTeal) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("patient_phone_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = visitReason,
                    onValueChange = { visitReason = it },
                    label = { Text("Reason for Visit") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("visit_reason_input")
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Appointment Summary Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = LifeCarePeachLight),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Appointment Summary", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFFC0553A))
                Spacer(modifier = Modifier.height(8.dp))
                SummaryRow("Doctor", doctor.name)
                SummaryRow("Specialization", doctor.specialization)
                SummaryRow("Date & Time", "${dates[selectedDateIndex].second} • $selectedTime")
                SummaryRow("Consultation Fee", doctor.consultationFee, isHighlighted = true)
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Confirm Button
        Button(
            onClick = {
                val fullDate = "${dates[selectedDateIndex].second} 2026"
                onConfirmBooking(doctor, patientName, patientPhone, visitReason, fullDate, selectedTime)
                isBookingSuccess = true
            },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("confirm_booking_button")
        ) {
            Text("Confirm Booking", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Booking Success Dialog
    if (isBookingSuccess) {
        AlertDialog(
            onDismissRequest = {
                isBookingSuccess = false
                onBack()
            },
            icon = {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = LifeCareTeal,
                    modifier = Modifier.size(52.dp)
                )
            },
            title = {
                Text("Appointment Booked Successfully", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "Your appointment with ${doctor.name} on ${dates[selectedDateIndex].second} at $selectedTime has been confirmed and saved to Firestore database.",
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        isBookingSuccess = false
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("View Appointments")
                }
            }
        )
    }
}

@Composable
private fun SummaryRow(label: String, value: String, isHighlighted: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 13.sp, color = LifeCareTextSecondary)
        Text(
            value,
            fontSize = 13.sp,
            fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Medium,
            color = if (isHighlighted) Color(0xFFC0553A) else LifeCareTextPrimary
        )
    }
}

/**
 * Student 2: My Appointments Screen (Full CRUD: Read, Update, Delete/Cancel)
 */
@Composable
fun MyAppointmentsScreen(
    appointments: List<Appointment>,
    onUpdateAppointment: (String, String, String, String) -> Unit,
    onCancelAppointment: (String) -> Unit,
    onDeleteAppointment: (String) -> Unit,
    onBookNewAppointment: () -> Unit
) {
    var editingAppointment by remember { mutableStateOf<Appointment?>(null) }
    var deletingAppointmentId by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LifeCareBackground)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "My Appointments",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = LifeCareTextPrimary
                )
                Text(
                    text = "Manage your doctor consultations",
                    fontSize = 13.sp,
                    color = LifeCareTextSecondary
                )
            }

            Button(
                onClick = onBookNewAppointment,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal),
                modifier = Modifier.testTag("book_new_appointment_button")
            ) {
                Text("+ Book", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (appointments.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = LifeCareTextMuted,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("No appointments scheduled", fontWeight = FontWeight.Bold, color = LifeCareTextSecondary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onBookNewAppointment,
                        colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal)
                    ) {
                        Text("Find a Doctor")
                    }
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(appointments, key = { it.id }) { apt ->
                    AppointmentCardItem(
                        appointment = apt,
                        onEdit = { editingAppointment = apt },
                        onCancel = { onCancelAppointment(apt.id) },
                        onDelete = { deletingAppointmentId = apt.id }
                    )
                }
            }
        }
    }

    // Edit Appointment Dialog (CRUD: Update)
    if (editingAppointment != null) {
        val currentApt = editingAppointment!!
        var editDate by remember { mutableStateOf(currentApt.date) }
        var editTime by remember { mutableStateOf(currentApt.time) }
        var editReason by remember { mutableStateOf(currentApt.reason) }

        AlertDialog(
            onDismissRequest = { editingAppointment = null },
            title = { Text("Reschedule Appointment", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Doctor: ${currentApt.doctorName}", fontWeight = FontWeight.SemiBold, color = LifeCareTealDark)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = editDate,
                        onValueChange = { editDate = it },
                        label = { Text("Appointment Date") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editTime,
                        onValueChange = { editTime = it },
                        label = { Text("Appointment Time") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editReason,
                        onValueChange = { editReason = it },
                        label = { Text("Reason for Visit") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateAppointment(currentApt.id, editDate, editTime, editReason)
                        editingAppointment = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal)
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingAppointment = null }) {
                    Text("Cancel", color = LifeCareTextSecondary)
                }
            }
        )
    }

    // Delete Confirmation Dialog
    if (deletingAppointmentId != null) {
        AlertDialog(
            onDismissRequest = { deletingAppointmentId = null },
            title = { Text("Delete Appointment Record?", fontWeight = FontWeight.Bold) },
            text = { Text("This will permanently remove this appointment from your history.") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteAppointment(deletingAppointmentId!!)
                        deletingAppointmentId = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareEmergency)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingAppointmentId = null }) {
                    Text("Keep", color = LifeCareTextSecondary)
                }
            }
        )
    }
}

@Composable
fun AppointmentCardItem(
    appointment: Appointment,
    onEdit: () -> Unit,
    onCancel: () -> Unit,
    onDelete: () -> Unit
) {
    val statusColor = when (appointment.status) {
        "Confirmed" -> Color(0xFF2E7D32)
        "Pending" -> Color(0xFFF57C00)
        "Completed" -> LifeCareTealDark
        else -> LifeCareEmergency
    }

    val statusBg = when (appointment.status) {
        "Confirmed" -> Color(0xFFE8F5E9)
        "Pending" -> Color(0xFFFFF3E0)
        "Completed" -> LifeCareTealLight
        else -> Color(0xFFFFECEC)
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = LifeCareSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = LifeCareTealLight,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.doctor_sarah),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(appointment.doctorName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(appointment.specialization, color = LifeCareTealDark, fontSize = 13.sp)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = statusBg
                ) {
                    Text(
                        text = appointment.status,
                        color = statusColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Date & Time Box
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = LifeCareBackground,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = LifeCareTeal, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(appointment.date, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = LifeCareTeal, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(appointment.time, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }

            if (appointment.reason.isNotBlank()) {
                Text(
                    text = "Reason: ${appointment.reason}",
                    fontSize = 12.sp,
                    color = LifeCareTextSecondary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = LifeCareBorder)
            Spacer(modifier = Modifier.height(8.dp))

            // Actions: Edit, Cancel, Delete
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (appointment.status != "Cancelled") {
                    TextButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp), tint = LifeCareTealDark)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Reschedule", color = LifeCareTealDark, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(onClick = onCancel) {
                        Text("Cancel", color = LifeCareEmergency, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                } else {
                    TextButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp), tint = LifeCareTextSecondary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Remove", color = LifeCareTextSecondary, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
