package com.example.ui.medication

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.models.HealthStatus
import com.example.models.MedicalRecord
import com.example.models.MedicationReminder
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
 * Student 4: Medication Reminders Screen
 * Full CRUD for Reminders
 */
@Composable
fun MedicationReminderScreen(
    reminders: List<MedicationReminder>,
    onAddReminder: (String, String, String, String, String, String) -> Unit,
    onUpdateReminder: (String, String, String, String, String) -> Unit,
    onToggleTaken: (String) -> Unit,
    onDeleteReminder: (String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editingReminder by remember { mutableStateOf<MedicationReminder?>(null) }
    var deletingReminderId by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LifeCareBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Medication Reminders",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = LifeCareTextPrimary
            )
            Text(
                text = "Never miss a dose. Manage your prescriptions effectively.",
                fontSize = 14.sp,
                color = LifeCareTextSecondary,
                modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
            )

            if (reminders.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(LifeCareTealLight.copy(alpha = 0.3f))
                            .padding(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Medication,
                            contentDescription = null,
                            tint = LifeCareTeal,
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Your medicine list is empty",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = LifeCareTextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Add your medications to get timely reminders and track your health.",
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            color = LifeCareTextSecondary
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { showAddDialog = true },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Create First Reminder", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(reminders, key = { it.id }) { reminder ->
                        ReminderCardItem(
                            reminder = reminder,
                            onToggleTaken = { onToggleTaken(reminder.id) },
                            onEdit = { editingReminder = reminder },
                            onDelete = { deletingReminderId = reminder.id }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(72.dp)) // padding for FAB
                    }
                }
            }
        }

        // Floating Action Button to Add Reminder
        FloatingActionButton(
            onClick = { showAddDialog = true },
            containerColor = LifeCareTeal,
            contentColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("add_reminder_fab")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
                Spacer(modifier = Modifier.width(6.dp))
                Text("Add Reminder", fontWeight = FontWeight.Bold)
            }
        }
    }

    // Add Reminder Dialog (CRUD: Create)
    if (showAddDialog) {
        var medName by remember { mutableStateOf("") }
        var dosage by remember { mutableStateOf("1 Tablet") }
        var time by remember { mutableStateOf("08:00 AM") }
        var frequency by remember { mutableStateOf("Daily") }
        var startDate by remember { mutableStateOf("Today") }
        var endDate by remember { mutableStateOf("Ongoing") }
        var showError by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.NotificationImportant, contentDescription = null, tint = LifeCareTeal)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Medication", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text("Enter the details of your new medicine reminder.", fontSize = 12.sp, color = LifeCareTextSecondary, modifier = Modifier.padding(bottom = 12.dp))
                    
                    OutlinedTextField(
                        value = medName,
                        onValueChange = { medName = it; showError = false },
                        label = { Text("Medicine Name") },
                        placeholder = { Text("e.g. Vitamin C, Panadol") },
                        singleLine = true,
                        isError = showError && medName.isBlank(),
                        modifier = Modifier.fillMaxWidth().testTag("add_reminder_name_input"),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = dosage,
                        onValueChange = { dosage = it; showError = false },
                        label = { Text("Dosage") },
                        placeholder = { Text("e.g. 1 Tablet, 5ml") },
                        singleLine = true,
                        isError = showError && dosage.isBlank(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = time,
                        onValueChange = { time = it; showError = false },
                        label = { Text("Scheduled Time") },
                        placeholder = { Text("e.g. 08:00 AM") },
                        singleLine = true,
                        isError = showError && time.isBlank(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null, tint = LifeCareTeal) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = frequency,
                        onValueChange = { frequency = it; showError = false },
                        label = { Text("Frequency") },
                        placeholder = { Text("e.g. Daily, Twice a Day") },
                        singleLine = true,
                        isError = showError && frequency.isBlank(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = startDate,
                        onValueChange = { startDate = it },
                        label = { Text("Start Date") },
                        placeholder = { Text("e.g. Today, Aug 25") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    if (showError) {
                        Text(
                            text = "* All fields except Start Date are required",
                            color = LifeCareEmergency,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (medName.isNotBlank() && dosage.isNotBlank() && time.isNotBlank() && frequency.isNotBlank()) {
                            onAddReminder(medName, dosage, time, frequency, startDate, endDate)
                            showAddDialog = false
                        } else {
                            showError = true
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal)
                ) {
                    Text("Save Reminder", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel", color = LifeCareTextSecondary)
                }
            }
        )
    }

    // Edit Reminder Dialog (CRUD: Update)
    if (editingReminder != null) {
        val rem = editingReminder!!
        var medName by remember { mutableStateOf(rem.medicineName) }
        var dosage by remember { mutableStateOf(rem.dosage) }
        var time by remember { mutableStateOf(rem.time) }
        var frequency by remember { mutableStateOf(rem.frequency) }
        var showError by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { editingReminder = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = LifeCareTeal)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit Reminder", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = medName,
                        onValueChange = { medName = it; showError = false },
                        label = { Text("Medicine Name") },
                        singleLine = true,
                        isError = showError && medName.isBlank(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = dosage,
                        onValueChange = { dosage = it; showError = false },
                        label = { Text("Dosage") },
                        singleLine = true,
                        isError = showError && dosage.isBlank(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = time,
                        onValueChange = { time = it; showError = false },
                        label = { Text("Time") },
                        singleLine = true,
                        isError = showError && time.isBlank(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null, tint = LifeCareTeal) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = frequency,
                        onValueChange = { frequency = it; showError = false },
                        label = { Text("Frequency") },
                        singleLine = true,
                        isError = showError && frequency.isBlank(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    if (showError) {
                        Text(
                            text = "* All fields are required",
                            color = LifeCareEmergency,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (medName.isNotBlank() && dosage.isNotBlank() && time.isNotBlank() && frequency.isNotBlank()) {
                            onUpdateReminder(rem.id, medName, dosage, time, frequency)
                            editingReminder = null
                        } else {
                            showError = true
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal)
                ) {
                    Text("Update", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { editingReminder = null }) {
                    Text("Cancel", color = LifeCareTextSecondary)
                }
            }
        )
    }

    // Delete Confirmation Dialog (CRUD: Delete)
    if (deletingReminderId != null) {
        AlertDialog(
            onDismissRequest = { deletingReminderId = null },
            title = { Text("Delete Reminder?", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to delete this medication reminder?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteReminder(deletingReminderId!!)
                        deletingReminderId = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareEmergency)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingReminderId = null }) {
                    Text("Cancel", color = LifeCareTextSecondary)
                }
            }
        )
    }
}

@Composable
fun ReminderCardItem(
    reminder: MedicationReminder,
    onToggleTaken: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (reminder.isTaken) Color(0xFFF1F8F7) else LifeCareSurface
        ),
        border = if (reminder.isTaken) androidx.compose.foundation.BorderStroke(1.dp, LifeCareTealLight) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = if (reminder.isTaken) 0.dp else 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox Icon for Taken status
            Surface(
                onClick = onToggleTaken,
                shape = CircleShape,
                color = if (reminder.isTaken) LifeCareTeal else Color.Transparent,
                border = if (reminder.isTaken) null else androidx.compose.foundation.BorderStroke(2.dp, LifeCareTealLight),
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (reminder.isTaken) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Mark Untaken",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reminder.medicineName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (reminder.isTaken) LifeCareTextSecondary else LifeCareTextPrimary
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Vaccines,
                        contentDescription = null,
                        tint = LifeCareTeal,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${reminder.dosage} • ${reminder.frequency}",
                        fontSize = 13.sp,
                        color = LifeCareTextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Spacer(modifier = Modifier.height(6.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = LifeCareTealLight,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = LifeCareTealDark, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = reminder.time,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = LifeCareTealDark
                            )
                        }
                    }
                    
                    if (reminder.isTaken) {
                        Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFE8F5E9)) {
                            Text(
                                "COMPLETED",
                                fontSize = 10.sp,
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Actions
            Column(horizontalAlignment = Alignment.End) {
                IconButton(onClick = onEdit, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = LifeCareTeal, modifier = Modifier.size(20.dp))
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = LifeCareEmergency, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

/**
 * Student 4: Health Status Screen
 * Displays Heart Rate, Steps, Calories, Sleep, and Water Intake.
 */
@Composable
fun HealthStatusScreen(
    healthStatus: HealthStatus,
    onLogWater: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LifeCareBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Health Dashboard",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = LifeCareTextPrimary
        )
        Text(
            text = "Your vitals and activity summary for today",
            fontSize = 14.sp,
            color = LifeCareTextSecondary,
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
        )

        // Main Vital: Heart Rate (Prominent)
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = LifeCareSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFFFEBEE),
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = LifeCareEmergency,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Heart Rate", fontSize = 14.sp, color = LifeCareTextSecondary, fontWeight = FontWeight.Medium)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${healthStatus.heartRate}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = LifeCareTextPrimary
                        )
                        Text(
                            text = " BPM",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = LifeCareTextSecondary,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFE8F5E9)
                ) {
                    Text(
                        text = "NORMAL",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF2E7D32),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Activity Grid: Steps & Calories
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HealthMetricCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.DirectionsRun,
                label = "Daily Steps",
                value = "${healthStatus.steps}",
                unit = "steps",
                tint = LifeCareTeal,
                bg = LifeCareTealLight
            )
            HealthMetricCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.LocalFireDepartment,
                label = "Calories",
                value = "${healthStatus.calories}",
                unit = "kcal",
                tint = Color(0xFFC0553A),
                bg = LifeCarePeachLight
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sleep Card
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = LifeCareSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFE8EAF6),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Bedtime, contentDescription = null, tint = Color(0xFF3F51B5), modifier = Modifier.size(24.dp))
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Sleep Duration", fontSize = 13.sp, color = LifeCareTextSecondary)
                    Text(
                        text = "${healthStatus.sleepHours}h ${healthStatus.sleepMinutes}m",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = LifeCareTextPrimary
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text("RESTED", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF3F51B5))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Water Intake (Interactive)
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = LifeCareSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Water Intake", fontSize = 14.sp, color = LifeCareTextSecondary, fontWeight = FontWeight.Medium)
                        Text(
                            text = "${healthStatus.waterGlasses} / ${healthStatus.maxWaterGlasses} Glasses",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = LifeCareTealDark
                        )
                    }
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { if (healthStatus.waterGlasses > 0) onLogWater(-1) },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Surface(shape = CircleShape, color = LifeCareBackground, border = androidx.compose.foundation.BorderStroke(1.dp, LifeCareBorder)) {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                    Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        IconButton(
                            onClick = { if (healthStatus.waterGlasses < 15) onLogWater(1) },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Surface(shape = CircleShape, color = LifeCareTeal) {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Modern progress indicator
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(LifeCareBorder)
                ) {
                    val progress = (healthStatus.waterGlasses.toFloat() / healthStatus.maxWaterGlasses.toFloat()).coerceIn(0f, 1f)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .fillMaxHeight()
                            .background(Brush.horizontalGradient(listOf(LifeCareTeal, LifeCareTealDark)))
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (healthStatus.waterGlasses >= healthStatus.maxWaterGlasses) "Daily Goal Reached! 💧" else "Stay hydrated throughout the day",
                    fontSize = 12.sp,
                    color = if (healthStatus.waterGlasses >= healthStatus.maxWaterGlasses) LifeCareTealDark else LifeCareTextSecondary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun HealthMetricCard(
    modifier: Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    unit: String,
    tint: Color,
    bg: Color
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = LifeCareSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Surface(shape = RoundedCornerShape(12.dp), color = bg, modifier = Modifier.size(40.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(22.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(label, fontSize = 12.sp, color = LifeCareTextSecondary, fontWeight = FontWeight.Medium)
            Row(verticalAlignment = Alignment.Bottom) {
                Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = LifeCareTextPrimary)
                Text(unit, fontSize = 12.sp, color = LifeCareTextSecondary, modifier = Modifier.padding(start = 4.dp, bottom = 2.dp))
            }
        }
    }
}


/**
 * Student 4: Medical Records Screen
 * Full CRUD for Medical Records
 */
@Composable
fun MedicalRecordsScreen(
    records: List<MedicalRecord>,
    userProfile: UserProfile,
    onAddRecord: (String, String, String, String, String) -> Unit,
    onUpdateRecord: (String, String, String, String, String) -> Unit,
    onDeleteRecord: (String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editingRecord by remember { mutableStateOf<MedicalRecord?>(null) }
    var deletingRecordId by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LifeCareBackground)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Medical Records",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = LifeCareTextPrimary
                )
                Text(
                    text = "Prescriptions, reports, and lab documents",
                    fontSize = 13.sp,
                    color = LifeCareTextSecondary
                )
            }

            Button(
                onClick = { showAddDialog = true },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal),
                modifier = Modifier.testTag("add_record_button")
            ) {
                Text("+ New", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // User Health Card Summary at Top
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
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = LifeCareTealDark, modifier = Modifier.size(28.dp))
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(userProfile.fullName, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = LifeCareTealDark)
                    Text("Blood: ${userProfile.bloodGroup} • Age: ${userProfile.age} Yrs", fontSize = 13.sp, color = LifeCareTextPrimary)
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White
                ) {
                    Text(
                        "${records.size} Records",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = LifeCareTealDark,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (records.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Description,
                        contentDescription = null,
                        tint = LifeCareTextMuted,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("No medical records found", fontWeight = FontWeight.Bold, color = LifeCareTextSecondary)
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(records, key = { it.id }) { record ->
                    MedicalRecordCardItem(
                        record = record,
                        onEdit = { editingRecord = record },
                        onDelete = { deletingRecordId = record.id }
                    )
                }
            }
        }
    }

    // Add Record Dialog (CRUD: Create)
    if (showAddDialog) {
        var title by remember { mutableStateOf("") }
        var recordType by remember { mutableStateOf("Prescription") }
        var doctorClinic by remember { mutableStateOf("") }
        var date by remember { mutableStateOf("Aug 17, 2026") }
        var description by remember { mutableStateOf("") }
        var showError by remember { mutableStateOf(false) }

        val typeOptions = listOf("Prescription", "Lab Report", "X-Ray Report", "Vaccination", "Health Summary")

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Description, contentDescription = null, tint = LifeCareTeal)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("New Medical Record", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it; showError = false },
                        label = { Text("Record Title") },
                        placeholder = { Text("e.g. Blood Test Results") },
                        singleLine = true,
                        isError = showError && title.isBlank(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Record Type:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = LifeCareTextPrimary)
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        typeOptions.chunked(3).forEach { rowItems ->
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                rowItems.forEach { t ->
                                    val isSelected = recordType == t
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isSelected) LifeCareTeal else LifeCareBackground,
                                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, LifeCareBorder),
                                        modifier = Modifier.weight(1f).clickable { recordType = t }
                                    ) {
                                        Text(
                                            t,
                                            fontSize = 11.sp,
                                            color = if (isSelected) Color.White else LifeCareTextSecondary,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(vertical = 10.dp)
                                        )
                                    }
                                }
                                if (rowItems.size < 3) Spacer(modifier = Modifier.weight((3 - rowItems.size).toFloat()))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = date,
                        onValueChange = { date = it; showError = false },
                        label = { Text("Date") },
                        singleLine = true,
                        isError = showError && date.isBlank(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null, tint = LifeCareTeal) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = doctorClinic,
                        onValueChange = { doctorClinic = it },
                        label = { Text("Hospital / Clinic") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it; showError = false },
                        label = { Text("Description / Notes") },
                        maxLines = 3,
                        isError = showError && description.isBlank(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    
                    if (showError) {
                        Text(
                            "* Title, Date and Description are required",
                            color = LifeCareEmergency,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isNotBlank() && date.isNotBlank() && description.isNotBlank()) {
                            onAddRecord(title, recordType, doctorClinic, date, description)
                            showAddDialog = false
                        } else {
                            showError = true
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal)
                ) {
                    Text("Save Record", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel", color = LifeCareTextSecondary)
                }
            }
        )
    }

    // Edit Record Dialog (CRUD: Update)
    if (editingRecord != null) {
        val rec = editingRecord!!
        var title by remember { mutableStateOf(rec.title) }
        var recordType by remember { mutableStateOf(rec.recordType) }
        var doctorClinic by remember { mutableStateOf(rec.doctorOrClinic) }
        var description by remember { mutableStateOf(rec.description) }
        var showError by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { editingRecord = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = LifeCareTeal)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Update Record", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it; showError = false },
                        label = { Text("Title") },
                        singleLine = true,
                        isError = showError && title.isBlank(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = doctorClinic,
                        onValueChange = { doctorClinic = it },
                        label = { Text("Hospital / Clinic") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it; showError = false },
                        label = { Text("Notes") },
                        maxLines = 3,
                        isError = showError && description.isBlank(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    
                    if (showError) {
                        Text(
                            "* Title and Description are required",
                            color = LifeCareEmergency,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isNotBlank() && description.isNotBlank()) {
                            onUpdateRecord(rec.id, title, recordType, doctorClinic, description)
                            editingRecord = null
                        } else {
                            showError = true
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal)
                ) {
                    Text("Update", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { editingRecord = null }) {
                    Text("Cancel", color = LifeCareTextSecondary)
                }
            }
        )
    }

    // Delete Confirmation Dialog (CRUD: Delete)
    if (deletingRecordId != null) {
        AlertDialog(
            onDismissRequest = { deletingRecordId = null },
            title = { Text("Delete Medical Record?", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to permanently delete this medical record?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteRecord(deletingRecordId!!)
                        deletingRecordId = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareEmergency)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingRecordId = null }) {
                    Text("Cancel", color = LifeCareTextSecondary)
                }
            }
        )
    }
}

