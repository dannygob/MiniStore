package com.example.ministore.data.domain.repository

import com.example.ministore.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProducts(): Flow<List<Product>>
    suspend fun getProductById(id: Long): Product?
    fun getLowStockProducts(): Flow<List<Product>>
    fun searchProducts(query: String): Flow<List<Product>>
    suspend fun insertProduct(product: Product): Long
    suspend fun insertProducts(products: List<Product>)
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(id: Long)
    suspend fun updateStock(id: Long, quantity: Int)
}