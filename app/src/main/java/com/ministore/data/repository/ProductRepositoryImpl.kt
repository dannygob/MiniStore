package com.ministore.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.ministore.data.local.dao.ProductDao
import com.ministore.data.local.entity.ProductEntity
import com.ministore.domain.model.Product
import com.ministore.domain.repository.ProductRepository
import com.ministore.util.NetworkConnectivityMonitor
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val productDao: ProductDao,
    private val networkMonitor: NetworkConnectivityMonitor
) : ProductRepository {

    private val productsCollection = firestore.collection("products")

    override fun getProducts(): Flow<List<Product>> = flow {
        // Always emit from local database first
        emitAll(productDao.getAllProducts().map { entities ->
            entities.map { it.toProduct() }
        })

        // If online, fetch from Firestore and update local database
        if (networkMonitor.observeNetworkState().first() is NetworkState.Connected) {
            try {
                val snapshot = productsCollection.get().await()
                val products = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Product::class.java)?.copy(id = doc.id)
                }

                // Update local database
                productDao.insertProducts(products.map { ProductEntity.fromProduct(it) })
            } catch (e: Exception) {
                // If Firestore fetch fails, we still have local data
                e.printStackTrace()
            }
        }
    }

    override suspend fun getProduct(id: String): Flow<Product?> = flow {
        // First emit from local database
        val localProduct = productDao.getProductById(id)?.toProduct()
        emit(localProduct)

        // If online, get from Firestore and update local
        if (networkMonitor.observeNetworkState().first() is NetworkState.Connected) {
            try {
                val snapshot = productsCollection.document(id).get().await()
                val product = snapshot?.toObject(Product::class.java)?.copy(id = snapshot.id)

                if (product != null) {
                    productDao.insertProduct(ProductEntity.fromProduct(product))
                    emit(product)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun addProduct(product: Product): Result<Unit> = try {
        // Add to local database first
        productDao.insertProduct(ProductEntity.fromProduct(product))

        // If online, sync to Firestore
        if (networkMonitor.observeNetworkState().first() is NetworkState.Connected) {
            productsCollection.document(product.id).set(product).await()
        }

        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateProduct(product: Product): Result<Unit> = try {
        // Update local database first
        productDao.updateProduct(ProductEntity.fromProduct(product))

        // If online, sync to Firestore
        if (networkMonitor.observeNetworkState().first() is NetworkState.Connected) {
            productsCollection.document(product.id).set(product).await()
        }

        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteProduct(productId: String): Result<Unit> = try {
        // Get product from local database
        val product = productDao.getProductById(productId)
        if (product != null) {
            // Delete from local database
            productDao.deleteProduct(product)

            // If online, sync to Firestore
            if (networkMonitor.observeNetworkState().first() is NetworkState.Connected) {
                productsCollection.document(productId).delete().await()
            }
        }

        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateProductQuantity(productId: String, newQuantity: Int): Result<Unit> =
        try {
            // Update local database first
            productDao.updateProductQuantity(productId, newQuantity)

            // If online, sync to Firestore
            if (networkMonitor.observeNetworkState().first() is NetworkState.Connected) {
                productsCollection.document(productId)
                    .update("quantity", newQuantity)
                    .await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override fun getLowStockProducts(minStock: Int): Flow<List<Product>> =
        productDao.getLowStockProducts(minStock).map { entities ->
            entities.map { it.toProduct() }
        }
} 