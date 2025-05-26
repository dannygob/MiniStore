package com.example.minstore.domain.models

import java.util.Date

data class Expense(
    val id: String = "",
    val description: String,
    val amount: Double,
    val category: ExpenseCategory,
    val date: Date,
    val paymentMethod: PaymentMethod,
    val notes: String?,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

enum class ExpenseCategory {
    RENT,
    UTILITIES,
    SALARIES,
    MAINTENANCE,
    SUPPLIES,
    MARKETING,
    TAXES,
    OTHER
} 