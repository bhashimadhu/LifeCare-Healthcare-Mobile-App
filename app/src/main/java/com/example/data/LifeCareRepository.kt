package com.example.data

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.local.UserDao
import com.example.data.local.UserEntity
import com.example.models.Appointment
import com.example.models.CartItem
import com.example.models.Doctor
import com.example.models.EmergencyContact
import com.example.models.HealthStatus
import com.example.models.MedicalRecord
import com.example.models.MedicationReminder
import com.example.models.Medicine
import com.example.models.MedicineOrder
import com.example.models.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

/**
 * LifeCare Central Data Repository
 * Features local SQLite Room Database persistence for Authentication, Sign-in, and Profiles,
 * with reactive StateFlow state and complete CRUD for all 5 student modules.
 */
class LifeCareRepository(private val context: Context? = null) {

    private val dbScope = CoroutineScope(Dispatchers.IO)
    private val userDao: UserDao? = context?.let { AppDatabase.getDatabase(it).userDao() }

    // --- Student 1: Auth & User Profile State ---
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _userProfile = MutableStateFlow(
        UserProfile(
            uid = "",
            fullName = "Guest User",
            email = "",
            phone = "",
            age = 0,
            bloodGroup = "O+",
            emergencyContact = ""
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    init {
        // Initialize Room DB with demo user if empty
        userDao?.let { dao ->
            dbScope.launch {
                try {
                    val count = dao.getUserCount()
                    if (count == 0) {
                        dao.insertUser(
                            UserEntity(
                                id = "usr_student_01",
                                fullName = "Kasun Perera",
                                email = "kasun.perera@gmail.com",
                                phone = "+94 77 123 4567",
                                password = "student123",
                                age = 24,
                                bloodGroup = "O+",
                                emergencyContact = "+94 71 987 6543 (Amma)"
                            )
                        )
                    }
                } catch (_: Exception) { }
            }
        }
    }

    // --- Student 2: Doctors & Appointments State ---
    private val _doctors = MutableStateFlow(getInitialDoctors())
    val doctors: StateFlow<List<Doctor>> = _doctors.asStateFlow()

    private val _appointments = MutableStateFlow(getInitialAppointments())
    val appointments: StateFlow<List<Appointment>> = _appointments.asStateFlow()

    // --- Student 3: Pharmacy & Orders State ---
    private val _medicines = MutableStateFlow(getInitialMedicines())
    val medicines: StateFlow<List<Medicine>> = _medicines.asStateFlow()

    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    private val _orders = MutableStateFlow(getInitialOrders())
    val orders: StateFlow<List<MedicineOrder>> = _orders.asStateFlow()

    // --- Student 4: Reminders, Records & Health Status ---
    private val _reminders = MutableStateFlow(getInitialReminders())
    val reminders: StateFlow<List<MedicationReminder>> = _reminders.asStateFlow()

    private val _medicalRecords = MutableStateFlow(getInitialRecords())
    val medicalRecords: StateFlow<List<MedicalRecord>> = _medicalRecords.asStateFlow()

    private val _healthStatus = MutableStateFlow(HealthStatus())
    val healthStatus: StateFlow<HealthStatus> = _healthStatus.asStateFlow()

    // --- Student 5: Emergency SOS & Contacts ---
    private val _emergencyContacts = MutableStateFlow(getInitialEmergencyContacts())
    val emergencyContacts: StateFlow<List<EmergencyContact>> = _emergencyContacts.asStateFlow()

    private val _isSosActive = MutableStateFlow(false)
    val isSosActive: StateFlow<Boolean> = _isSosActive.asStateFlow()

    // ==========================================
    // MODULE 1: AUTH & PROFILE CRUD (ROOM DB INTEGRATED)
    // ==========================================

    suspend fun login(email: String, pass: String): Pair<Boolean, String> = withContext(Dispatchers.IO) {
        if (email.isBlank()) {
            return@withContext Pair(false, "Please enter your email address")
        }
        if (pass.length < 6) {
            return@withContext Pair(false, "Password must be at least 6 characters")
        }

        if (userDao != null) {
            val user = userDao.getUserByEmail(email.trim())
            if (user == null) {
                return@withContext Pair(false, "No registered account found with this email in database")
            }
            if (user.password != pass) {
                return@withContext Pair(false, "Incorrect password. Please verify credentials.")
            }
            val profile = UserProfile(
                uid = user.id,
                fullName = user.fullName,
                email = user.email,
                phone = user.phone,
                age = user.age,
                bloodGroup = user.bloodGroup,
                emergencyContact = user.emergencyContact
            )
            _userProfile.value = profile
            _isLoggedIn.value = true
            return@withContext Pair(true, "Login successful!")
        } else {
            _userProfile.update { it.copy(email = email) }
            _isLoggedIn.value = true
            return@withContext Pair(true, "Login successful!")
        }
    }

    suspend fun register(name: String, email: String, phone: String, pass: String): Pair<Boolean, String> = withContext(Dispatchers.IO) {
        if (name.isBlank()) {
            return@withContext Pair(false, "Please enter your full name")
        }
        if (email.isBlank() || !email.contains("@")) {
            return@withContext Pair(false, "Please enter a valid email address")
        }
        if (pass.length < 6) {
            return@withContext Pair(false, "Password must contain at least 6 characters")
        }

        if (userDao != null) {
            val existing = userDao.getUserByEmail(email.trim())
            if (existing != null) {
                return@withContext Pair(false, "This email is already registered in the database. Please Login.")
            }
            val newId = "usr_${System.currentTimeMillis() % 100000}"
            val newEntity = UserEntity(
                id = newId,
                fullName = name.trim(),
                email = email.trim(),
                phone = phone.ifBlank { "+94 77 000 0000" },
                password = pass,
                age = 24,
                bloodGroup = "O+",
                emergencyContact = "+94 71 987 6543 (Amma)"
            )
            try {
                userDao.insertUser(newEntity)
                val newProfile = UserProfile(
                    uid = newId,
                    fullName = newEntity.fullName,
                    email = newEntity.email,
                    phone = newEntity.phone,
                    age = newEntity.age,
                    bloodGroup = newEntity.bloodGroup,
                    emergencyContact = newEntity.emergencyContact
                )
                _userProfile.value = newProfile
                _isLoggedIn.value = true
                return@withContext Pair(true, "Account created successfully in database!")
            } catch (e: Exception) {
                return@withContext Pair(false, "Database error: ${e.localizedMessage}")
            }
        } else {
            val newUser = UserProfile(
                uid = UUID.randomUUID().toString(),
                fullName = name,
                email = email,
                phone = phone.ifBlank { "+94 77 000 0000" }
            )
            _userProfile.value = newUser
            _isLoggedIn.value = true
            return@withContext Pair(true, "Account created successfully!")
        }
    }

    fun updateProfile(updated: UserProfile) {
        _userProfile.value = updated
        userDao?.let { dao ->
            dbScope.launch {
                try {
                    val entity = dao.getUserById(updated.uid)
                    if (entity != null) {
                        dao.updateUser(
                            entity.copy(
                                fullName = updated.fullName,
                                email = updated.email,
                                phone = updated.phone,
                                age = updated.age,
                                bloodGroup = updated.bloodGroup,
                                emergencyContact = updated.emergencyContact
                            )
                        )
                    }
                } catch (_: Exception) {}
            }
        }
    }

    fun logout() {
        _isLoggedIn.value = false
        _userProfile.value = UserProfile(
            uid = "",
            fullName = "Guest User",
            email = "",
            phone = "",
            age = 0,
            bloodGroup = "O+",
            emergencyContact = ""
        )
    }

    // ==========================================
    // MODULE 2: APPOINTMENTS CRUD
    // ==========================================

    fun bookAppointment(
        doctor: Doctor,
        patientName: String,
        patientPhone: String,
        reason: String,
        date: String,
        time: String
    ): Appointment {
        val newAppointment = Appointment(
            id = "APT-${System.currentTimeMillis() % 10000}",
            doctorId = doctor.id,
            doctorName = doctor.name,
            specialization = doctor.specialization,
            patientName = patientName.ifBlank { _userProfile.value.fullName },
            patientPhone = patientPhone.ifBlank { _userProfile.value.phone },
            reason = reason.ifBlank { "General Checkup" },
            date = date,
            time = time,
            consultationFee = doctor.consultationFee,
            status = "Pending"
        )
        _appointments.update { listOf(newAppointment) + it }
        return newAppointment
    }

    fun updateAppointment(appointmentId: String, newDate: String, newTime: String, reason: String) {
        _appointments.update { list ->
            list.map {
                if (it.id == appointmentId) {
                    it.copy(date = newDate, time = newTime, reason = reason)
                } else it
            }
        }
    }

    fun cancelAppointment(appointmentId: String) {
        _appointments.update { list ->
            list.map {
                if (it.id == appointmentId) it.copy(status = "Cancelled") else it
            }
        }
    }

    fun deleteAppointment(appointmentId: String) {
        _appointments.update { list ->
            list.filterNot { it.id == appointmentId }
        }
    }

    // ==========================================
    // MODULE 3: PHARMACY & CART & ORDERS CRUD
    // ==========================================

    fun addToCart(medicine: Medicine, qty: Int = 1) {
        _cart.update { current ->
            val existing = current.find { it.medicine.id == medicine.id }
            if (existing != null) {
                current.map {
                    if (it.medicine.id == medicine.id) it.copy(quantity = it.quantity + qty) else it
                }
            } else {
                current + CartItem(medicine, qty)
            }
        }
    }

    fun updateCartQuantity(medicineId: String, delta: Int) {
        _cart.update { current ->
            current.mapNotNull {
                if (it.medicine.id == medicineId) {
                    val newQty = it.quantity + delta
                    if (newQty > 0) it.copy(quantity = newQty) else null
                } else it
            }
        }
    }

    fun removeFromCart(medicineId: String) {
        _cart.update { current ->
            current.filterNot { it.medicine.id == medicineId }
        }
    }

    fun clearCart() {
        _cart.value = emptyList()
    }

    fun placeOrder(customerName: String, phone: String, address: String): MedicineOrder? {
        val currentCart = _cart.value
        if (currentCart.isEmpty()) return null

        val subtotal = currentCart.sumOf { it.medicine.price * it.quantity }
        val deliveryFee = 50.0
        val total = subtotal + deliveryFee
        val summary = currentCart.joinToString(", ") { "${it.medicine.name} (x${it.quantity})" }

        val newOrder = MedicineOrder(
            id = "ORD-${System.currentTimeMillis() % 100000}",
            customerName = customerName.ifBlank { _userProfile.value.fullName },
            phone = phone.ifBlank { _userProfile.value.phone },
            address = address.ifBlank { "University Student Hostel, Block B, Room 304" },
            itemsSummary = summary,
            itemCount = currentCart.sumOf { it.quantity },
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            total = total,
            paymentMethod = "Cash on Delivery",
            status = "Preparing",
            date = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
        )

        _orders.update { listOf(newOrder) + it }
        clearCart()
        return newOrder
    }

    fun cancelOrder(orderId: String) {
        _orders.update { list ->
            list.map {
                if (it.id == orderId) it.copy(status = "Cancelled") else it
            }
        }
    }

    // ==========================================
    // MODULE 4: MEDICATION REMINDERS CRUD
    // ==========================================

    fun addReminder(
        name: String,
        dosage: String,
        time: String,
        frequency: String,
        startDate: String,
        endDate: String
    ) {
        val newReminder = MedicationReminder(
            id = "REM-${System.currentTimeMillis() % 10000}",
            medicineName = name,
            dosage = dosage.ifBlank { "1 Tablet" },
            time = time.ifBlank { "08:00 AM" },
            frequency = frequency.ifBlank { "Daily" },
            startDate = startDate.ifBlank { "Today" },
            endDate = endDate.ifBlank { "Ongoing" },
            isTaken = false
        )
        _reminders.update { listOf(newReminder) + it }
    }

    fun updateReminder(
        id: String,
        name: String,
        dosage: String,
        time: String,
        frequency: String
    ) {
        _reminders.update { list ->
            list.map {
                if (it.id == id) {
                    it.copy(
                        medicineName = name,
                        dosage = dosage,
                        time = time,
                        frequency = frequency
                    )
                } else it
            }
        }
    }

    fun toggleReminderTaken(id: String) {
        _reminders.update { list ->
            list.map {
                if (it.id == id) it.copy(isTaken = !it.isTaken) else it
            }
        }
    }

    fun deleteReminder(id: String) {
        _reminders.update { list -> list.filterNot { it.id == id } }
    }

    // ==========================================
    // MODULE 4: MEDICAL RECORDS CRUD
    // ==========================================

    fun addMedicalRecord(
        title: String,
        recordType: String,
        doctorOrClinic: String,
        date: String,
        description: String
    ) {
        val newRecord = MedicalRecord(
            id = "REC-${System.currentTimeMillis() % 10000}",
            title = title,
            recordType = recordType,
            doctorOrClinic = doctorOrClinic.ifBlank { "City General Hospital" },
            date = date.ifBlank { SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date()) },
            description = description
        )
        _medicalRecords.update { listOf(newRecord) + it }
    }

    fun updateMedicalRecord(
        id: String,
        title: String,
        recordType: String,
        doctorOrClinic: String,
        description: String
    ) {
        _medicalRecords.update { list ->
            list.map {
                if (it.id == id) {
                    it.copy(
                        title = title,
                        recordType = recordType,
                        doctorOrClinic = doctorOrClinic,
                        description = description
                    )
                } else it
            }
        }
    }

    fun deleteMedicalRecord(id: String) {
        _medicalRecords.update { list -> list.filterNot { it.id == id } }
    }

    fun logWater(delta: Int) {
        _healthStatus.update { current ->
            val newGlasses = (current.waterGlasses + delta).coerceIn(0, 15)
            current.copy(waterGlasses = newGlasses)
        }
    }

    // ==========================================
    // MODULE 5: EMERGENCY SOS & CONTACTS CRUD
    // ==========================================

    fun triggerSos() {
        _isSosActive.value = true
    }

    fun dismissSos() {
        _isSosActive.value = false
    }

    fun addEmergencyContact(name: String, relationship: String, phone: String, isPrimary: Boolean = false) {
        val newContact = EmergencyContact(
            id = "CNT-${System.currentTimeMillis() % 10000}",
            name = name,
            relationship = relationship,
            phone = phone,
            isPrimary = isPrimary
        )
        _emergencyContacts.update { it + newContact }
    }

    fun deleteEmergencyContact(id: String) {
        _emergencyContacts.update { it.filterNot { item -> item.id == id } }
    }

    // ==========================================
    // INITIAL SAMPLE DATA
    // ==========================================

    private fun getInitialDoctors(): List<Doctor> {
        return listOf(
            Doctor(
                id = "doc_1",
                name = "Dr. Ruvan Ekanayake",
                specialization = "Cardiologist",
                rating = 4.9,
                reviewCount = 248,
                experience = "25+ Years Experience",
                consultationFee = "Rs. 2,500",
                availableDays = "Mon - Fri (04:00 PM - 08:00 PM)",
                about = "Dr. Ruvan Ekanayake is a distinguished Senior Consultant Cardiologist at National Hospital of Sri Lanka (NHSL) & Asiri Surgical Hospital, specializing in coronary care, heart failure management, and preventive cardiology.",
                isAvailable = true,
                imageRes = "doctor_sarah"
            ),
            Doctor(
                id = "doc_2",
                name = "Dr. B. J. C. Perera",
                specialization = "Pediatrician",
                rating = 4.9,
                reviewCount = 312,
                experience = "30+ Years Experience",
                consultationFee = "Rs. 2,200",
                availableDays = "Tue - Sat (09:00 AM - 01:00 PM)",
                about = "Dr. B. J. C. Perera is a renowned Senior Consultant Paediatrician at Lady Ridgeway Hospital for Children (LRH) & Nawaloka Hospital, specializing in infant health, child development, and vaccinations.",
                isAvailable = true,
                imageRes = "doctor_alex"
            ),
            Doctor(
                id = "doc_3",
                name = "Dr. Dilani Ranasinghe",
                specialization = "Dentist",
                rating = 4.8,
                reviewCount = 185,
                experience = "15+ Years Experience",
                consultationFee = "Rs. 1,800",
                availableDays = "Mon, Wed, Sat (09:00 AM - 05:00 PM)",
                about = "Dr. Dilani Ranasinghe is a Consultant Dental Surgeon at Dental Institute Colombo & Lanka Hospitals, providing advanced cosmetic smile design, root canal treatment, and oral surgery.",
                isAvailable = true,
                imageRes = "doctor_sarah"
            ),
            Doctor(
                id = "doc_4",
                name = "Dr. Ruvaiz Haniffa",
                specialization = "General Physician",
                rating = 4.9,
                reviewCount = 270,
                experience = "20+ Years Experience",
                consultationFee = "Rs. 1,500",
                availableDays = "Mon - Sat (08:30 AM - 01:30 PM)",
                about = "Dr. Ruvaiz Haniffa is Head of Department of Family Medicine and Consultant Family Physician practicing at Colombo South Teaching Hospital & Durdans Hospital, focusing on family health and preventive primary care.",
                isAvailable = true,
                imageRes = "doctor_alex"
            ),
            Doctor(
                id = "doc_5",
                name = "Dr. Indira Kahawita",
                specialization = "Dermatologist",
                rating = 4.9,
                reviewCount = 224,
                experience = "18+ Years Experience",
                consultationFee = "Rs. 2,400",
                availableDays = "Wed - Sun (03:00 PM - 07:30 PM)",
                about = "Dr. Indira Kahawita is a leading Consultant Dermatologist at Base Hospital Homagama & Asiri Central Hospital Colombo, an expert in clinical dermatology, acne treatments, and hair/skin therapies.",
                isAvailable = true,
                imageRes = "doctor_sarah"
            ),
            Doctor(
                id = "doc_6",
                name = "Dr. Ananda Wijewickrama",
                specialization = "General Physician",
                rating = 4.9,
                reviewCount = 340,
                experience = "22+ Years Experience",
                consultationFee = "Rs. 2,500",
                availableDays = "Mon, Wed, Fri (05:00 PM - 08:30 PM)",
                about = "Dr. Ananda Wijewickrama is a Senior Consultant Physician at National Institute of Infectious Diseases (IDH) & Sri Jayewardenepura General Hospital, acclaimed nationally for internal medicine and infection management.",
                isAvailable = true,
                imageRes = "doctor_alex"
            )
        )
    }

    private fun getInitialAppointments(): List<Appointment> {
        return listOf(
            Appointment(
                id = "APT-1001",
                doctorId = "doc_1",
                doctorName = "Dr. Ruvan Ekanayake",
                specialization = "Cardiologist",
                patientName = "Kasun Perera",
                patientPhone = "+94 77 123 4567",
                reason = "Routine Cardiovascular Health Checkup & ECG",
                date = "28 August 2026",
                time = "05:00 PM",
                consultationFee = "Rs. 2,500",
                status = "Confirmed"
            ),
            Appointment(
                id = "APT-1002",
                doctorId = "doc_3",
                doctorName = "Dr. Dilani Ranasinghe",
                specialization = "Dentist",
                patientName = "Kasun Perera",
                patientPhone = "+94 77 123 4567",
                reason = "Dental Cleaning & Polishing",
                date = "05 September 2026",
                time = "10:30 AM",
                consultationFee = "Rs. 1,800",
                status = "Pending"
            )
        )
    }

    private fun getInitialMedicines(): List<Medicine> {
        return listOf(
            Medicine(
                id = "med_1",
                name = "Panadol Extra",
                category = "Pain Relief",
                price = 450.0,
                priceFormatted = "Rs. 450",
                inStock = true,
                stockCount = 120,
                description = "Effective fast relief for headaches, body aches, toothaches, and reducing fever.",
                dosageForm = "20 Caplets / Pack"
            ),
            Medicine(
                id = "med_2",
                name = "Vitamin C 1000mg",
                category = "Vitamins",
                price = 680.0,
                priceFormatted = "Rs. 680",
                inStock = true,
                stockCount = 85,
                description = "High-potency antioxidant immunity booster with zinc and rose hips extract.",
                dosageForm = "30 Effervescent Tablets"
            ),
            Medicine(
                id = "med_3",
                name = "Cold & Flu Multi-Action",
                category = "Cold & Flu",
                price = 320.0,
                priceFormatted = "Rs. 320",
                inStock = true,
                stockCount = 60,
                description = "Relieves nasal congestion, sore throat, sneezing, and sinus headache.",
                dosageForm = "10 Capsules / Strip"
            ),
            Medicine(
                id = "med_4",
                name = "First Aid Emergency Kit",
                category = "First Aid",
                price = 1450.0,
                priceFormatted = "Rs. 1,450",
                inStock = true,
                stockCount = 30,
                description = "Complete emergency kit with antiseptic wipes, bandages, surgical tape, and sterile gauze.",
                dosageForm = "1 Medical Box"
            ),
            Medicine(
                id = "med_5",
                name = "Ibuprofen 400mg",
                category = "Pain Relief",
                price = 380.0,
                priceFormatted = "Rs. 380",
                inStock = true,
                stockCount = 74,
                description = "Non-steroidal anti-inflammatory drug for muscle aches, joint pains, and swelling.",
                dosageForm = "10 Tablets / Strip"
            ),
            Medicine(
                id = "med_6",
                name = "Cetirizine 10mg",
                category = "Personal Care",
                price = 240.0,
                priceFormatted = "Rs. 240",
                inStock = true,
                stockCount = 95,
                description = "Non-drowsy 24-hour antihistamine for seasonal allergies, pollen, and skin hives.",
                dosageForm = "10 Tablets / Strip"
            )
        )
    }

    private fun getInitialOrders(): List<MedicineOrder> {
        return listOf(
            MedicineOrder(
                id = "ORD-84920",
                customerName = "Kasun Perera",
                phone = "+94 77 123 4567",
                address = "No. 45/2, Galle Road, Colombo 03",
                itemsSummary = "Panadol Extra (x1), Vitamin C 1000mg (x1)",
                itemCount = 2,
                subtotal = 1130.0,
                deliveryFee = 50.0,
                total = 1180.0,
                paymentMethod = "Cash on Delivery",
                status = "Preparing",
                date = "26 Aug 2026, 04:30 PM"
            )
        )
    }

    private fun getInitialReminders(): List<MedicationReminder> {
        return listOf(
            MedicationReminder(
                id = "REM-01",
                medicineName = "Vitamin C",
                dosage = "1 Tablet",
                time = "08:00 AM",
                frequency = "Daily",
                startDate = "20 Aug 2026",
                endDate = "Ongoing",
                isTaken = true
            ),
            MedicationReminder(
                id = "REM-02",
                medicineName = "Omega-3 Fish Oil",
                dosage = "1 Capsule",
                time = "01:00 PM",
                frequency = "Daily",
                startDate = "20 Aug 2026",
                endDate = "Ongoing",
                isTaken = false
            ),
            MedicationReminder(
                id = "REM-03",
                medicineName = "Calcium + D3",
                dosage = "1 Tablet",
                time = "09:00 PM",
                frequency = "Daily",
                startDate = "15 Aug 2026",
                endDate = "15 Sep 2026",
                isTaken = false
            )
        )
    }

    private fun getInitialRecords(): List<MedicalRecord> {
        return listOf(
            MedicalRecord(
                id = "REC-01",
                title = "Prescription - Cardiovascular Care",
                recordType = "Prescription",
                doctorOrClinic = "Dr. Ruvan Ekanayake, Asiri Surgical Hospital",
                date = "22 August 2026",
                description = "Daily blood pressure maintenance, low sodium dietary plan, and routine 30-min morning walk recommendations."
            ),
            MedicalRecord(
                id = "REC-02",
                title = "Complete Blood Count (CBC) & Lipid Profile",
                recordType = "Lab Reports",
                doctorOrClinic = "Asiri Laboratories / Nawaloka Diagnostics Colombo",
                date = "15 August 2026",
                description = "Hemoglobin 14.5 g/dL, Platelets 280,000 /mcL, Total Cholesterol within healthy optimal limits."
            ),
            MedicalRecord(
                id = "REC-03",
                title = "Chest X-Ray (PA View)",
                recordType = "X-Ray Reports",
                doctorOrClinic = "National Hospital of Sri Lanka (NHSL), Colombo",
                date = "10 July 2026",
                description = "Lungs clear, normal cardiac silhouette, no active infiltrates observed."
            ),
            MedicalRecord(
                id = "REC-04",
                title = "COVID-19 Booster & Tetanus Shot",
                recordType = "Vaccination",
                doctorOrClinic = "MOH Office / Colombo Municipal Health Department",
                date = "04 April 2026",
                description = "Annual booster dose administered successfully. Next tetanus due in 2031."
            ),
            MedicalRecord(
                id = "REC-05",
                title = "Annual Health Assessment Summary",
                recordType = "Health Summary",
                doctorOrClinic = "Sri Jayewardenepura General Hospital",
                date = "12 January 2026",
                description = "Overall physical fitness: Good. Recommended maintaining 8 glasses of water daily and hydration during sports."
            )
        )
    }

    private fun getInitialEmergencyContacts(): List<EmergencyContact> {
        return listOf(
            EmergencyContact(
                id = "CNT-01",
                name = "Kumari Perera",
                relationship = "Mother (Amma)",
                phone = "+94 71 987 6543",
                isPrimary = true
            ),
            EmergencyContact(
                id = "CNT-02",
                name = "Sunil Perera",
                relationship = "Father (Thaththa)",
                phone = "+94 77 876 5432",
                isPrimary = false
            ),
            EmergencyContact(
                id = "CNT-03",
                name = "1990 Suwa Seriya Ambulance",
                relationship = "Emergency Hotline",
                phone = "1990",
                isPrimary = false
            ),
            EmergencyContact(
                id = "CNT-04",
                name = "National Hospital Colombo Emergency Unit",
                relationship = "Hospital ER",
                phone = "011 269 1111",
                isPrimary = false
            )
        )
    }

    companion object {
        val instance = LifeCareRepository()
    }
}
