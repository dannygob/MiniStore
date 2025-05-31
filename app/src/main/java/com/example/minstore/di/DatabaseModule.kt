package com.example.minstore.di

import android.content.Context
import androidx.room.Room
import com.example.ministore.data.local.dao.ClientDao
import com.example.minstore.data.local.dao.*
import com.example.minstore.data.local.database.MiniStoreDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMiniStoreDatabase(
        @ApplicationContext context: Context
    ): MiniStoreDatabase = Room.databaseBuilder(
        context,
        MiniStoreDatabase::class.java,
        "ministore.db"
    ).build()

    @Provides
    @Singleton
    fun provideProductDao(database: MiniStoreDatabase): ProductDao = database.productDao()

    @Provides
    @Singleton
    fun provideSaleDao(database: MiniStoreDatabase): SaleDao = database.saleDao()

    @Provides
    @Singleton
    fun providePurchaseDao(database: MiniStoreDatabase): PurchaseDao = database.purchaseDao()

    @Provides
    @Singleton
    fun provideClientDao(database: MiniStoreDatabase): ClientDao = database.clientDao()

    @Provides
    @Singleton
    fun provideProviderDao(database: MiniStoreDatabase): ProviderDao = database.providerDao()

    @Provides
    @Singleton
    fun provideOrderDao(database: MiniStoreDatabase): OrderDao = database.orderDao()

    @Provides
    @Singleton
    fun provideExpenseDao(database: MiniStoreDatabase): ExpenseDao = database.expenseDao()
} 