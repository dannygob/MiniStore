package com.example.ministore.di

import android.content.Context
import androidx.room.Room
import androidx.work.ListenableWorker
import com.example.ministore.data.local.AppDatabase
import com.example.ministore.data.local.SyncProductWorker
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DatabaseModule {

    @Binds
    @IntoMap
    @WorkerKey(SyncProductWorker::class)
    abstract fun bindSyncProductWorker(
        factory: SyncProductWorker.Factory,
    ): dagger.assisted.AssistedFactory<out ListenableWorker>

    companion object {
        @Provides
        @Singleton
        fun provideAppDatabase(
            @ApplicationContext context: Context,
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
}
