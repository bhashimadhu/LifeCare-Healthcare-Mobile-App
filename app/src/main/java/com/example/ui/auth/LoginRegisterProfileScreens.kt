package com.example.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
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

import androidx.compose.material3.CircularProgressIndicator
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import android.util.Patterns
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

/**
 * Student 1: Login Screen (Room SQLite Database Integrated)
 * White card over a light mint background with teal Login button.
 */
@Composable
fun LoginScreen(
    onLogin: suspend (String, String) -> Pair<Boolean, String>,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LifeCareTealLight)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = LifeCareSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // LifeCare Icon & Header
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = LifeCareTealLight,
                    modifier = Modifier.size(64.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_lifecare_logo),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Welcome to LifeCare",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = LifeCareTextPrimary
                )

                Text(
                    text = "Sign in to access your healthcare portal",
                    fontSize = 13.sp,
                    color = LifeCareTextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Database indicator badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFE8F5E9),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Room SQLite Database Secure",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Quick Demo Account Chip
                Surface(
                    onClick = {
                        email = "kasun.perera@gmail.com"
                        password = "student123"
                        errorMessage = null
                    },
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFE0F2FE),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color(0xFF0284C7),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Quick Fill Demo: kasun.perera@gmail.com",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF0369A1)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Email field
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        errorMessage = null
                    },
                    label = { Text("Email Address") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null, tint = LifeCareTeal)
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LifeCareTeal,
                        unfocusedBorderColor = LifeCareBorder
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("login_email_input")
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password field
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = null
                    },
                    label = { Text("Password") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = LifeCareTeal)
                    },
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = LifeCareTextSecondary
                            )
                        }
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LifeCareTeal,
                        unfocusedBorderColor = LifeCareBorder
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("login_password_input")
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage ?: "",
                        color = LifeCareEmergency,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                if (successMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = successMessage ?: "",
                        color = Color(0xFF2E7D32),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Forgot Password
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { showForgotPasswordDialog = true }) {
                        Text(
                            text = "Forgot Password?",
                            color = LifeCareTealDark,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Login Button
                Button(
                    onClick = {
                        if (email.isBlank()) {
                            errorMessage = "Please enter your email"
                        } else if (password.length < 6) {
                            errorMessage = "Password must contain at least 6 characters"
                        } else {
                            isLoading = true
                            errorMessage = null
                            coroutineScope.launch {
                                val (ok, msg) = onLogin(email, password)
                                isLoading = false
                                if (ok) {
                                    successMessage = msg
                                    onLoginSuccess()
                                } else {
                                    errorMessage = msg
                                }
                            }
                        }
                    },
                    enabled = !isLoading,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("login_submit_button")
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    } else {
                        Text(
                            text = "Login",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                HorizontalDivider(color = LifeCareBorder)

                Spacer(modifier = Modifier.height(16.dp))

                // Create Account link
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Don't have an account?",
                        fontSize = 14.sp,
                        color = LifeCareTextSecondary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(
                        onClick = onNavigateToRegister,
                        modifier = Modifier.testTag("navigate_to_register_button")
                    ) {
                        Text(
                            text = "Create Account",
                            fontWeight = FontWeight.Bold,
                            color = LifeCareTealDark,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }

    if (showForgotPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showForgotPasswordDialog = false },
            title = { Text("Reset Password", fontWeight = FontWeight.Bold) },
            text = {
                Text("A password reset link has been sent to $email. Please check your inbox.")
            },
            confirmButton = {
                Button(
                    onClick = { showForgotPasswordDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal)
                ) {
                    Text("OK")
                }
            }
        )
    }
}

/**
 * Student 1: Registration Screen (Room SQLite Database Integrated)
 * Full Name, Email, Phone Number, Password, Confirm Password with real database insertion.
 */
@Composable
fun RegisterScreen(
    onRegister: suspend (String, String, String, String) -> Pair<Boolean, String>,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LifeCareTealLight)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = LifeCareSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Create Account",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = LifeCareTextPrimary
                )

                Text(
                    text = "Join LifeCare with persistent local database storage",
                    fontSize = 13.sp,
                    color = LifeCareTextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Database badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFE8F5E9),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Stores in SQLite Room Database",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Full Name
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it; errorMessage = null },
                    label = { Text("Full Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = LifeCareTeal) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LifeCareTeal,
                        unfocusedBorderColor = LifeCareBorder
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().testTag("register_name_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; errorMessage = null },
                    label = { Text("Email Address") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = LifeCareTeal) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LifeCareTeal,
                        unfocusedBorderColor = LifeCareBorder
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().testTag("register_email_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Phone
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it; errorMessage = null },
                    label = { Text("Phone Number") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = LifeCareTeal) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LifeCareTeal,
                        unfocusedBorderColor = LifeCareBorder
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().testTag("register_phone_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; errorMessage = null },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = LifeCareTeal) },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LifeCareTeal,
                        unfocusedBorderColor = LifeCareBorder
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().testTag("register_password_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Confirm Password
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it; errorMessage = null },
                    label = { Text("Confirm Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = LifeCareTeal) },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LifeCareTeal,
                        unfocusedBorderColor = LifeCareBorder
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().testTag("register_confirm_password_input")
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = errorMessage ?: "",
                        color = LifeCareEmergency,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                if (successMessage != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = successMessage ?: "",
                        color = Color(0xFF2E7D32),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Submit Button
                Button(
                    onClick = {
                        if (fullName.isBlank()) {
                            errorMessage = "Please enter your name"
                        } else if (email.isBlank() || !email.contains("@")) {
                            errorMessage = "Please enter a valid email"
                        } else if (password.length < 6) {
                            errorMessage = "Password must contain at least 6 characters"
                        } else if (password != confirmPassword) {
                            errorMessage = "Passwords do not match"
                        } else {
                            isLoading = true
                            errorMessage = null
                            coroutineScope.launch {
                                val (ok, msg) = onRegister(fullName, email, phone, password)
                                isLoading = false
                                if (ok) {
                                    successMessage = msg
                                    onRegisterSuccess()
                                } else {
                                    errorMessage = msg
                                }
                            }
                        }
                    },
                    enabled = !isLoading,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("register_submit_button")
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    } else {
                        Text(
                            text = "Create Account",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Already have an account?",
                        fontSize = 14.sp,
                        color = LifeCareTextSecondary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(
                        onClick = onNavigateToLogin,
                        modifier = Modifier.testTag("navigate_to_login_button")
                    ) {
                        Text(
                            text = "Login",
                            fontWeight = FontWeight.Bold,
                            color = LifeCareTealDark,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Student 1: User Profile Screen (with full CRUD Update)
 */
@Composable
fun ProfileScreen(
    user: UserProfile,
    onUpdateProfile: (UserProfile) -> Unit,
    onLogout: () -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LifeCareBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Screen Header
        Text(
            text = "My Profile",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = LifeCareTextPrimary
        )
        Text(
            text = "Personal and medical identification details",
            fontSize = 13.sp,
            color = LifeCareTextSecondary,
            modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
        )

        // Profile Avatar Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = LifeCareSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Surface(
                        shape = CircleShape,
                        color = LifeCareTealLight,
                        modifier = Modifier.size(80.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile Photo",
                                tint = LifeCareTealDark,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                    Surface(
                        shape = CircleShape,
                        color = LifeCareTeal,
                        modifier = Modifier.size(26.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = user.fullName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = LifeCareTextPrimary
                )

                Text(
                    text = user.email,
                    fontSize = 14.sp,
                    color = LifeCareTextSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ProfileBadge(label = "Blood Group", value = user.bloodGroup, color = LifeCarePeachLight, textColor = Color(0xFFC0553A))
                    ProfileBadge(label = "Age", value = "${user.age} Yrs", color = LifeCareTealLight, textColor = LifeCareTealDark)
                    ProfileBadge(label = "Status", value = "Verified", color = Color(0xFFE8F5E9), textColor = Color(0xFF2E7D32))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Personal Information List
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = LifeCareSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Contact & Emergency Details",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = LifeCareTextPrimary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                ProfileInfoRow(
                    icon = Icons.Default.Phone,
                    label = "Phone Number",
                    value = user.phone
                )
                HorizontalDivider(color = LifeCareBorder, modifier = Modifier.padding(vertical = 8.dp))

                ProfileInfoRow(
                    icon = Icons.Default.Bloodtype,
                    label = "Blood Group",
                    value = user.bloodGroup
                )
                HorizontalDivider(color = LifeCareBorder, modifier = Modifier.padding(vertical = 8.dp))

                ProfileInfoRow(
                    icon = Icons.Default.HealthAndSafety,
                    label = "Emergency Contact",
                    value = user.emergencyContact
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Action Buttons: Edit Profile, Settings, Logout
        Button(
            onClick = { showEditDialog = true },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("edit_profile_button")
        ) {
            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Edit Profile", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = { showSettingsDialog = true },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = LifeCareTextPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("profile_settings_button")
        ) {
            Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Settings & Preferences", fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onLogout,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = LifeCareEmergency),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("logout_button")
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null, tint = LifeCareEmergency, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout", color = LifeCareEmergency, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Edit Profile Dialog (CRUD: Update)
    if (showEditDialog) {
        var editName by remember { mutableStateOf(user.fullName) }
        var editPhone by remember { mutableStateOf(user.phone) }
        var editAge by remember { mutableStateOf(user.age.toString()) }
        var editBlood by remember { mutableStateOf(user.bloodGroup) }
        var editEmergency by remember { mutableStateOf(user.emergencyContact) }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Profile Details", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Full Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editPhone,
                        onValueChange = { editPhone = it },
                        label = { Text("Phone Number") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editAge,
                        onValueChange = { editAge = it },
                        label = { Text("Age") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editBlood,
                        onValueChange = { editBlood = it },
                        label = { Text("Blood Group (e.g. O+, A-, B+)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editEmergency,
                        onValueChange = { editEmergency = it },
                        label = { Text("Emergency Contact") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsedAge = editAge.toIntOrNull() ?: user.age
                        onUpdateProfile(
                            user.copy(
                                fullName = editName,
                                phone = editPhone,
                                age = parsedAge,
                                bloodGroup = editBlood,
                                emergencyContact = editEmergency
                            )
                        )
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal)
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel", color = LifeCareTextSecondary)
                }
            }
        )
    }

    // Settings Info Dialog
    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text("Settings & Privacy", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("LifeCare Mobile v1.0", fontWeight = FontWeight.Bold, color = LifeCareTealDark)
                    Text("Developed for University Mobile App Mini Project (Team of 5).", fontSize = 13.sp, color = LifeCareTextSecondary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Push Notifications: Enabled\n• Data Sync: Firestore Cloud Active\n• SDG 3: Good Health & Well-being", fontSize = 13.sp)
                }
            },
            confirmButton = {
                Button(
                    onClick = { showSettingsDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal)
                ) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
private fun ProfileBadge(label: String, value: String, color: Color, textColor: Color) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = textColor)
            Text(text = label, fontSize = 11.sp, color = textColor.copy(alpha = 0.8f))
        }
    }
}

@Composable
private fun ProfileInfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = LifeCareTealLight,
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = LifeCareTealDark, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = label, fontSize = 12.sp, color = LifeCareTextSecondary)
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = LifeCareTextPrimary)
        }
    }
}
