package com.example.ministore.domain.model

import com.example.minstore.domain.models.ExpenseCategory
import java.util.Date
import java.util.UUID

data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val category: ExpenseCategory,
    val amount: Double,
    val description: String,
    val date: Date = Date(),
    val paymentMethod: PaymentMethod,
    val receiptUrl: String? = null,
    val notes: String? = null,
    val isRecurring: Boolean = false,
    val recurringPeriod: RecurringPeriod? = null
)

enum class RecurringPeriod {
    DAILY, WEEKLY, MONTHLY, YEARLY
} 