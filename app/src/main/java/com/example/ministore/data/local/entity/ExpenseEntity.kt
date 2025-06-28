package com.example.ministore.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.ministore.data.local.converter.DateConverter
import com.example.ministore.data.local.converter.ExpenseCategoryConverter
import com.example.ministore.data.local.converter.PaymentMethodConverter
import com.example.ministore.data.local.converter.RecurringPeriodConverter
import com.example.ministore.domain.model.PaymentMethod
import com.example.ministore.domain.model.RecurringPeriod
import com.example.minstore.domain.models.ExpenseCategory
import java.util.Date

@Entity(tableName = "expenses")
@TypeConverters(
    DateConverter::class,
    ExpenseCategoryConverter::class,
    PaymentMethodConverter::class,
    RecurringPeriodConverter::class
)
data class ExpenseEntity(
    @PrimaryKey
    val id: String,
    val category: ExpenseCategory,
    val amount: Double,
    val description: String,
    val date: Date,
    val paymentMethod: PaymentMethod,
    val receiptUrl: String?,
    val notes: String?,
    val isRecurring: Boolean,
    val recurringPeriod: RecurringPeriod?
) 