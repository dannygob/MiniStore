package com.example.ministore.di

import com.example.ministore.data.repository.ClientRepositoryImpl
import com.example.ministore.data.repository.ExpenseRepositoryImpl
import com.example.ministore.data.repository.OrderRepositoryImpl
import com.example.ministore.data.repository.ProductRepositoryImpl
import com.example.ministore.data.repository.ProviderRepositoryImpl
import com.example.ministore.data.repository.PurchaseRepositoryImpl
import com.example.ministore.data.repository.SaleRepositoryImpl
import com.example.ministore.data.domain.repository.ClientRepository
import com.example.ministore.data.domain.repository.ExpenseRepository
import com.example.ministore.data.domain.repository.OrderRepository
import com.example.ministore.data.domain.repository.ProductRepository
import com.example.ministore.data.domain.repository.ProviderRepository
import com.example.ministore.data.domain.repository.PurchaseRepository
import com.example.ministore.domain.repository.SaleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindClientRepository(
        clientRepositoryImpl: ClientRepositoryImpl
    ): ClientRepository

    @Binds
    @Singleton
    abstract fun bindExpenseRepository(
        expenseRepositoryImpl: ExpenseRepositoryImpl
    ): ExpenseRepository

    @Binds
    @Singleton
    abstract fun bindOrderRepository(
        orderRepositoryImpl: OrderRepositoryImpl
    ): OrderRepository

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindProviderRepository(
        providerRepositoryImpl: ProviderRepositoryImpl
    ): ProviderRepository

    @Binds
    @Singleton
    abstract fun bindPurchaseRepository(
        purchaseRepositoryImpl: PurchaseRepositoryImpl
    ): PurchaseRepository

    @Binds
    @Singleton
    abstract fun bindSaleRepository(
        saleRepositoryImpl: SaleRepositoryImpl
    ): SaleRepository
}