package com.ministore.di

import com.ministore.data.repository.AuthRepositoryImpl
import com.ministore.data.repository.ProductRepositoryImpl
import com.ministore.data.repository.SaleRepositoryImpl
import com.ministore.domain.repository.AuthRepository
import com.ministore.domain.repository.ProductRepository
import com.ministore.domain.repository.SaleRepository
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
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

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