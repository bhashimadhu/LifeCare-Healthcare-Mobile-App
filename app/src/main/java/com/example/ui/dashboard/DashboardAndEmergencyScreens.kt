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
import androidx.compose.material.icons.filled.Edit
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
import com.example.models.MedicationReminder
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
    reminders: List<MedicationReminder>,
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
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LifeCareTextPrimary
                )
                Text(
                    text = "Track your health and routine easily",
                    fontSize = 12.sp,
                    color = LifeCareTeal,
                    fontWeight = FontWeight.SemiBold
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
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = LifeCareTealDark,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onNavigateToProfile,
                    modifier = Modifier.testTag("dashboard_profile_button")
                ) {
                    Surface(
                        shape = CircleShape,
                        color = LifeCareTealLight,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = LifeCareTealDark,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Health Overview Header
        Text(
            text = "Health Overview",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = LifeCareTextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 3 Simple Health Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Heart Rate
            HealthOverviewCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Favorite,
                iconTint = LifeCareEmergency,
                iconBg = Color(0xFFFFEBEE),
                title = "Heart Rate",
                value = "${healthStatus.heartRate}",
                unit = "BPM"
            )

            // Blood Pressure
            HealthOverviewCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Bloodtype,
                iconTint = Color(0xFFC0553A),
                iconBg = LifeCarePeachLight,
                title = "Blood Pressure",
                value = healthStatus.bloodPressure,
                unit = "mmHg"
            )

            // Water Intake
            HealthOverviewCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Opacity,
                iconTint = LifeCareTealDark,
                iconBg = LifeCareTealLight,
                title = "Water Log",
                value = "${healthStatus.waterGlasses}/${healthStatus.maxWaterGlasses}",
                unit = "Glasses"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Upcoming Appointment Card
        Text(
            text = "Next Appointment",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = LifeCareTextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (upcomingAppointment != null) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = LifeCareSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = LifeCareTealLight,
                            modifier = Modifier.size(54.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = LifeCareTealDark, modifier = Modifier.size(28.dp))
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

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
                            shape = RoundedCornerShape(10.dp),
                            color = if (upcomingAppointment.status == "Confirmed") Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                        ) {
                            Text(
                                text = upcomingAppointment.status,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (upcomingAppointment.status == "Confirmed") Color(0xFF2E7D32) else Color(0xFFE65100),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = LifeCareBorder, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MedicalServices, contentDescription = null, tint = LifeCareTeal, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${upcomingAppointment.date} | ${upcomingAppointment.time}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = LifeCareTextPrimary
                            )
                        }

                        TextButton(
                            onClick = onViewAppointmentDetails,
                            modifier = Modifier.testTag("view_appointment_details_button")
                        ) {
                            Text("Manage", color = LifeCareTeal, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = LifeCareTeal, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        } else {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = LifeCareSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, LifeCareBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("No upcoming appointments", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = LifeCareTextPrimary)
                        Text("Connect with specialized doctors anytime", fontSize = 12.sp, color = LifeCareTextSecondary)
                    }
                    Button(
                        onClick = onNavigateToDoctorList,
                        colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("Book Now", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Today's Medication Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Today's Medication",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = LifeCareTextPrimary
            )
            Text(
                text = "View All",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = LifeCareTeal,
                modifier = Modifier.clickable { onNavigateToReminders() }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (reminders.isNotEmpty()) {
            reminders.take(2).forEach { reminder ->
                MedicationDashboardItem(reminder = reminder)
                Spacer(modifier = Modifier.height(10.dp))
            }
        } else {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = LifeCareSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, LifeCareBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "No medicine reminders set for today.",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 13.sp,
                    color = LifeCareTextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Quick Actions
        Text(
            text = "Quick Actions",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = LifeCareTextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            QuickActionButton(
                icon = Icons.Default.MedicalServices,
                label = "Find Doctors",
                bg = LifeCareTealLight,
                tint = LifeCareTealDark,
                onClick = onNavigateToDoctorList,
                testTag = "qa_find_doctors"
            )

            QuickActionButton(
                icon = Icons.Default.Medication,
                label = "Medicines",
                bg = LifeCarePeachLight,
                tint = Color(0xFFC0553A),
                onClick = onNavigateToPharmacy,
                testTag = "qa_medicines"
            )

            QuickActionButton(
                icon = Icons.Default.Notifications,
                label = "Reminders",
                bg = Color(0xFFEDE7F6),
                tint = Color(0xFF5E35B1),
                onClick = onNavigateToReminders,
                testTag = "qa_reminders"
            )

            QuickActionButton(
                icon = Icons.Default.WarningAmber,
                label = "Emergency",
                bg = Color(0xFFFFEBEE),
                tint = LifeCareEmergency,
                onClick = onNavigateToEmergency,
                testTag = "qa_emergency"
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
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
private fun MedicationDashboardItem(reminder: MedicationReminder) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = LifeCareSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, LifeCareBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = if (reminder.isTaken) Color(0xFFE8F5E9) else Color(0xFFEDE7F6),
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (reminder.isTaken) Icons.Default.CheckCircle else Icons.Default.Medication,
                        contentDescription = null,
                        tint = if (reminder.isTaken) Color(0xFF2E7D32) else Color(0xFF5E35B1),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reminder.medicineName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = LifeCareTextPrimary
                )
                Text(
                    text = "${reminder.dosage} | ${reminder.time}",
                    fontSize = 12.sp,
                    color = LifeCareTextSecondary
                )
            }
            if (reminder.isTaken) {
                Text(
                    text = "Taken",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
            }
        }
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
    onUpdateEmergencyContact: (String, String, String, String) -> Unit,
    onDeleteEmergencyContact: (String) -> Unit
) {
    val context = LocalContext.current
    var showSosConfirmDialog by remember { mutableStateOf(false) }
    var showAddContactDialog by remember { mutableStateOf(false) }
    var editingContact by remember { mutableStateOf<EmergencyContact?>(null) }
    var deletingContactId by remember { mutableStateOf<String?>(null) }
    var showHospitalInfo by remember { mutableStateOf(false) }
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
                            text = "Emergency alert activated",
                            fontWeight = FontWeight.ExtraBold,
                            color = LifeCareEmergency,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Help is on the way. Your location has been shared.",
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

        // Simple Actions Section
        Text(
            text = "Emergency Actions",
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = LifeCareTextPrimary,
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            EmergencyActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.LocalHospital,
                title = "Ambulance",
                subtitle = "Call 1990",
                onClick = { callingNumber = "1990" }
            )

            EmergencyActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.MedicalServices,
                title = "Hospitals",
                subtitle = "View Info",
                onClick = { showHospitalInfo = true }
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
                    onEdit = { editingContact = contact },
                    onDelete = { deletingContactId = contact.id }
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

    // Edit Emergency Contact Dialog (CRUD: Update)
    if (editingContact != null) {
        val contact = editingContact!!
        var name by remember { mutableStateOf(contact.name) }
        var relationship by remember { mutableStateOf(contact.relationship) }
        var phone by remember { mutableStateOf(contact.phone) }
        var showError by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { editingContact = null },
            title = { Text("Edit Emergency Contact", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it; showError = false },
                        label = { Text("Full Name") },
                        singleLine = true,
                        isError = showError && name.isBlank(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = relationship,
                        onValueChange = { relationship = it; showError = false },
                        label = { Text("Relationship") },
                        singleLine = true,
                        isError = showError && relationship.isBlank(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it; showError = false },
                        label = { Text("Phone Number") },
                        singleLine = true,
                        isError = showError && phone.isBlank(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (showError) {
                        Text("All fields are required", color = LifeCareEmergency, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (name.isNotBlank() && relationship.isNotBlank() && phone.isNotBlank()) {
                            onUpdateEmergencyContact(contact.id, name, relationship, phone)
                            editingContact = null
                        } else {
                            showError = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal)
                ) {
                    Text("Update Contact")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingContact = null }) {
                    Text("Cancel", color = LifeCareTextSecondary)
                }
            }
        )
    }

    // Delete Emergency Contact Confirmation Dialog (CRUD: Delete)
    if (deletingContactId != null) {
        AlertDialog(
            onDismissRequest = { deletingContactId = null },
            title = { Text("Delete Contact?", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to remove this emergency contact?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteEmergencyContact(deletingContactId!!)
                        deletingContactId = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareEmergency)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingContactId = null }) {
                    Text("Cancel", color = LifeCareTextSecondary)
                }
            }
        )
    }

    // Hospital Information Dialog
    if (showHospitalInfo) {
        AlertDialog(
            onDismissRequest = { showHospitalInfo = false },
            title = { Text("Nearby Hospitals", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    HospitalInfoItem("National Hospital", "Colombo 07", "011 269 1111")
                    Spacer(modifier = Modifier.height(12.dp))
                    HospitalInfoItem("Asiri Surgical", "Colombo 05", "011 452 4400")
                    Spacer(modifier = Modifier.height(12.dp))
                    HospitalInfoItem("Lanka Hospitals", "Colombo 05", "011 543 0000")
                }
            },
            confirmButton = {
                Button(
                    onClick = { showHospitalInfo = false },
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal)
                ) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
private fun HospitalInfoItem(name: String, location: String, phone: String) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(location, fontSize = 12.sp, color = LifeCareTextSecondary)
        }
        IconButton(onClick = {
            val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            context.startActivity(dialIntent)
        }) {
            Icon(Icons.Default.Call, contentDescription = "Call", tint = Color(0xFF2E7D32), modifier = Modifier.size(20.dp))
        }
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
    onEdit: () -> Unit,
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

            IconButton(onClick = onCall, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.Call, contentDescription = "Call", tint = Color(0xFF2E7D32), modifier = Modifier.size(18.dp))
            }

            IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = LifeCareTeal, modifier = Modifier.size(18.dp))
            }

            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = LifeCareEmergency, modifier = Modifier.size(18.dp))
            }
        }
    }
}
