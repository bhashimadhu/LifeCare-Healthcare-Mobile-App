package com.example.ui

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LifeCareRepository
import com.example.models.Doctor
import com.example.ui.auth.LoginScreen
import com.example.ui.auth.OnboardingScreen
import com.example.ui.auth.ProfileScreen
import com.example.ui.auth.RegisterScreen
import com.example.ui.auth.SplashScreen
import com.example.ui.dashboard.EmergencyScreen
import com.example.ui.dashboard.HomeDashboardScreen
import com.example.ui.doctors.BookAppointmentScreen
import com.example.ui.doctors.DoctorListScreen
import com.example.ui.doctors.MyAppointmentsScreen
import com.example.ui.medication.HealthStatusScreen
import com.example.ui.medication.MedicalRecordsScreen
import com.example.ui.medication.MedicationReminderScreen
import com.example.ui.pharmacy.CartScreen
import com.example.ui.pharmacy.MyOrdersScreen
import com.example.ui.pharmacy.PharmacyScreen
import com.example.ui.theme.LifeCareBackground
import com.example.ui.theme.LifeCareBorder
import com.example.ui.theme.LifeCareEmergency
import com.example.ui.theme.LifeCarePeachLight
import com.example.ui.theme.LifeCareSurface
import com.example.ui.theme.LifeCareTeal
import com.example.ui.theme.LifeCareTealDark
import com.example.ui.theme.LifeCareTealLight
import com.example.ui.theme.LifeCareTextMuted
import com.example.ui.theme.LifeCareTextPrimary
import com.example.ui.theme.LifeCareTextSecondary

