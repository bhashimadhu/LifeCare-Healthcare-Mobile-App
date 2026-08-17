package com.example.ui.pharmacy

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.models.CartItem
import com.example.models.Medicine
import com.example.models.MedicineOrder
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
 * Student 3: Pharmacy Screen
 */
@Composable
fun PharmacyScreen(
    medicines: List<Medicine>,
    cart: List<CartItem>,
    orders: List<MedicineOrder>,
    onAddToCart: (Medicine, Int) -> Unit,
    onOpenCart: () -> Unit,
    onOpenOrders: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var viewingMedicine by remember { mutableStateOf<Medicine?>(null) }
    var orderPlacedToast by remember { mutableStateOf(false) }

    val categories = listOf("All", "Pain Relief", "Cold & Flu", "Vitamins", "First Aid", "Personal Care")

    val filteredMedicines = medicines.filter { med ->
        val matchesCategory = if (selectedCategory == "All") true else med.category.equals(selectedCategory, ignoreCase = true)
        val matchesSearch = med.name.contains(searchQuery, ignoreCase = true) || med.description.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    val totalCartCount = cart.sumOf { it.quantity }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LifeCareBackground)
            .padding(16.dp)
    ) {
        // Header with Cart and Orders shortcuts
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Medicines",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = LifeCareTextPrimary
                )
                Text(
                    text = "Authentic pharmacy essentials delivered",
                    fontSize = 13.sp,
                    color = LifeCareTextSecondary
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    onClick = onOpenOrders,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(40.dp).testTag("open_orders_button")
                ) {
                    Text("My Orders", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = LifeCareTealDark)
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onOpenCart,
                    modifier = Modifier.testTag("open_cart_button")
                ) {
                    BadgedBox(
                        badge = {
                            if (totalCartCount > 0) {
                                Badge(containerColor = LifeCareEmergency) {
                                    Text("$totalCartCount", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = LifeCareTealLight,
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.ShoppingCart,
                                    contentDescription = "Cart",
                                    tint = LifeCareTealDark,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Search Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search medicines...", color = LifeCareTextMuted) },
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
            modifier = Modifier.fillMaxWidth().testTag("medicine_search_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Categories Chips
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

        // Medicines Grid/List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredMedicines, key = { it.id }) { medicine ->
                MedicineCardItem(
                    medicine = medicine,
                    onViewDetails = { viewingMedicine = medicine },
                    onAddToCart = { onAddToCart(medicine, 1) }
                )
            }
        }
    }

    // Medicine Details Modal Dialog
    if (viewingMedicine != null) {
        val med = viewingMedicine!!
        var quantity by remember { mutableStateOf(1) }

        AlertDialog(
            onDismissRequest = { viewingMedicine = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = LifeCareTealLight,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Medication, contentDescription = null, tint = LifeCareTealDark)
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(med.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(med.category, color = LifeCareTealDark, fontSize = 13.sp)
                    }
                }
            },
            text = {
                Column {
                    Text(
                        text = med.description,
                        fontSize = 14.sp,
                        color = LifeCareTextSecondary,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Packaging: ${med.dosageForm}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = LifeCareTextPrimary
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = med.priceFormatted,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = LifeCareTealDark
                        )

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (med.inStock) Color(0xFFE8F5E9) else Color(0xFFFFECEC)
                        ) {
                            Text(
                                text = if (med.inStock) "In Stock (${med.stockCount})" else "Out of Stock",
                                color = if (med.inStock) Color(0xFF2E7D32) else LifeCareEmergency,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = LifeCareBorder)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Quantity Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Select Quantity", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(
                                onClick = { if (quantity > 1) quantity-- },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Surface(shape = CircleShape, color = LifeCareBackground, modifier = Modifier.fillMaxSize()) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
                                    }
                                }
                            }

                            Text("$quantity", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                            IconButton(
                                onClick = { if (quantity < 10) quantity++ },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Surface(shape = CircleShape, color = LifeCareTealLight, modifier = Modifier.fillMaxSize()) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.Add, contentDescription = "Increase", tint = LifeCareTealDark, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAddToCart(med, quantity)
                        viewingMedicine = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Add to Cart (Rs. ${(med.price * quantity).toInt()})", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewingMedicine = null }) {
                    Text("Close", color = LifeCareTextSecondary)
                }
            }
        )
    }
}

