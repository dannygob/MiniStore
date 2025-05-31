package com.example.minstore.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.ministore.data.local.dao.ClientDao
import com.example.minstore.data.local.dao.*
import com.example.minstore.data.local.entities.*
import com.example.minstore.domain.models.*
import java.util.Date

@Database(
    entities = [
        ProductEntity::class,
        SaleEntity::class,
        SaleDetailEntity::class,
        PurchaseEntity::class,
        PurchaseDetailEntity::class,
        ClientEntity::class,
        ProviderEntity::class,
        OrderEntity::class,
        OrderDetailEntity::class,
        ExpenseEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class MiniStoreDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun saleDao(): SaleDao
    abstract fun purchaseDao(): PurchaseDao
    abstract fun clientDao(): ClientDao
    abstract fun providerDao(): ProviderDao
    abstract fun orderDao(): OrderDao
    abstract fun expenseDao(): ExpenseDao
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromPaymentMethod(value: PaymentMethod): String {
        return value.name
    }

    @TypeConverter
    fun toPaymentMethod(value: String): PaymentMethod {
        return PaymentMethod.valueOf(value)
    }

    @TypeConverter
    fun fromSaleStatus(value: SaleStatus): String {
        return value.name
    }

    @TypeConverter
    fun toSaleStatus(value: String): SaleStatus {
        return SaleStatus.valueOf(value)
    }

    @TypeConverter
    fun fromPurchaseStatus(value: PurchaseStatus): String {
        return value.name
    }

    @TypeConverter
    fun toPurchaseStatus(value: String): PurchaseStatus {
        return PurchaseStatus.valueOf(value)
    }

    @TypeConverter
    fun fromOrderStatus(value: OrderStatus): String {
        return value.name
    }

    @TypeConverter
    fun toOrderStatus(value: String): OrderStatus {
        return OrderStatus.valueOf(value)
    }

    @TypeConverter
    fun fromExpenseCategory(value: ExpenseCategory): String {
        return value.name
    }

    @TypeConverter
    fun toExpenseCategory(value: String): ExpenseCategory {
        return ExpenseCategory.valueOf(value)
    }
} 