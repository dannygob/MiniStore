package com.example.ministore.di

import android.content.Context
import androidx.room.Room
import com.example.ministore.data.local.AppDatabase
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
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ministore.db"
        ).build()
    }

    @Provides
    fun provideProductDao(database: AppDatabase) = database.productDao()

    @Provides
    fun provideSaleDao(database: AppDatabase) = database.saleDao()

    @Provides
    fun providePurchaseDao(database: AppDatabase) = database.purchaseDao()

    @Provides
    fun provideClientDao(database: AppDatabase) = database.clientDao()

    @Provides
    fun provideProviderDao(database: AppDatabase) = database.providerDao()

    @Provides
    fun provideOrderDao(database: AppDatabase) = database.orderDao()

    @Provides
    fun provideExpenseDao(database: AppDatabase) = database.expenseDao()
} 