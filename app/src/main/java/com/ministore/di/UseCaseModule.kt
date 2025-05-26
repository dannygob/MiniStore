package com.ministore.di

import com.ministore.domain.repository.ProductRepository
import com.ministore.domain.usecase.product.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideProductUseCases(
        repository: ProductRepository
    ): ProductUseCases {
        return ProductUseCases(
            getProducts = GetProducts(repository),
            getProduct = GetProduct(repository),
            searchProducts = SearchProducts(repository),
            addProduct = AddProduct(repository),
            updateProduct = UpdateProduct(repository),
            deleteProduct = DeleteProduct(repository),
            updateStock = UpdateStock(repository),
            getLowStockProducts = GetLowStockProducts(repository),
            lookupProductByBarcode = LookupProductByBarcode(repository)
        )
    }
} 