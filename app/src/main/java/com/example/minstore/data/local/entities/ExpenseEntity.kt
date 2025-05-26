package com.example.minstore.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.minstore.domain.models.ExpenseCategory
import com.example.minstore.domain.models.PaymentMethod
import java.util.Date

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey
    val id: String,
    val description: String,
    val amount: Double,
    val category: ExpenseCategory,
    val date: Date,
    val paymentMethod: PaymentMethod,
    val notes: String?,
    val createdAt: Date,
    val updatedAt: Date
) 