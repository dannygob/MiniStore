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

    // New methods
    fun getProductByBarcode(barcode: String): Flow<Product?>
    fun getProductsByCategory(category: String): Flow<List<Product>>
    fun searchProducts(query: String): Flow<List<Product>>
    suspend fun uploadProductImage(productId: String, imageBytes: ByteArray): Result<String>
    suspend fun deleteProductImage(imageUrl: String): Result<Unit>
    suspend fun fetchProductDetailsFromApi(barcode: String): Result<Product?>
}