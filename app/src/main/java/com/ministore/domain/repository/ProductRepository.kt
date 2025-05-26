package com.ministore.domain.repository

import com.ministore.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<List<Product>>
    fun getProduct(id: String): Flow<Product?>
    suspend fun addProduct(product: Product): Result<Unit>
    suspend fun updateProduct(product: Product): Result<Unit>
    suspend fun deleteProduct(id: String): Result<Unit>
    suspend fun updateProductQuantity(id: String, newQuantity: Int): Result<Unit>
} 