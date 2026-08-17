package com.example.models

/**
 * Student 3: Pharmacy & Medicine Model
 */
data class Medicine(
    val id: String = "",
    val name: String = "",
    val category: String = "General",
    val price: Double = 0.0,
    val priceFormatted: String = "Rs. 0",
    val inStock: Boolean = true,
    val stockCount: Int = 45,
    val description: String = "",
    val dosageForm: String = "10 Tablets / Strip"
)

data class CartItem(
    val medicine: Medicine,
    var quantity: Int = 1
)

data class MedicineOrder(
    val id: String = "",
    val customerName: String = "",
    val phone: String = "",
    val address: String = "",
    val itemsSummary: String = "",
    val itemCount: Int = 1,
    val subtotal: Double = 0.0,
    val deliveryFee: Double = 50.0,
    val total: Double = 0.0,
    val paymentMethod: String = "Cash on Delivery",
    val status: String = "Preparing", // Pending, Preparing, Delivered, Cancelled
    val date: String = ""
)