enum class NavDestination {
    SPLASH,
    ONBOARDING,
    LOGIN,
    REGISTER,
    HOME,
    APPOINTMENTS,
    DOCTOR_LIST,
    BOOK_APPOINTMENT,
    PHARMACY,
    CART,
    ORDERS,
    REMINDERS,
    HEALTH_STATUS,
    RECORDS,
    EMERGENCY,
    PROFILE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContainer(
    repository: LifeCareRepository
) {
    val isLoggedIn by repository.isLoggedIn.collectAsState()
    val currentUser by repository.userProfile.collectAsState()
    val doctors by repository.doctors.collectAsState()
    val appointments by repository.appointments.collectAsState()
    val medicines by repository.medicines.collectAsState()
    val cart by repository.cart.collectAsState()
    val orders by repository.orders.collectAsState()
    val reminders by repository.reminders.collectAsState()
    val records by repository.medicalRecords.collectAsState()
    val healthStatus by repository.healthStatus.collectAsState()
    val emergencyContacts by repository.emergencyContacts.collectAsState()
    val isSosActive by repository.isSosActive.collectAsState()

    var currentScreen by remember { mutableStateOf(NavDestination.SPLASH) }
    var selectedDoctorForBooking by remember { mutableStateOf<Doctor?>(null) }
    var showQuickActionSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Splash navigation
    if (currentScreen == NavDestination.SPLASH) {
        SplashScreen(
            onSplashFinished = {
                currentScreen = if (isLoggedIn) NavDestination.HOME else NavDestination.LOGIN
            }
        )
        return
    }

    // Onboarding
    if (currentScreen == NavDestination.ONBOARDING) {
        OnboardingScreen(
            onFinished = { currentScreen = NavDestination.LOGIN }
        )
        return
    }

    // Login
    if (currentScreen == NavDestination.LOGIN) {
        LoginScreen(
            onLogin = { email, pass ->
                repository.login(email, pass)
            },
            onLoginSuccess = {
                currentScreen = NavDestination.HOME
            },
            onNavigateToRegister = { currentScreen = NavDestination.REGISTER }
        )
        return
    }

    // Register
    if (currentScreen == NavDestination.REGISTER) {
        RegisterScreen(
            onRegister = { name, email, phone, pass ->
                repository.register(name, email, phone, pass)
            },
            onRegisterSuccess = {
                currentScreen = NavDestination.HOME
            },
            onNavigateToLogin = { currentScreen = NavDestination.LOGIN }
        )
        return
    }

    // Main App with Bottom Bar
    Scaffold(
        bottomBar = {
            LifeCareBottomBar(
                currentScreen = currentScreen,
                onSelectTab = { tab ->
                    currentScreen = tab
                },
                onQuickAddClick = {
                    showQuickActionSheet = true
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                NavDestination.HOME -> {
                    val upcoming = appointments.firstOrNull { it.status == "Confirmed" || it.status == "Pending" }
                    HomeDashboardScreen(
                        userProfile = currentUser,
                        healthStatus = healthStatus,
                        upcomingAppointment = upcoming,
                        onNavigateToDoctorList = { currentScreen = NavDestination.DOCTOR_LIST },
                        onNavigateToPharmacy = { currentScreen = NavDestination.PHARMACY },
                        onNavigateToReminders = { currentScreen = NavDestination.REMINDERS },
                        onNavigateToEmergency = { currentScreen = NavDestination.EMERGENCY },
                        onNavigateToProfile = { currentScreen = NavDestination.PROFILE },
                        onViewAppointmentDetails = { currentScreen = NavDestination.APPOINTMENTS }
                    )
                }

                NavDestination.APPOINTMENTS -> {
                    MyAppointmentsScreen(
                        appointments = appointments,
                        doctors = doctors,
                        onUpdateAppointment = { id, date, time, reason ->
                            repository.updateAppointment(id, date, time, reason)
                        },
                        onCancelAppointment = { id ->
                            repository.cancelAppointment(id)
                        },
                        onDeleteAppointment = { id ->
                            repository.deleteAppointment(id)
                        },
                        onBookNewAppointment = {
                            currentScreen = NavDestination.DOCTOR_LIST
                        }
                    )
                }

                NavDestination.DOCTOR_LIST -> {
                    DoctorListScreen(
                        doctors = doctors,
                        onSelectDoctorForBooking = { doc ->
                            selectedDoctorForBooking = doc
                            currentScreen = NavDestination.BOOK_APPOINTMENT
                        }
                    )
                }

                NavDestination.BOOK_APPOINTMENT -> {
                    val doc = selectedDoctorForBooking ?: doctors.first()
                    BookAppointmentScreen(
                        doctor = doc,
                        currentUser = currentUser,
                        onConfirmBooking = { d, name, phone, reason, date, time ->
                            repository.bookAppointment(d, name, phone, reason, date, time)
                        },
                        onBack = { currentScreen = NavDestination.APPOINTMENTS }
                    )
                }

                NavDestination.PHARMACY -> {
                    PharmacyScreen(
                        medicines = medicines,
                        cart = cart,
                        orders = orders,
                        onAddToCart = { med, qty ->
                            repository.addToCart(med, qty)
                        },
                        onOpenCart = { currentScreen = NavDestination.CART },
                        onOpenOrders = { currentScreen = NavDestination.ORDERS }
                    )
                }

                NavDestination.CART -> {
                    CartScreen(
                        cart = cart,
                        currentUser = currentUser,
                        onUpdateQuantity = { medId, delta ->
                            repository.updateCartQuantity(medId, delta)
                        },
                        onRemoveItem = { medId ->
                            repository.removeFromCart(medId)
                        },
                        onPlaceOrder = { name, phone, address ->
                            repository.placeOrder(name, phone, address)
                        },
                        onBack = { currentScreen = NavDestination.PHARMACY }
                    )
                }

                NavDestination.ORDERS -> {
                    MyOrdersScreen(
                        orders = orders,
                        onCancelOrder = { id ->
                            repository.cancelOrder(id)
                        },
                        onBack = { currentScreen = NavDestination.PHARMACY }
                    )
                }

                NavDestination.REMINDERS -> {
                    MedicationReminderScreen(
                        reminders = reminders,
                        onAddReminder = { name, dosage, time, freq, start, end ->
                            repository.addReminder(name, dosage, time, freq, start, end)
                        },
                        onUpdateReminder = { id, name, dosage, time, freq ->
                            repository.updateReminder(id, name, dosage, time, freq)
                        },
                        onToggleTaken = { id ->
                            repository.toggleReminderTaken(id)
                        },
                        onDeleteReminder = { id ->
                            repository.deleteReminder(id)
                        }
                    )
                }

                NavDestination.HEALTH_STATUS -> {
                    HealthStatusScreen(
                        healthStatus = healthStatus,
                        onLogWater = { delta ->
                            repository.logWater(delta)
                        }
                    )
                }

                NavDestination.RECORDS -> {
                    MedicalRecordsScreen(
                        records = records,
                        userProfile = currentUser,
                        onAddRecord = { title, type, doc, date, desc ->
                            repository.addMedicalRecord(title, type, doc, date, desc)
                        },
                        onUpdateRecord = { id, title, type, doc, desc ->
                            repository.updateMedicalRecord(id, title, type, doc, desc)
                        },
                        onDeleteRecord = { id ->
                            repository.deleteMedicalRecord(id)
                        }
                    )
                }

                NavDestination.EMERGENCY -> {
                    EmergencyScreen(
                        emergencyContacts = emergencyContacts,
                        isSosActive = isSosActive,
                        onTriggerSos = { repository.triggerSos() },
                        onDismissSos = { repository.dismissSos() },
                        onAddEmergencyContact = { name, rel, phone, primary ->
                            repository.addEmergencyContact(name, rel, phone, primary)
                        },
                        onDeleteEmergencyContact = { id ->
                            repository.deleteEmergencyContact(id)
                        }
                    )
                }

                NavDestination.PROFILE -> {
                    ProfileScreen(
                        user = currentUser,
                        onUpdateProfile = { updated ->
                            repository.updateProfile(updated)
                        },
                        onLogout = {
                            repository.logout()
                            currentScreen = NavDestination.LOGIN
                        }
                    )
                }

                else -> {}
            }
        }
    }

    // Quick Add Modal Bottom Sheet triggered by the Center '+' button
    if (showQuickActionSheet) {
        ModalBottomSheet(
            onDismissRequest = { showQuickActionSheet = false },
            sheetState = sheetState,
            containerColor = LifeCareSurface,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Quick Actions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = LifeCareTextPrimary
                )
                Text(
                    text = "Fast access to all LifeCare healthcare modules",
                    fontSize = 13.sp,
                    color = LifeCareTextSecondary,
                    modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
                )

                QuickSheetItem(
                    icon = Icons.Default.MedicalServices,
                    iconBg = LifeCareTealLight,
                    iconTint = LifeCareTealDark,
                    title = "Book Doctor Appointment",
                    subtitle = "Search specialists & book consultations",
                    onClick = {
                        showQuickActionSheet = false
                        currentScreen = NavDestination.DOCTOR_LIST
                    }
                )

                QuickSheetItem(
                    icon = Icons.Default.Medication,
                    iconBg = Color(0xFFEDE7F6),
                    iconTint = Color(0xFF5E35B1),
                    title = "Add Medication Reminder",
                    subtitle = "Track daily pills and prescriptions",
                    onClick = {
                        showQuickActionSheet = false
                        currentScreen = NavDestination.REMINDERS
                    }
                )

                QuickSheetItem(
                    icon = Icons.Default.Description,
                    iconBg = LifeCarePeachLight,
                    iconTint = Color(0xFFC0553A),
                    title = "Upload Medical Record",
                    subtitle = "Store reports, lab tests & prescriptions",
                    onClick = {
                        showQuickActionSheet = false
                        currentScreen = NavDestination.RECORDS
                    }
                )

                QuickSheetItem(
                    icon = Icons.Default.ShoppingBag,
                    iconBg = LifeCareTealLight,
                    iconTint = LifeCareTealDark,
                    title = "Order Pharmacy Medicines",
                    subtitle = "Get verified medicines delivered to door",
                    onClick = {
                        showQuickActionSheet = false
                        currentScreen = NavDestination.PHARMACY
                    }
                )

                QuickSheetItem(
                    icon = Icons.Default.Favorite,
                    iconBg = Color(0xFFE8F5E9),
                    iconTint = Color(0xFF2E7D32),
                    title = "View Health Status & Vitals",
                    subtitle = "Check heart rate, blood pressure and water log",
                    onClick = {
                        showQuickActionSheet = false
                        currentScreen = NavDestination.HEALTH_STATUS
                    }
                )

                QuickSheetItem(
                    icon = Icons.Default.WarningAmber,
                    iconBg = Color(0xFFFFECEC),
                    iconTint = LifeCareEmergency,
                    title = "Emergency SOS",
                    subtitle = "Broadcast urgent medical alert",
                    onClick = {
                        showQuickActionSheet = false
                        currentScreen = NavDestination.EMERGENCY
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun LifeCareBottomBar(
    currentScreen: NavDestination,
    onSelectTab: (NavDestination) -> Unit,
    onQuickAddClick: () -> Unit
) {
    Surface(
        color = LifeCareSurface,
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Home
            BottomNavItem(
                icon = Icons.Default.Home,
                label = "Home",
                isSelected = currentScreen == NavDestination.HOME,
                onClick = { onSelectTab(NavDestination.HOME) },
                testTag = "nav_home"
            )

            // 2. Appointments
            BottomNavItem(
                icon = Icons.Default.CalendarMonth,
                label = "Appointments",
                isSelected = currentScreen == NavDestination.APPOINTMENTS || currentScreen == NavDestination.DOCTOR_LIST || currentScreen == NavDestination.BOOK_APPOINTMENT,
                onClick = { onSelectTab(NavDestination.APPOINTMENTS) },
                testTag = "nav_appointments"
            )

            // 3. Center Quick '+' Button
            Box(
                modifier = Modifier
                    .offset(y = (-10).dp)
                    .size(54.dp)
                    .shadow(elevation = 6.dp, shape = CircleShape)
                    .background(LifeCareTeal, shape = CircleShape)
                    .clickable { onQuickAddClick() }
                    .testTag("nav_quick_add"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Quick Add",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            // 4. Records
            BottomNavItem(
                icon = Icons.Default.Description,
                label = "Records",
                isSelected = currentScreen == NavDestination.RECORDS || currentScreen == NavDestination.HEALTH_STATUS || currentScreen == NavDestination.REMINDERS,
                onClick = { onSelectTab(NavDestination.RECORDS) },
                testTag = "nav_records"
            )

            // 5. Profile
            BottomNavItem(
                icon = Icons.Default.Person,
                label = "Profile",
                isSelected = currentScreen == NavDestination.PROFILE,
                onClick = { onSelectTab(NavDestination.PROFILE) },
                testTag = "nav_profile"
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag(testTag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) LifeCareTeal else LifeCareTextMuted,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) LifeCareTeal else LifeCareTextMuted
        )
    }
}

@Composable
private fun QuickSheetItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = LifeCareBackground,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = iconBg,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = LifeCareTextPrimary)
                Text(subtitle, fontSize = 12.sp, color = LifeCareTextSecondary)
            }
        }
    }
}
