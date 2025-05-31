package com.example.ministore.data.repository

import android.content.Context
import android.util.LruCache
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.ministore.data.local.SyncProductWorker
import com.example.ministore.data.local.dao.ProductDao
import com.example.ministore.data.mapper.toDomain
import com.example.ministore.data.mapper.toEntity
import com.example.ministore.domain.model.Product
import com.example.ministore.domain.repository.ProductRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context,
) : ProductRepository {

    private val productCache = LruCache<Long, Product>(100) // Cache up to 100 products

    override fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAllProducts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getProductById(id: Long): Product? {
        // Check if product is in cache
        val cachedProduct = productCache.get(id)
        if (cachedProduct != null) {
            return cachedProduct
        }

        // If not in cache, get from database
        val product = productDao.getProductById(id)?.toDomain()

        // Add to cache if found
        if (product != null) {
            productCache.put(id, product)
        }

        return product
    }

    override fun getLowStockProducts(): Flow<List<Product>> {
        return productDao.getLowStockProducts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun searchProducts(query: String): Flow<List<Product>> {
        return productDao.searchProducts(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertProduct(product: Product): Long {
        val entity = product.toEntity()
        val id = productDao.insertProduct(entity)

        // Sync with Firebase in background
        val data = Data.Builder()
            .putLong("product_id", id)
            .build()

        val syncWorkRequest = OneTimeWorkRequestBuilder<SyncProductWorker>()
            .setInputData(data)
            .build()

        WorkManager.getInstance(context).enqueue(syncWorkRequest)

        return id
    }

    override suspend fun insertProducts(products: List<Product>) {
        val entities = products.map { it.toEntity() }
        productDao.insertProducts(entities)

        // Sync with Firebase in background
        products.forEach { product ->
            val data = Data.Builder()
                .putLong("product_id", product.id)
                .build()

            val syncWorkRequest = OneTimeWorkRequestBuilder<SyncProductWorker>()
                .setInputData(data)
                .build()

            WorkManager.getInstance(context).enqueue(syncWorkRequest)
        }
    }

    override suspend fun updateProduct(product: Product) {
        val entity = product.toEntity()
        productDao.updateProduct(entity)

        // Sync with Firebase in background
        val data = Data.Builder()
            .putLong("product_id", product.id)
            .build()

        val syncWorkRequest = OneTimeWorkRequestBuilder<SyncProductWorker>()
            .setInputData(data)
            .build()

        WorkManager.getInstance(context).enqueue(syncWorkRequest)
    }

    override suspend fun deleteProduct(id: Long) {
        productDao.deleteProduct(id)

        // Sync with Firebase in background
        val data = Data.Builder()
            .putLong("product_id", id)
            .build()

        val syncWorkRequest = OneTimeWorkRequestBuilder<SyncProductWorker>()
            .setInputData(data)
            .build()

        WorkManager.getInstance(context).enqueue(syncWorkRequest)
    }

    override suspend fun updateStock(id: Long, quantity: Int) {
        productDao.updateStock(id, quantity)

        // Update stock in Firebase in background
        val data = Data.Builder()
            .putLong("product_id", id)
            .putInt("quantity", quantity)
            .build()

        val syncWorkRequest = OneTimeWorkRequestBuilder<SyncProductWorker>()
            .setInputData(data)
            .build()

        WorkManager.getInstance(context).enqueue(syncWorkRequest)
    }
}
