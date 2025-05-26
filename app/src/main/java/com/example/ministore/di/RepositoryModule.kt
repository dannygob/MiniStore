package com.example.ministore.di

import com.example.ministore.data.repository.ProductRepositoryImpl
import com.example.ministore.data.repository.SaleRepositoryImpl
import com.example.ministore.domain.repository.ProductRepository
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
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindSaleRepository(
        saleRepositoryImpl: SaleRepositoryImpl
    ): SaleRepository
} 