package com.example.ui.dashboard

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.models.Appointment
import com.example.models.Doctor
import com.example.models.EmergencyContact
import com.example.models.HealthStatus
import com.example.models.UserProfile
import com.example.ui.theme.LifeCareBackground
import com.example.ui.theme.LifeCareBorder
import com.example.ui.theme.LifeCareEmergency
import com.example.ui.theme.LifeCareEmergencyLight
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
 * Student 5: Home Dashboard Screen
 */
@Composable
fun HomeDashboardScreen(
    userProfile: UserProfile,
    healthStatus: HealthStatus,
    upcomingAppointment: Appointment?,
    onNavigateToDoctorList: () -> Unit,
    onNavigateToPharmacy: () -> Unit,
    onNavigateToReminders: () -> Unit,
    onNavigateToEmergency: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onViewAppointmentDetails: () -> Unit
) {
    var showNotificationDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LifeCareBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Top Header Section: Good Morning, [User Name]
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Good Morning,",
                    fontSize = 14.sp,
                    color = LifeCareTextSecondary
                )
                Text(
                    text = userProfile.fullName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = LifeCareTextPrimary
                )
                Text(
                    text = "Take care of your health today",
                    fontSize = 12.sp,
                    color = LifeCareTealDark,
                    fontWeight = FontWeight.Medium
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { showNotificationDialog = true },
                    modifier = Modifier.testTag("notification_button")
                ) {
                    Surface(
                        shape = CircleShape,
                        color = LifeCareSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, LifeCareBorder),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = LifeCareTextPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                IconButton(
                    onClick = onNavigateToProfile,
                    modifier = Modifier.testTag("dashboard_profile_button")
                ) {
                    Surface(
                        shape = CircleShape,
                        color = LifeCareTealLight,
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = LifeCareTealDark,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Health Overview Header
        Text(
            text = "Health Overview",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = LifeCareTextPrimary
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 3 Simple Health Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Heart Rate (72 BPM)
            HealthOverviewCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Favorite,
                iconTint = LifeCareEmergency,
                iconBg = Color(0xFFFFECEC),
                title = "Heart Rate",
                value = "${healthStatus.heartRate}",
                unit = "BPM"
            )

            // Blood Pressure (120/80)
            HealthOverviewCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Bloodtype,
                iconTint = Color(0xFFC0553A),
                iconBg = LifeCarePeachLight,
                title = "Blood Pressure",
                value = healthStatus.bloodPressure,
                unit = "mmHg"
            )

            // Water Intake (5 / 8 Glasses)
            HealthOverviewCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Opacity,
                iconTint = LifeCareTealDark,
                iconBg = LifeCareTealLight,
                title = "Water Intake",
                value = "${healthStatus.waterGlasses}/${healthStatus.maxWaterGlasses}",
                unit = "Glasses"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Upcoming Appointment Card
        Text(
            text = "Upcoming Appointment",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = LifeCareTextPrimary
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (upcomingAppointment != null) {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = LifeCareSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = LifeCareTealLight,
                            modifier = Modifier.size(54.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.doctor_sarah),
                                contentDescription = "Doctor",
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = upcomingAppointment.doctorName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = LifeCareTextPrimary
                            )
                            Text(
                                text = upcomingAppointment.specialization,
                                fontSize = 13.sp,
                                color = LifeCareTealDark,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFE8F5E9)
                        ) {
                            Text(
                                text = upcomingAppointment.status,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = LifeCareBorder)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${upcomingAppointment.date} • ${upcomingAppointment.time}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = LifeCareTextPrimary
                            )
                        }

                        TextButton(
                            onClick = onViewAppointmentDetails,
                            modifier = Modifier.testTag("view_appointment_details_button")
                        ) {
                            Text("View Details", color = LifeCareTealDark, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = LifeCareTealDark, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        } else {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = LifeCareSurface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("No scheduled appointments", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Text("Book with verified doctors anytime", fontSize = 12.sp, color = LifeCareTextSecondary)
                    }
                    Button(
                        onClick = onNavigateToDoctorList,
                        colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Find Doctors", fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Quick Actions
        Text(
            text = "Quick Actions",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = LifeCareTextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Find Doctors
            QuickActionButton(
                icon = Icons.Default.MedicalServices,
                label = "Find Doctors",
                bg = LifeCareTealLight,
                tint = LifeCareTealDark,
                onClick = onNavigateToDoctorList,
                testTag = "qa_find_doctors"
            )

            // Medicines
            QuickActionButton(
                icon = Icons.Default.Medication,
                label = "Medicines",
                bg = LifeCarePeachLight,
                tint = Color(0xFFC0553A),
                onClick = onNavigateToPharmacy,
                testTag = "qa_medicines"
            )

            // Reminders
            QuickActionButton(
                icon = Icons.Default.HealthAndSafety,
                label = "Reminders",
                bg = Color(0xFFEDE7F6),
                tint = Color(0xFF5E35B1),
                onClick = onNavigateToReminders,
                testTag = "qa_reminders"
            )

            // Emergency
            QuickActionButton(
                icon = Icons.Default.WarningAmber,
                label = "Emergency",
                bg = Color(0xFFFFECEC),
                tint = LifeCareEmergency,
                onClick = onNavigateToEmergency,
                testTag = "qa_emergency"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Health Tip / SDG 3 Banner Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = LifeCareTealLight),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Campaign, contentDescription = null, tint = LifeCareTealDark, modifier = Modifier.size(24.dp))
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Daily Wellness Tip",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = LifeCareTealDark
                    )
                    Text(
                        text = "Drink at least 8 glasses of water today and take a 15-minute brisk walk.",
                        fontSize = 12.sp,
                        color = LifeCareTextPrimary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showNotificationDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationDialog = false },
            title = { Text("Notifications", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("• Doctor Appointment Confirmed: Dr. Ruvan Ekanayake on 28 Aug 05:00 PM", fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Medicine Reminder: Vitamin C dose due at 08:00 AM", fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Hydration: You've reached 5/8 glasses of water today!", fontSize = 13.sp)
                }
            },
            confirmButton = {
                Button(
                    onClick = { showNotificationDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal)
                ) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
private fun HealthOverviewCard(
    modifier: Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    iconBg: Color,
    title: String,
    value: String,
    unit: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LifeCareSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Surface(
                shape = CircleShape,
                color = iconBg,
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = LifeCareTextPrimary)
            Text(title, fontSize = 11.sp, color = LifeCareTextSecondary, modifier = Modifier.padding(top = 2.dp))
        }
    }
}

@Composable
private fun QuickActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    bg: Color,
    tint: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = bg,
            modifier = Modifier.size(62.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = label, tint = tint, modifier = Modifier.size(28.dp))
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = LifeCareTextPrimary,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Student 5: Emergency SOS Screen
 * Soft red/coral theme with pulsating SOS button and full emergency support
 */
@Composable
fun EmergencyScreen(
    emergencyContacts: List<EmergencyContact>,
    isSosActive: Boolean,
    onTriggerSos: () -> Unit,
    onDismissSos: () -> Unit,
    onAddEmergencyContact: (String, String, String, Boolean) -> Unit,
    onDeleteEmergencyContact: (String) -> Unit
) {
    val context = LocalContext.current
    var showSosConfirmDialog by remember { mutableStateOf(false) }
    var showAddContactDialog by remember { mutableStateOf(false) }
    var callingNumber by remember { mutableStateOf<String?>(null) }

    // Pulsating animation for SOS button
    val infiniteTransition = rememberInfiniteTransition(label = "sos_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LifeCareBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title & Header
        Text(
            text = "Emergency SOS",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = LifeCareEmergency
        )
        Text(
            text = "Instant medical assistance and emergency alerting",
            fontSize = 13.sp,
            color = LifeCareTextSecondary,
            modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
        )

        // Active Alert Banner if SOS is triggered
        AnimatedVisibility(visible = isSosActive) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = LifeCareEmergencyLight),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = LifeCareEmergency,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Emergency Alert Activated!",
                            fontWeight = FontWeight.Bold,
                            color = LifeCareEmergency,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Location and medical info dispatched to emergency contacts.",
                            fontSize = 12.sp,
                            color = LifeCareTextPrimary
                        )
                    }
                    TextButton(onClick = onDismissSos) {
                        Text("Dismiss", color = LifeCareEmergency, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Circular SOS Button
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFFFFECEC),
                modifier = Modifier
                    .size(170.dp)
                    .scale(pulseScale)
            ) {}

            Surface(
                shape = CircleShape,
                color = LifeCareEmergency,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .size(130.dp)
                    .clickable { showSosConfirmDialog = true }
                    .testTag("sos_large_button")
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "SOS",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "PRESS FOR HELP",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        Text(
            text = "Tap the SOS button to alert campus medical staff & primary contacts",
            fontSize = 12.sp,
            color = LifeCareTextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Quick Emergency Options
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            EmergencyActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.LocalHospital,
                title = "Suwa Seriya",
                subtitle = "Ambulance 1990",
                onClick = { callingNumber = "1990" }
            )

            EmergencyActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.LocalPharmacy,
                title = "NHSL Colombo",
                subtitle = "24/7 ER Hotline",
                onClick = { callingNumber = "0112691111" }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Emergency Contacts Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Emergency Contacts",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = LifeCareTextPrimary
            )

            TextButton(
                onClick = { showAddContactDialog = true },
                modifier = Modifier.testTag("add_emergency_contact_button")
            ) {
                Text("+ Add Contact", color = LifeCareTealDark, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Emergency Contacts List
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            emergencyContacts.forEach { contact ->
                EmergencyContactRow(
                    contact = contact,
                    onCall = { callingNumber = contact.phone },
                    onDelete = { onDeleteEmergencyContact(contact.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // SOS Activation Confirmation Dialog
    if (showSosConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showSosConfirmDialog = false },
            icon = {
                Icon(Icons.Default.Warning, contentDescription = null, tint = LifeCareEmergency, modifier = Modifier.size(44.dp))
            },
            title = {
                Text("Are you sure you want to activate Emergency SOS?", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            },
            text = {
                Text(
                    "This will trigger high-priority emergency alerts, broadcast your current campus coordinates, and notify all listed emergency guardians.",
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onTriggerSos()
                        showSosConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareEmergency)
                ) {
                    Text("Activate SOS", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSosConfirmDialog = false }) {
                    Text("Cancel", color = LifeCareTextSecondary)
                }
            }
        )
    }

    // Call Phone Dialog (Mock & Real Intent)
    if (callingNumber != null) {
        AlertDialog(
            onDismissRequest = { callingNumber = null },
            title = { Text("Emergency Call", fontWeight = FontWeight.Bold) },
            text = { Text("Connecting emergency voice call to $callingNumber...") },
            confirmButton = {
                Button(
                    onClick = {
                        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$callingNumber"))
                        try {
                            context.startActivity(dialIntent)
                        } catch (e: Exception) {
                            // Handled gracefully in emulator
                        }
                        callingNumber = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareEmergency)
                ) {
                    Text("Place Call")
                }
            },
            dismissButton = {
                TextButton(onClick = { callingNumber = null }) {
                    Text("Dismiss", color = LifeCareTextSecondary)
                }
            }
        )
    }

    // Add Emergency Contact Dialog (CRUD: Create)
    if (showAddContactDialog) {
        var name by remember { mutableStateOf("") }
        var relationship by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddContactDialog = false },
            title = { Text("Add Emergency Contact", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = relationship,
                        onValueChange = { relationship = it },
                        label = { Text("Relationship (e.g. Guardian, Sister)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (name.isNotBlank() && phone.isNotBlank()) {
                            onAddEmergencyContact(name, relationship.ifBlank { "Contact" }, phone, false)
                            showAddContactDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal)
                ) {
                    Text("Save Contact")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddContactDialog = false }) {
                    Text("Cancel", color = LifeCareTextSecondary)
                }
            }
        )
    }
}

@Composable
private fun EmergencyActionCard(
    modifier: Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LifeCareSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Surface(shape = CircleShape, color = Color(0xFFFFECEC), modifier = Modifier.size(36.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = LifeCareEmergency, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = LifeCareTextPrimary)
            Text(subtitle, fontSize = 12.sp, color = LifeCareEmergency, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 2.dp))
        }
    }
}

@Composable
private fun EmergencyContactRow(
    contact: EmergencyContact,
    onCall: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = LifeCareSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = if (contact.isPrimary) Color(0xFFFFECEC) else LifeCareTealLight,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Phone,
                        contentDescription = null,
                        tint = if (contact.isPrimary) LifeCareEmergency else LifeCareTealDark,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(contact.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    if (contact.isPrimary) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFFFECEC)) {
                            Text("Primary", fontSize = 10.sp, color = LifeCareEmergency, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                        }
                    }
                }
                Text("${contact.relationship} • ${contact.phone}", fontSize = 12.sp, color = LifeCareTextSecondary)
            }

            IconButton(onClick = onCall) {
                Icon(Icons.Default.Call, contentDescription = "Call", tint = Color(0xFF2E7D32))
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = LifeCareEmergency, modifier = Modifier.size(18.dp))
            }
        }
    }
}
