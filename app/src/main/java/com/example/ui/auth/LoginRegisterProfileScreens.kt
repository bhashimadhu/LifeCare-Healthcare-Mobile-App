package com.example.ui.auth

import android.util.Patterns
import com.example.models.UserProfile

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.width

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import kotlinx.coroutines.launch


private val LifeCareTeal = Color(0xFF18B8B2)
private val LifeCareLightMint = Color(0xFFEAF9F7)
private val LifeCarePeach = Color(0xFFFFEDE7)
private val LifeCareText = Color(0xFF263238)
private val LifeCareSecondaryText = Color(0xFF718286)


/* ---------------------------------------------------------
   STUDENT 1 - LOGIN SCREEN
--------------------------------------------------------- */

@Composable
fun LoginScreen(
    onLogin: suspend (String, String) -> Pair<Boolean, String>,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var emailError by remember {
        mutableStateOf<String?>(null)
    }

    var passwordError by remember {
        mutableStateOf<String?>(null)
    }

    var loginError by remember {
        mutableStateOf<String?>(null)
    }

    var showPassword by remember {
        mutableStateOf(false)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LifeCareLightMint)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "LifeCare",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = LifeCareTeal
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Your Health, Anytime, Anywhere",
                color = LifeCareSecondaryText
            )

            Spacer(
                modifier = Modifier.height(30.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(22.dp)
                ) {

                    Text(
                        text = "Welcome Back",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = LifeCareText
                    )

                    Spacer(
                        modifier = Modifier.height(5.dp)
                    )

                    Text(
                        text = "Login to continue to LifeCare",
                        color = LifeCareSecondaryText
                    )

                    Spacer(
                        modifier = Modifier.height(22.dp)
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = null
                            loginError = null
                        },
                        label = {
                            Text("Email Address")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = emailError != null,
                        supportingText = {

                            if (emailError != null) {

                                Text(
                                    text = emailError ?: ""
                                )
                            }
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = null
                            loginError = null
                        },
                        label = {
                            Text("Password")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        visualTransformation =
                            if (showPassword) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                        trailingIcon = {

                            Text(
                                text =
                                    if (showPassword) {
                                        "Hide"
                                    } else {
                                        "Show"
                                    },
                                color = LifeCareTeal,
                                modifier = Modifier.clickable {
                                    showPassword = !showPassword
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = passwordError != null,
                        supportingText = {

                            if (passwordError != null) {

                                Text(
                                    text = passwordError ?: ""
                                )
                            }
                        }
                    )

                    if (loginError != null) {

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        Text(
                            text = loginError ?: "",
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(22.dp)
                    )

                    Button(
                        onClick = {

                            emailError = when {

                                email.isBlank() ->
                                    "Email is required"

                                !Patterns.EMAIL_ADDRESS
                                    .matcher(email.trim())
                                    .matches() ->
                                    "Enter a valid email address"

                                else ->
                                    null
                            }

                            passwordError = when {

                                password.isBlank() ->
                                    "Password is required"

                                password.length < 6 ->
                                    "Password must be at least 6 characters"

                                else ->
                                    null
                            }

                            if (
                                emailError == null &&
                                passwordError == null
                            ) {

                                isLoading = true
                                loginError = null

                                coroutineScope.launch {

                                    val result = onLogin(
                                        email.trim(),
                                        password
                                    )

                                    isLoading = false

                                    if (result.first) {

                                        onLoginSuccess()

                                    } else {

                                        loginError =
                                            if (result.second.isBlank()) {
                                                "Login failed. Please try again."
                                            } else {
                                                result.second
                                            }
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        enabled = !isLoading,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LifeCareTeal
                        )
                    ) {

                        if (isLoading) {

                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp,
                                color = Color.White
                            )

                        } else {

                            Text(
                                text = "Login",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = "Don't have an account? ",
                            color = LifeCareSecondaryText
                        )

                        Text(
                            text = "Register",
                            color = LifeCareTeal,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                onNavigateToRegister()
                            }
                        )
                    }
                }
            }
        }
    }
}


/* ---------------------------------------------------------
   STUDENT 1 - REGISTER SCREEN
--------------------------------------------------------- */

@Composable
fun RegisterScreen(
    onRegister: suspend (
        String,
        String,
        String,
        String
    ) -> Pair<Boolean, String>,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()

    var fullName by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var phone by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LifeCareLightMint)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Create Account",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = LifeCareText
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = "Join LifeCare and manage your health easily.",
                color = LifeCareSecondaryText
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(22.dp)
                ) {

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = {
                            fullName = it
                            errorMessage = null
                        },
                        label = {
                            Text("Full Name")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            errorMessage = null
                        },
                        label = {
                            Text("Email Address")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = {
                            phone = it
                            errorMessage = null
                        },
                        label = {
                            Text("Phone Number")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorMessage = null
                        },
                        label = {
                            Text("Password")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        visualTransformation =
                            PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            errorMessage = null
                        },
                        label = {
                            Text("Confirm Password")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        visualTransformation =
                            PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    if (errorMessage != null) {

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        Text(
                            text = errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(22.dp)
                    )

                    Button(
                        onClick = {

                            errorMessage = when {
                                fullName.trim().isEmpty() ->
                                    "Full name cannot be empty"

                                email.trim().isEmpty() ->
                                    "Email cannot be empty"

                                !Patterns.EMAIL_ADDRESS
                                    .matcher(email.trim())
                                    .matches() ->
                                    "Please enter a valid email address"

                                phone.trim().isEmpty() ->
                                    "Phone number cannot be empty"

                                password.isEmpty() ->
                                    "Password cannot be empty"

                                password.length < 6 ->
                                    "Password must contain at least 6 characters"

                                confirmPassword.isEmpty() ->
                                    "Confirm password cannot be empty"

                                password != confirmPassword ->
                                    "Passwords do not match"

                                else ->
                                    null
                            }

                            if (errorMessage == null) {

                                isLoading = true

                                coroutineScope.launch {

                                    val result = onRegister(
                                        fullName.trim(),
                                        email.trim(),
                                        phone.trim(),
                                        password
                                    )

                                    isLoading = false

                                    if (result.first) {

                                        onRegisterSuccess()

                                    } else {

                                        errorMessage =
                                            if (result.second.isBlank()) {
                                                "Registration failed"
                                            } else {
                                                result.second
                                            }
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        enabled = !isLoading,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LifeCareTeal
                        )
                    ) {

                        if (isLoading) {

                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp,
                                color = Color.White
                            )

                        } else {

                            Text(
                                text = "Create Account",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = "Already have an account? ",
                            color = LifeCareSecondaryText
                        )

                        Text(
                            text = "Login",
                            color = LifeCareTeal,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                onNavigateToLogin()
                            }
                        )
                    }
                }
            }
        }
    }
}


/* ---------------------------------------------------------
   STUDENT 1 - PROFILE SCREEN
--------------------------------------------------------- */

@Composable
fun ProfileScreen(
    user: UserProfile = UserProfile(),
    onUpdateProfile: (UserProfile) -> Unit = {},
    onLogout: () -> Unit = {}
) {

    var isEditing by remember {
        mutableStateOf(false)
    }

    if (isEditing) {
        EditProfileScreen(
            currentFullName = user.fullName,
            currentPhone = user.phone,
            currentAge = user.age.toString(),
            currentBloodGroup = user.bloodGroup,
            currentEmergencyContact = user.emergencyContact,
            onSave = { name, phone, age, blood, emergency ->
                onUpdateProfile(
                    user.copy(
                        fullName = name,
                        phone = phone,
                        age = age.toIntOrNull() ?: user.age,
                        bloodGroup = blood,
                        emergencyContact = emergency
                    )
                )
                isEditing = false
            },
            onBack = {
                isEditing = false
            }
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LifeCareLightMint)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Text(
                    text = "My Profile",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = LifeCareText
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                // Header Profile Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .background(
                                    color = LifeCarePeach,
                                    shape = RoundedCornerShape(45.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text =
                                    user.fullName
                                        .take(1)
                                        .uppercase(),
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = LifeCareTeal
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        Text(
                            text =
                                if (user.fullName.isBlank()) {
                                    "LifeCare User"
                                } else {
                                    user.fullName
                                },
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = LifeCareText
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = user.email,
                            fontSize = 14.sp,
                            color = LifeCareSecondaryText
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                // Detailed Information Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            text = "Health Profile",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = LifeCareText,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        ProfileInformationRow(
                            icon = Icons.Default.Email,
                            title = "Email Address",
                            value =
                                if (user.email.isBlank()) {
                                    "Not added"
                                } else {
                                    user.email
                                }
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 4.dp),
                            thickness = 0.5.dp,
                            color = Color.LightGray.copy(alpha = 0.3f)
                        )

                        ProfileInformationRow(
                            icon = Icons.Default.Phone,
                            title = "Phone Number",
                            value =
                                if (user.phone.isBlank()) {
                                    "Not added"
                                } else {
                                    user.phone
                                }
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 4.dp),
                            thickness = 0.5.dp,
                            color = Color.LightGray.copy(alpha = 0.3f)
                        )

                        ProfileInformationRow(
                            icon = Icons.Default.Cake,
                            title = "Age",
                            value =
                                if (user.age == 0) {
                                    "Not added"
                                } else {
                                    "${user.age} Years"
                                }
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 4.dp),
                            thickness = 0.5.dp,
                            color = Color.LightGray.copy(alpha = 0.3f)
                        )

                        ProfileInformationRow(
                            icon = Icons.Default.Opacity,
                            title = "Blood Group",
                            value =
                                if (user.bloodGroup.isBlank()) {
                                    "Not added"
                                } else {
                                    user.bloodGroup
                                }
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 4.dp),
                            thickness = 0.5.dp,
                            color = Color.LightGray.copy(alpha = 0.3f)
                        )

                        ProfileInformationRow(
                            icon = Icons.Default.ContactPhone,
                            title = "Emergency Contact",
                            value =
                                if (user.emergencyContact.isBlank()) {
                                    "Not added"
                                } else {
                                    user.emergencyContact
                                }
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Button(
                    onClick = {
                        isEditing = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LifeCareTeal
                    )
                ) {

                    Text(
                        text = "Edit Profile",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                OutlinedButton(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, LifeCareTeal.copy(alpha = 0.5f))
                ) {

                    Text(
                        text = "Logout",
                        fontSize = 16.sp,
                        color = LifeCareTeal
                    )
                }

                Spacer(
                    modifier = Modifier.height(40.dp)
                )
            }
        }
    }
}


/* ---------------------------------------------------------
   STUDENT 1 - EDIT PROFILE SCREEN
--------------------------------------------------------- */

@Composable
fun EditProfileScreen(
    currentFullName: String = "",
    currentPhone: String = "",
    currentAge: String = "",
    currentBloodGroup: String = "",
    currentEmergencyContact: String = "",
    onSave: (
        String,
        String,
        String,
        String,
        String
    ) -> Unit = { _, _, _, _, _ -> },
    onBack: () -> Unit = {}
) {

    var fullName by remember { mutableStateOf(currentFullName) }
    var phone by remember { mutableStateOf(currentPhone) }
    var age by remember { mutableStateOf(currentAge) }
    var bloodGroup by remember { mutableStateOf(currentBloodGroup) }
    var emergencyContact by remember { mutableStateOf(currentEmergencyContact) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LifeCareLightMint)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = LifeCareTeal
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Edit Profile",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = LifeCareText
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {

                Column(
                    modifier = Modifier.padding(24.dp)
                ) {

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = {
                            fullName = it
                            errorMessage = null
                        },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = {
                            phone = it
                            errorMessage = null
                        },
                        label = { Text("Phone Number") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = age,
                        onValueChange = {
                            age = it
                            errorMessage = null
                        },
                        label = { Text("Age") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = bloodGroup,
                        onValueChange = {
                            bloodGroup = it
                            errorMessage = null
                        },
                        label = { Text("Blood Group") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = emergencyContact,
                        onValueChange = {
                            emergencyContact = it
                            errorMessage = null
                        },
                        label = { Text("Emergency Contact") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            errorMessage = when {
                                fullName.isBlank() -> "Full name is required"
                                phone.isNotBlank() && phone.length < 9 -> "Enter a valid phone number"
                                age.isNotBlank() && age.toIntOrNull() == null -> "Enter a valid age"
                                else -> null
                            }

                            if (errorMessage == null) {
                                onSave(
                                    fullName.trim(),
                                    phone.trim(),
                                    age.trim(),
                                    bloodGroup.trim(),
                                    emergencyContact.trim()
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal)
                    ) {
                        Text(
                            text = "Save Changes",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


/* ---------------------------------------------------------
   PROFILE INFORMATION ROW
--------------------------------------------------------- */

@Composable
private fun ProfileInformationRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(LifeCarePeach.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = LifeCareTeal,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = title,
                fontSize = 12.sp,
                color = LifeCareSecondaryText
            )
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = LifeCareText
            )
        }
    }
}