@Composable
fun MedicineCardItem(
    medicine: Medicine,
    onViewDetails: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LifeCareSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().clickable { onViewDetails() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = LifeCareTealLight,
                modifier = Modifier.size(60.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Medication,
                        contentDescription = medicine.name,
                        tint = LifeCareTealDark,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = medicine.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = LifeCareTextPrimary
                )
                Text(
                    text = "${medicine.category} • ${medicine.dosageForm}",
                    fontSize = 12.sp,
                    color = LifeCareTextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = medicine.priceFormatted,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = LifeCareTealDark
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (medicine.inStock) "In Stock" else "Out of Stock",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (medicine.inStock) Color(0xFF2E7D32) else LifeCareEmergency
                    )
                }
            }

            // Quick Add to Cart Button
            Button(
                onClick = onAddToCart,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.testTag("add_to_cart_${medicine.id}")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

/**
 * Student 3: Shopping Cart Screen
 */
@Composable
fun CartScreen(
    cart: List<CartItem>,
    currentUser: UserProfile,
    onUpdateQuantity: (String, Int) -> Unit,
    onRemoveItem: (String) -> Unit,
    onPlaceOrder: (String, String, String) -> MedicineOrder?,
    onBack: () -> Unit
) {
    var showCheckoutDialog by remember { mutableStateOf(false) }
    var placedOrder by remember { mutableStateOf<MedicineOrder?>(null) }

    val subtotal = cart.sumOf { it.medicine.price * it.quantity }
    val deliveryFee = if (cart.isNotEmpty()) 50.0 else 0.0
    val total = subtotal + deliveryFee

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LifeCareBackground)
            .padding(16.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = LifeCareTextPrimary)
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Shopping Cart (${cart.sumOf { it.quantity }})",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = LifeCareTextPrimary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (cart.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.LocalMall,
                        contentDescription = null,
                        tint = LifeCareTextMuted,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Your cart is empty", fontWeight = FontWeight.Bold, color = LifeCareTextSecondary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal)
                    ) {
                        Text("Browse Medicines")
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(cart, key = { it.medicine.id }) { item ->
                        CartItemRow(
                            item = item,
                            onIncrease = { onUpdateQuantity(item.medicine.id, 1) },
                            onDecrease = { onUpdateQuantity(item.medicine.id, -1) },
                            onRemove = { onRemoveItem(item.medicine.id) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Price Breakdown Card
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = LifeCareSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Subtotal", color = LifeCareTextSecondary, fontSize = 14.sp)
                            Text("Rs. ${subtotal.toInt()}", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Delivery Fee", color = LifeCareTextSecondary, fontSize = 14.sp)
                            Text("Rs. ${deliveryFee.toInt()}", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = LifeCareBorder)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total Amount", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(
                                "Rs. ${total.toInt()}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = LifeCareTealDark
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = { showCheckoutDialog = true },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("checkout_order_button")
                ) {
                    Text("Place Order (Rs. ${total.toInt()})", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    // Checkout Order Form Dialog
    if (showCheckoutDialog) {
        var customerName by remember { mutableStateOf(currentUser.fullName) }
        var customerPhone by remember { mutableStateOf(currentUser.phone) }
        var deliveryAddress by remember { mutableStateOf("University Student Dormitory, Block B, Room 304") }

        AlertDialog(
            onDismissRequest = { showCheckoutDialog = false },
            title = { Text("Complete Medicine Order", fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(
                        "Please verify your delivery details below:",
                        fontSize = 13.sp,
                        color = LifeCareTextSecondary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = customerName,
                        onValueChange = { customerName = it },
                        label = { Text("Customer Name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = LifeCareTeal) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = customerPhone,
                        onValueChange = { customerPhone = it },
                        label = { Text("Phone Number") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = LifeCareTeal) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = deliveryAddress,
                        onValueChange = { deliveryAddress = it },
                        label = { Text("Delivery Address") },
                        leadingIcon = { Icon(Icons.Default.LocalShipping, contentDescription = null, tint = LifeCareTeal) },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = LifeCarePeachLight,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Payment, contentDescription = null, tint = Color(0xFFC0553A))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Payment Method", fontSize = 11.sp, color = LifeCareTextSecondary)
                                Text("Cash on Delivery (COD)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFFC0553A))
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val order = onPlaceOrder(customerName, customerPhone, deliveryAddress)
                        showCheckoutDialog = false
                        placedOrder = order
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal)
                ) {
                    Text("Confirm Order", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCheckoutDialog = false }) {
                    Text("Cancel", color = LifeCareTextSecondary)
                }
            }
        )
    }

    // Success confirmation dialog
    if (placedOrder != null) {
        val ord = placedOrder!!
        AlertDialog(
            onDismissRequest = {
                placedOrder = null
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
                Text("Order Placed Successfully", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "Order #${ord.id} totaling Rs. ${ord.total.toInt()} has been confirmed and saved to Firestore. Delivery will arrive at your address with Cash on Delivery.",
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        placedOrder = null
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LifeCareTeal),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Back to Pharmacy")
                }
            }
        )
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
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
                shape = RoundedCornerShape(10.dp),
                color = LifeCareTealLight,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Medication, contentDescription = null, tint = LifeCareTealDark)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(item.medicine.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(item.medicine.priceFormatted, color = LifeCareTealDark, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Text("Subtotal: Rs. ${(item.medicine.price * item.quantity).toInt()}", fontSize = 11.sp, color = LifeCareTextSecondary)
            }

            // Counter controls
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDecrease, modifier = Modifier.size(30.dp)) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
                }

                Text(
                    text = "${item.quantity}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )

                IconButton(onClick = onIncrease, modifier = Modifier.size(30.dp)) {
                    Icon(Icons.Default.Add, contentDescription = "Increase", tint = LifeCareTealDark, modifier = Modifier.size(16.dp))
                }

                IconButton(onClick = onRemove, modifier = Modifier.size(30.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove", tint = LifeCareEmergency, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

/**
 * Student 3: My Orders Screen (with full CRUD support)
 */
@Composable
fun MyOrdersScreen(
    orders: List<MedicineOrder>,
    onCancelOrder: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LifeCareBackground)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = LifeCareTextPrimary)
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "My Medicine Orders",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = LifeCareTextPrimary
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.ShoppingBag,
                        contentDescription = null,
                        tint = LifeCareTextMuted,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("No past orders found", fontWeight = FontWeight.Bold, color = LifeCareTextSecondary)
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(orders, key = { it.id }) { order ->
                    OrderCardItem(order = order, onCancel = { onCancelOrder(order.id) })
                }
            }
        }
    }
}

@Composable
fun OrderCardItem(
    order: MedicineOrder,
    onCancel: () -> Unit
) {
    val statusBg = when (order.status) {
        "Delivered" -> Color(0xFFE8F5E9)
        "Preparing" -> LifeCareTealLight
        "Pending" -> Color(0xFFFFF3E0)
        else -> Color(0xFFFFECEC)
    }

    val statusColor = when (order.status) {
        "Delivered" -> Color(0xFF2E7D32)
        "Preparing" -> LifeCareTealDark
        "Pending" -> Color(0xFFF57C00)
        else -> LifeCareEmergency
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
                Column {
                    Text("Order #${order.id}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(order.date, fontSize = 12.sp, color = LifeCareTextSecondary)
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = statusBg
                ) {
                    Text(
                        text = order.status,
                        color = statusColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text("Items: ${order.itemsSummary}", fontSize = 13.sp, color = LifeCareTextPrimary)
            Text("Delivery to: ${order.address}", fontSize = 12.sp, color = LifeCareTextSecondary, modifier = Modifier.padding(top = 2.dp))

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = LifeCareBorder)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Total Amount (COD)", fontSize = 11.sp, color = LifeCareTextSecondary)
                    Text("Rs. ${order.total.toInt()}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = LifeCareTealDark)
                }

                if (order.status == "Preparing" || order.status == "Pending") {
                    TextButton(onClick = onCancel) {
                        Text("Cancel Order", color = LifeCareEmergency, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}
