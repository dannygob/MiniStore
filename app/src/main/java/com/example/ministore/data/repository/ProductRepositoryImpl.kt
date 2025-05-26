package com.example.ministore.data.repository

import com.example.ministore.data.local.dao.ProductDao
import com.example.ministore.data.mapper.toDomain
import com.example.ministore.data.mapper.toEntity
import com.example.ministore.domain.model.Product
import com.example.ministore.domain.repository.ProductRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val firestore: FirebaseFirestore
) : ProductRepository {

    override fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAllProducts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getProductById(id: Long): Product? {
        return productDao.getProductById(id)?.toDomain()
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

        // Sync with Firebase
        firestore.collection("products")
            .document(id.toString())
            .set(product)

        return id
    }

    override suspend fun insertProducts(products: List<Product>) {
        val entities = products.map { it.toEntity() }
        productDao.insertProducts(entities)

        // Sync with Firebase
        products.forEach { product ->
            firestore.collection("products")
                .document(product.id.toString())
                .set(product)
        }
    }

    override suspend fun updateProduct(product: Product) {
        val entity = product.toEntity()
        productDao.updateProduct(entity)

        // Sync with Firebase
        firestore.collection("products")
            .document(product.id.toString())
            .set(product)
    }

    override suspend fun deleteProduct(id: Long) {
        productDao.deleteProduct(id)

        // Sync with Firebase
        firestore.collection("products")
            .document(id.toString())
            .delete()
    }

    override suspend fun updateStock(id: Long, quantity: Int) {
        productDao.updateStock(id, quantity)

        // Update stock in Firebase
        firestore.collection("products")
            .document(id.toString())
            .update("stock", quantity)
    }
} 