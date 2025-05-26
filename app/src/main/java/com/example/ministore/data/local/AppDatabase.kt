package com.example.ministore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ministore.data.local.converter.DateConverter
import com.example.ministore.data.local.dao.*
import com.example.ministore.data.local.entity.*

@Database(
    entities = [
        ProductEntity::class,
        SaleEntity::class,
        PurchaseEntity::class,
        ClientEntity::class,
        ProviderEntity::class,
        OrderEntity::class,
        ExpenseEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun saleDao(): SaleDao
    abstract fun purchaseDao(): PurchaseDao
    abstract fun clientDao(): ClientDao
    abstract fun providerDao(): ProviderDao
    abstract fun orderDao(): OrderDao
    abstract fun expenseDao(): ExpenseDao
} 