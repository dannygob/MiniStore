package com.ministore.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Domain model representing a product in the MiniStore application.
 * This class contains all the business logic and validation rules for products.
 */
data class Product(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val quantity: Int = 0,
    val barcode: String = "",
    val category: String = "",
    val createdAt: Long = System.currentTimeMillis()
) {
    init {
        require(barcode.isNotBlank()) { "Barcode cannot be blank" }
        require(name.isNotBlank()) { "Product name cannot be blank" }
        require(category.isNotBlank()) { "Category cannot be blank" }
        require(price >= BigDecimal.ZERO) { "Price cannot be negative" }
        require(quantity >= 0) { "Quantity cannot be negative" }
    }

    companion object {
        const val DEFAULT_MIN_STOCK = 5
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
    fun needsRestocking(): Boolean = quantity <= DEFAULT_MIN_STOCK

    /**
     * Calculates the total value of the product's current stock
     */
    fun calculateStockValue(): BigDecimal =
        BigDecimal.valueOf(price).multiply(BigDecimal.valueOf(quantity))
} 