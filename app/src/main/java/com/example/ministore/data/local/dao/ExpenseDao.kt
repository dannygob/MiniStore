package com.example.ministore.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ministore.data.local.entity.ExpenseEntity
import com.example.minstore.domain.models.ExpenseCategory
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses WHERE id = :id")
    fun getExpenseById(id: String): Flow<ExpenseEntity?>

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE category = :category ORDER BY date DESC")
    fun getExpensesByCategory(category: ExpenseCategory): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getExpensesByDateRange(startDate: Date, endDate: Date): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE isRecurring = 1 ORDER BY date DESC")
    fun getRecurringExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT SUM(amount) FROM expenses WHERE date BETWEEN :startDate AND :endDate")
    fun getTotalExpensesByDateRange(startDate: Date, endDate: Date): Flow<Double?>

    @Query("SELECT SUM(amount) FROM expenses WHERE category = :category AND date BETWEEN :startDate AND :endDate")
    fun getTotalExpensesByCategoryAndDateRange(
        category: ExpenseCategory,
        startDate: Date,
        endDate: Date
    ): Flow<Double?>
} 