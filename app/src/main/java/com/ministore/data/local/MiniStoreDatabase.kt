package com.ministore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ministore.data.local.dao.ProductDao
import com.ministore.data.local.dao.SaleDao
import com.ministore.data.local.entity.ProductEntity
import com.ministore.data.local.entity.SaleEntity

@Database(
    entities = [
        ProductEntity::class,
        SaleEntity::class
    ],
    version = 1
)
abstract class MiniStoreDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun saleDao(): SaleDao
} 