@Composable
fun MedicalRecordCardItem(
    record: MedicalRecord,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val (typeColor, typeBg, typeIcon) = when (record.recordType) {
        "Prescription" -> Triple(LifeCareTealDark, LifeCareTealLight, Icons.Default.Description)
        "Lab Report" -> Triple(Color(0xFFC0553A), LifeCarePeachLight, Icons.Default.CheckCircleOutline)
        "X-Ray Report" -> Triple(Color(0xFF5C6BC0), Color(0xFFE8EAF6), Icons.Default.Person)
        "Vaccination" -> Triple(Color(0xFF2E7D32), Color(0xFFE8F5E9), Icons.Default.Vaccines)
        else -> Triple(Color(0xFF607D8B), Color(0xFFECEFF1), Icons.Default.Description)
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LifeCareSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = typeBg
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(typeIcon, contentDescription = null, tint = typeColor, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = record.recordType,
                            color = typeColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Text(record.date, fontSize = 12.sp, color = LifeCareTextSecondary, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = record.title,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 17.sp,
                color = LifeCareTextPrimary
            )
            
            if (record.doctorOrClinic.isNotBlank()) {
                Text(
                    text = "at ${record.doctorOrClinic}",
                    fontSize = 13.sp,
                    color = LifeCareTealDark,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (record.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = record.description,
                    fontSize = 14.sp,
                    color = LifeCareTextSecondary,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = LifeCareBorder, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = LifeCareTeal, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Edit", color = LifeCareTeal, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = LifeCareEmergency, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Delete", color = LifeCareEmergency, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
