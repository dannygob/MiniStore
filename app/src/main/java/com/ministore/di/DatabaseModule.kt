package com.ministore.di

import android.content.Context
import androidx.room.Room
import com.ministore.data.local.MiniStoreDatabase
import com.ministore.data.local.dao.ProductDao
import com.ministore.data.local.dao.SaleDao
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
} 