package com.example.ministore.data.repository

import com.example.ministore.data.local.dao.ExpenseDao
import com.example.ministore.data.mapper.toDomain
import com.example.ministore.data.mapper.toEntity
import com.example.ministore.data.domain.repository.ExpenseRepository
import com.example.ministore.domain.model.Expense
import com.example.minstore.domain.models.ExpenseCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

class ExpenseRepositoryImpl(private val expenseDao: ExpenseDao) : ExpenseRepository {
    override suspend fun insertExpense(expense: Expense) {
        expenseDao.insertExpense(expense.toEntity())
    }

    override suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpense(expense.toEntity())
    }

    override suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense.toEntity())
    }

    override fun getExpenseById(id: String): Flow<Expense?> {
        return expenseDao.getExpenseById(id).map { it?.toDomain() }
    }

    override fun getAllExpenses(): Flow<List<Expense>> {
        return expenseDao.getAllExpenses().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getExpensesByCategory(category: ExpenseCategory): Flow<List<Expense>> {
        return expenseDao.getExpensesByCategory(category).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getExpensesByDateRange(startDate: Date, endDate: Date): Flow<List<Expense>> {
        return expenseDao.getExpensesByDateRange(startDate, endDate).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getRecurringExpenses(): Flow<List<Expense>> {
        return expenseDao.getRecurringExpenses().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTotalExpensesByDateRange(startDate: Date, endDate: Date): Flow<Double?> {
        return expenseDao.getTotalExpensesByDateRange(startDate, endDate)
    }

    override fun getTotalExpensesByCategoryAndDateRange(
        category: ExpenseCategory,
        startDate: Date,
        endDate: Date
    ): Flow<Double?> {
        return expenseDao.getTotalExpensesByCategoryAndDateRange(category, startDate, endDate)
    }
}