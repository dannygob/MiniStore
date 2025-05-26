package com.example.minstore.data.repository

import com.example.minstore.data.local.dao.ProductDao
import com.example.minstore.data.local.entities.ProductEntity
import com.example.minstore.domain.models.Product
import com.example.minstore.domain.models.ProductLocation
import com.example.minstore.domain.repository.ProductRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ProductRepository {

    private val productsCollection = firestore.collection("products")
    private val storageRef = storage.reference.child("product_images")

    override fun getAllProducts(): Flow<List<Product>> =
        productDao.getAllProducts().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getProductById(id: String): Product? =
        productDao.getProductById(id)?.toDomain()

    override suspend fun getProductByBarcode(barcode: String): Product? =
        productDao.getProductByBarcode(barcode)?.toDomain()

    override fun getProductsByCategory(category: String): Flow<List<Product>> =
        productDao.getProductsByCategory(category).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getLowStockProducts(): Flow<List<Product>> =
        productDao.getLowStockProducts().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getProductsExpiringBefore(date: Date): Flow<List<Product>> =
        productDao.getProductsExpiringBefore(date.time).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getProductsByProvider(providerId: String): Flow<List<Product>> =
        productDao.getProductsByProvider(providerId).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun insertProduct(product: Product) {
        productDao.insertProduct(product.toEntity())
        productsCollection.document(product.id).set(product).await()
    }

    override suspend fun insertProducts(products: List<Product>) {
        productDao.insertProducts(products.map { it.toEntity() })
        products.forEach { product ->
            productsCollection.document(product.id).set(product).await()
        }
    }

    override suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product.toEntity())
        productsCollection.document(product.id).set(product).await()
    }

    override suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product.toEntity())
        productsCollection.document(product.id).delete().await()
        product.imageUrl?.let { deleteProductImage(it) }
    }

    override suspend fun incrementStock(productId: String, quantity: Int) {
        productDao.incrementStock(productId, quantity)
        productsCollection.document(productId)
            .update("stock", getProductById(productId)?.stock).await()
    }

    override suspend fun decrementStock(productId: String, quantity: Int) {
        productDao.decrementStock(productId, quantity)
        productsCollection.document(productId)
            .update("stock", getProductById(productId)?.stock).await()
    }

    override fun searchProducts(query: String): Flow<List<Product>> =
        productDao.searchProducts(query).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun syncWithFirestore() {
        val remoteProducts = productsCollection.get().await()
            .documents.mapNotNull { it.toObject(Product::class.java) }
        productDao.insertProducts(remoteProducts.map { it.toEntity() })
    }

    override suspend fun uploadProductImage(productId: String, imageBytes: ByteArray): String {
        val imageRef = storageRef.child("$productId/${UUID.randomUUID()}.jpg")
        imageRef.putBytes(imageBytes).await()
        return imageRef.downloadUrl.await().toString()
    }

    override suspend fun deleteProductImage(imageUrl: String) {
        storage.getReferenceFromUrl(imageUrl).delete().await()
    }

    private fun ProductEntity.toDomain() = Product(
        id = id,
        name = name,
        barcode = barcode,
        purchasePrice = purchasePrice,
        sellingPrice = sellingPrice,
        category = category,
        stock = stock,
        providerId = providerId,
        location = ProductLocation(
            aisle = locationAisle,
            shelf = locationShelf,
            level = locationLevel
        ),
        minimumStock = minimumStock,
        expirationDate = expirationDate,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    private fun Product.toEntity() = ProductEntity(
        id = id,
        name = name,
        barcode = barcode,
        purchasePrice = purchasePrice,
        sellingPrice = sellingPrice,
        category = category,
        stock = stock,
        providerId = providerId,
        locationAisle = location.aisle,
        locationShelf = location.shelf,
        locationLevel = location.level,
        minimumStock = minimumStock,
        expirationDate = expirationDate,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
} 