package com.example.minstore.domain.repository

import com.example.minstore.domain.models.Product
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface ProductRepository {
    fun getAllProducts(): Flow<List<Product>>
    suspend fun getProductById(id: String): Product?
    suspend fun getProductByBarcode(barcode: String): Product?
    fun getProductsByCategory(category: String): Flow<List<Product>>
    fun getLowStockProducts(): Flow<List<Product>>
    fun getProductsExpiringBefore(date: Date): Flow<List<Product>>
    fun getProductsByProvider(providerId: String): Flow<List<Product>>
    suspend fun insertProduct(product: Product)
    suspend fun insertProducts(products: List<Product>)
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(product: Product)
    suspend fun incrementStock(productId: String, quantity: Int)
    suspend fun decrementStock(productId: String, quantity: Int)
    fun searchProducts(query: String): Flow<List<Product>>

    // Firebase specific operations
    suspend fun syncWithFirestore()
    suspend fun uploadProductImage(productId: String, imageBytes: ByteArray): String
    suspend fun deleteProductImage(imageUrl: String)
} 