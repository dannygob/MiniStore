package com.example.ministore.data.mapper

import com.example.ministore.data.local.entity.ExpenseEntity
import com.example.ministore.domain.model.Expense

fun ExpenseEntity.toDomain(): Expense {
    return Expense(
        id = id,
        category = category,
        amount = amount,
        description = description,
        date = date,
        paymentMethod = paymentMethod,
        receiptUrl = receiptUrl,
        notes = notes,
        isRecurring = isRecurring,
        recurringPeriod = recurringPeriod
    )
}

fun Expense.toEntity(): ExpenseEntity {
    return ExpenseEntity(
        id = id,
        category = category,
        amount = amount,
        description = description,
        date = date,
        paymentMethod = paymentMethod,
        receiptUrl = receiptUrl,
        notes = notes,
        isRecurring = isRecurring,
        recurringPeriod = recurringPeriod
    )
}