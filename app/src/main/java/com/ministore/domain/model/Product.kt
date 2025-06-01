package com.ministore.domain.model

import java.math.BigDecimal // Preserved for calculateStockValue

// Potentially remove java.time.LocalDateTime if not used elsewhere,
// as expirationDate and createdAt/updatedAt are Long timestamps.

data class ProductLocation(
    val aisle: String = "",
    val shelf: String = "",
    val level: String = ""
)

/**
 * Domain model representing a product in the MiniStore application.
 * This class contains all the business logic and validation rules for products.
 */
data class Product(
    val id: String = "",
    val name: String = "",
    val barcode: String = "", // Kept non-nullable as per original
    val category: String = "",
    val purchasePrice: Double = 0.0,
    val sellingPrice: Double = 0.0, // Renamed from 'price'
    val quantity: Int = 0,
    val providerId: String = "",
    val location: ProductLocation? = null,
    val minimumStock: Int = DEFAULT_MIN_STOCK, // Uses companion object constant
    val expirationDate: Long? = null, // Timestamp
    val imageUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    init {
        require(barcode.isNotBlank()) { "Barcode cannot be blank" }
        require(name.isNotBlank()) { "Product name cannot be blank" }
        require(category.isNotBlank()) { "Category cannot be blank" }
        // Updated validation for sellingPrice (formerly price)
        // The original check was `price >= BigDecimal.ZERO`. For Double, it's `price >= 0.0`.
        require(sellingPrice >= 0.0) { "Selling price cannot be negative" }
        require(purchasePrice >= 0.0) { "Purchase price cannot be negative" }
        require(quantity >= 0) { "Quantity cannot be negative" }
        // Consider validation for minimumStock if needed, e.g., require(minimumStock >= 0)
        // However, it defaults to DEFAULT_MIN_STOCK which is >= 0.
    }

    companion object {
        const val DEFAULT_MIN_STOCK = 5 // This is now used as default for minimumStock field
        val VALID_CATEGORIES = setOf(
            "Food",
            "Beverages",
            "Household",
            "Personal Care",
            "Electronics",
            "Other"
        )
    }

    /**
     * Checks if the product needs restocking
     */
    fun needsRestocking(): Boolean = quantity <= minimumStock // Updated to use the field

    /**
     * Calculates the total value of the product's current stock based on selling price
     */
    fun calculateStockValue(): BigDecimal =
        BigDecimal.valueOf(sellingPrice).multiply(BigDecimal.valueOf(quantity.toLong())) // Use sellingPrice and ensure quantity is Long for BigDecimal if it can be large
}