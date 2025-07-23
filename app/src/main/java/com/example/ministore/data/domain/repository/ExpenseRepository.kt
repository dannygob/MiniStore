package com.example.ministore.data.domain.repository

import com.example.ministore.domain.model.Expense
import com.example.minstore.domain.models.ExpenseCategory
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface ExpenseRepository {
    suspend fun insertExpense(expense: Expense)
    suspend fun updateExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)
    fun getExpenseById(id: String): Flow<Expense?>
    fun getAllExpenses(): Flow<List<Expense>>
    fun getExpensesByCategory(category: ExpenseCategory): Flow<List<Expense>>
    fun getExpensesByDateRange(startDate: Date, endDate: Date): Flow<List<Expense>>
    fun getRecurringExpenses(): Flow<List<Expense>>
    fun getTotalExpensesByDateRange(startDate: Date, endDate: Date): Flow<Double?>
    fun getTotalExpensesByCategoryAndDateRange(
        category: ExpenseCategory,
        startDate: Date,
        endDate: Date
    ): Flow<Double?>
}