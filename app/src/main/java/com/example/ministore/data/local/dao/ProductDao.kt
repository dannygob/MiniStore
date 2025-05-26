package com.example.ministore.data.local.dao

import androidx.room.*
import com.example.ministore.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE isDeleted = 0")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id AND isDeleted = 0")
    suspend fun getProductById(id: Long): ProductEntity?

    @Query("SELECT * FROM products WHERE stock <= minStock AND isDeleted = 0")
    fun getLowStockProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%' AND isDeleted = 0")
    fun searchProducts(query: String): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("UPDATE products SET isDeleted = 1 WHERE id = :id")
    suspend fun deleteProduct(id: Long)

    @Query("UPDATE products SET stock = stock + :quantity WHERE id = :id")
    suspend fun updateStock(id: Long, quantity: Int)
} 