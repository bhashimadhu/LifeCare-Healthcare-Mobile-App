package com.example.ui.medication

import androidx.compose.foundation.Canvas
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
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = LifeCareTextPrimary
            )
            Text(
                text = "Track your daily medicine routine and doses",
                fontSize = 13.sp,
                color = LifeCareTextSecondary,
                modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
            )

            if (reminders.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Medication,
                            contentDescription = null,
                            tint = LifeCareTextMuted,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No reminders added yet", fontWeight = FontWeight.Bold, color = LifeCareTextSecondary)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { showAddDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal)
                        ) {
                            Text("+ Add Reminder")
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

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Medication Reminder", fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    OutlinedTextField(
                        value = medName,
                        onValueChange = { medName = it },
                        label = { Text("Medicine Name (e.g. Vitamin C)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("add_reminder_name_input")
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = dosage,
                        onValueChange = { dosage = it },
                        label = { Text("Dosage (e.g. 1 Tablet / 5ml)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = time,
                        onValueChange = { time = it },
                        label = { Text("Time (e.g. 08:00 AM)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = frequency,
                        onValueChange = { frequency = it },
                        label = { Text("Frequency (Daily / Twice a Day / Weekly)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = startDate,
                        onValueChange = { startDate = it },
                        label = { Text("Start Date") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (medName.isNotBlank()) {
                            onAddReminder(medName, dosage, time, frequency, startDate, endDate)
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal)
                ) {
                    Text("Save Reminder")
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

        AlertDialog(
            onDismissRequest = { editingReminder = null },
            title = { Text("Edit Reminder", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = medName,
                        onValueChange = { medName = it },
                        label = { Text("Medicine Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = dosage,
                        onValueChange = { dosage = it },
                        label = { Text("Dosage") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = time,
                        onValueChange = { time = it },
                        label = { Text("Time") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = frequency,
                        onValueChange = { frequency = it },
                        label = { Text("Frequency") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateReminder(rem.id, medName, dosage, time, frequency)
                        editingReminder = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal)
                ) {
                    Text("Save")
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (reminder.isTaken) Color(0xFFF4FAF9) else LifeCareSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox Icon for Taken status
            IconButton(
                onClick = onToggleTaken,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = if (reminder.isTaken) Icons.Default.CheckCircle else Icons.Default.CheckCircleOutline,
                    contentDescription = "Mark Taken",
                    tint = if (reminder.isTaken) LifeCareTeal else LifeCareTextMuted,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reminder.medicineName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (reminder.isTaken) LifeCareTextSecondary else LifeCareTextPrimary
                )
                Text(
                    text = "${reminder.dosage} • ${reminder.frequency}",
                    fontSize = 13.sp,
                    color = LifeCareTextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = LifeCareTeal, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = reminder.time,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LifeCareTealDark
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    if (reminder.isTaken) {
                        Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFE8F5E9)) {
                            Text("Taken", fontSize = 11.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                }
            }

            // Edit & Delete Actions
            Row {
                IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = LifeCareTextSecondary, modifier = Modifier.size(18.dp))
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = LifeCareEmergency, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

/**
 * Student 4: Health Status Screen
 * Tabs: Overview, Heart, Activity, Sleep
 */
@Composable
fun HealthStatusScreen(
    healthStatus: HealthStatus,
    onLogWater: (Int) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Overview", "Heart", "Activity", "Sleep")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LifeCareBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Health Status",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = LifeCareTextPrimary
        )
        Text(
            text = "Real-time summary and daily vital monitoring",
            fontSize = 13.sp,
            color = LifeCareTextSecondary,
            modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
        )

        // Tab Row
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = LifeCareSurface,
            contentColor = LifeCareTeal,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = LifeCareTeal
                )
            },
            modifier = Modifier.clip(RoundedCornerShape(12.dp))
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == index) LifeCareTealDark else LifeCareTextSecondary,
                            fontSize = 13.sp
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Main Metric Cards Grid (Heart Rate & Blood Pressure)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Heart Rate Card
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = LifeCareSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Surface(shape = CircleShape, color = Color(0xFFFFECEC), modifier = Modifier.size(38.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Favorite, contentDescription = null, tint = LifeCareEmergency, modifier = Modifier.size(20.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Heart Rate", fontSize = 12.sp, color = LifeCareTextSecondary)
                    Text("${healthStatus.heartRate} BPM", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = LifeCareTextPrimary)
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFE8F5E9),
                        modifier = Modifier.padding(top = 6.dp)
                    ) {
                        Text(
                            text = "Normal",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Blood Pressure Card
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = LifeCareSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Surface(shape = CircleShape, color = LifeCarePeachLight, modifier = Modifier.size(38.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Bloodtype, contentDescription = null, tint = Color(0xFFC0553A), modifier = Modifier.size(20.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Blood Pressure", fontSize = 12.sp, color = LifeCareTextSecondary)
                    Text(healthStatus.bloodPressure, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = LifeCareTextPrimary)
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = LifeCareTealLight,
                        modifier = Modifier.padding(top = 6.dp)
                    ) {
                        Text(
                            text = "Optimal",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = LifeCareTealDark,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Water Intake Card with +/- Controls
        Card(
            shape = RoundedCornerShape(18.dp),
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = CircleShape, color = LifeCareTealLight, modifier = Modifier.size(38.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Opacity, contentDescription = null, tint = LifeCareTealDark, modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Water Intake", fontSize = 13.sp, color = LifeCareTextSecondary)
                            Text("${healthStatus.waterGlasses} / ${healthStatus.maxWaterGlasses} Glasses", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { onLogWater(-1) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Surface(shape = CircleShape, color = LifeCareBackground, modifier = Modifier.fillMaxSize()) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = { onLogWater(1) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Surface(shape = CircleShape, color = LifeCareTealLight, modifier = Modifier.fillMaxSize()) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Add, contentDescription = "Add", tint = LifeCareTealDark, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Water Glass progress indicators
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(8) { index ->
                        val isFilled = index < healthStatus.waterGlasses
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 2.dp)
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(if (isFilled) LifeCareTeal else LifeCareBorder)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Steps, Calories, Sleep Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            HealthSmallCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.DirectionsRun,
                iconTint = LifeCareTealDark,
                iconBg = LifeCareTealLight,
                label = "Steps",
                value = "${healthStatus.steps}"
            )

            HealthSmallCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.LocalFireDepartment,
                iconTint = Color(0xFFC0553A),
                iconBg = LifeCarePeachLight,
                label = "Calories",
                value = "${healthStatus.calories} kcal"
            )

            HealthSmallCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Bedtime,
                iconTint = Color(0xFF5C6BC0),
                iconBg = Color(0xFFE8EAF6),
                label = "Sleep",
                value = "${healthStatus.sleepHours}h ${healthStatus.sleepMinutes}m"
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Weekly Activity Chart Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = LifeCareSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Weekly Activity Summary",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = LifeCareTextPrimary
                )
                Text(
                    text = "Daily step trends across the current week",
                    fontSize = 12.sp,
                    color = LifeCareTextSecondary,
                    modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
                )

                // Simple Activity Bar Chart
                SimpleWeeklyActivityChart()
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun HealthSmallCard(
    modifier: Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    iconBg: Color,
    label: String,
    value: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LifeCareSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(shape = CircleShape, color = iconBg, modifier = Modifier.size(34.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = LifeCareTextPrimary, textAlign = TextAlign.Center)
            Text(label, fontSize = 11.sp, color = LifeCareTextSecondary)
        }
    }
}

@Composable
private fun SimpleWeeklyActivityChart() {
    val weekDays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val activityLevels = listOf(0.65f, 0.85f, 0.45f, 0.90f, 0.75f, 0.95f, 0.60f)

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val barWidth = 24.dp.toPx()
                val totalBars = weekDays.size
                val spaceBetween = (size.width - (barWidth * totalBars)) / (totalBars + 1)

                activityLevels.forEachIndexed { index, fraction ->
                    val x = spaceBetween + index * (barWidth + spaceBetween)
                    val barHeight = size.height * fraction
                    val y = size.height - barHeight

                    // Background track
                    drawRoundRect(
                        color = Color(0xFFF0F5F4),
                        topLeft = Offset(x, 0f),
                        size = Size(barWidth, size.height),
                        cornerRadius = CornerRadius(12f, 12f)
                    )

                    // Active bar with teal gradient
                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(LifeCareTeal, LifeCareTealDark)
                        ),
                        topLeft = Offset(x, y),
                        size = Size(barWidth, barHeight),
                        cornerRadius = CornerRadius(12f, 12f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Day Labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            weekDays.forEach { day ->
                Text(
                    text = day,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = LifeCareTextSecondary,
                    modifier = Modifier.width(32.dp),
                    textAlign = TextAlign.Center
                )
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
        var doctorClinic by remember { mutableStateOf("City Medical Hospital") }
        var date by remember { mutableStateOf("Today") }
        var description by remember { mutableStateOf("") }

        val typeOptions = listOf("Prescription", "Lab Reports", "X-Ray Reports", "Vaccination", "Health Summary")

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Medical Record", fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Record Title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("add_record_title_input")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Record Type:", fontSize = 12.sp, color = LifeCareTextSecondary)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        typeOptions.take(3).forEach { t ->
                            val isSelected = recordType == t
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) LifeCareTeal else LifeCareBackground,
                                modifier = Modifier.clickable { recordType = t }
                            ) {
                                Text(
                                    t,
                                    fontSize = 11.sp,
                                    color = if (isSelected) Color.White else LifeCareTextSecondary,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = doctorClinic,
                        onValueChange = { doctorClinic = it },
                        label = { Text("Doctor or Clinic Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Notes & Diagnostics") },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            onAddRecord(title, recordType, doctorClinic, date, description)
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal)
                ) {
                    Text("Save Record")
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

        AlertDialog(
            onDismissRequest = { editingRecord = null },
            title = { Text("Edit Record", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Record Title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = doctorClinic,
                        onValueChange = { doctorClinic = it },
                        label = { Text("Doctor / Clinic") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateRecord(rec.id, title, recordType, doctorClinic, description)
                        editingRecord = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal)
                ) {
                    Text("Save")
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
    val (typeColor, typeBg) = when (record.recordType) {
        "Prescription" -> Pair(LifeCareTealDark, LifeCareTealLight)
        "Lab Reports" -> Pair(Color(0xFFC0553A), LifeCarePeachLight)
        "Vaccination" -> Pair(Color(0xFF2E7D32), Color(0xFFE8F5E9))
        else -> Pair(Color(0xFF5C6BC0), Color(0xFFE8EAF6))
    }

    Card(
        shape = RoundedCornerShape(16.dp),
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
                    shape = RoundedCornerShape(8.dp),
                    color = typeBg
                ) {
                    Text(
                        text = record.recordType,
                        color = typeColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Text(record.date, fontSize = 12.sp, color = LifeCareTextSecondary)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(record.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = LifeCareTextPrimary)
            Text(record.doctorOrClinic, fontSize = 12.sp, color = LifeCareTealDark, fontWeight = FontWeight.Medium)

            if (record.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = record.description,
                    fontSize = 13.sp,
                    color = LifeCareTextSecondary,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = LifeCareBorder)
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = LifeCareTealDark, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit", color = LifeCareTealDark, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = LifeCareEmergency, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Delete", color = LifeCareEmergency, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
