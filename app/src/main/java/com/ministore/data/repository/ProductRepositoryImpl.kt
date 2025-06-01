package com.ministore.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage // Added import
import com.ministore.data.local.dao.ProductDao
import com.ministore.data.local.entity.ProductEntity
import com.ministore.data.remote.api.OpenFoodFactsApiService // Added import
import com.ministore.domain.model.Product
import com.ministore.domain.repository.ProductRepository
import com.ministore.util.NetworkConnectivityMonitor
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import java.util.UUID // Added import
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val productDao: ProductDao,
    private val networkMonitor: NetworkConnectivityMonitor,
    private val storage: FirebaseStorage, // Added FirebaseStorage
    private val openFoodFactsApiService: OpenFoodFactsApiService // Added OpenFoodFactsApiService
) : ProductRepository {

    private val productsCollection = firestore.collection("products")
    private val storageRef = storage.reference.child("product_images") // Added storage reference

    override fun getProducts(): Flow<List<Product>> = flow {
        emitAll(productDao.getAllProducts().map { entities ->
            entities.map { it.toProduct() }
        })

        if (networkMonitor.observeNetworkState().first() is com.ministore.presentation.util.NetworkState.Connected) { // Explicit path for NetworkState
            try {
                val snapshot = productsCollection.get().await()
                val products = snapshot.documents.mapNotNull { doc ->
                    // Using ProductEntity for Firestore deserialization is safer
                    doc.toObject(ProductEntity::class.java)?.copy(id = doc.id)?.toProduct()
                }
                if (products.isNotEmpty()) { // Check if products list is not empty before inserting
                     productDao.insertProducts(products.map { ProductEntity.fromProduct(it) })
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun getProduct(id: String): Flow<Product?> = flow {
        val localProductEntity = productDao.getProductById(id)
        emit(localProductEntity?.toProduct()) // Emit local data first

        if (networkMonitor.observeNetworkState().first() is com.ministore.presentation.util.NetworkState.Connected) { // Explicit path for NetworkState
            try {
                val snapshot = productsCollection.document(id).get().await()
                if (snapshot.exists()) {
                    val product = snapshot.toObject(ProductEntity::class.java)?.copy(id = snapshot.id)?.toProduct()
                    if (product != null) {
                        productDao.insertProduct(ProductEntity.fromProduct(product)) // Update local cache
                        emit(product) // Emit the fresh product from Firestore
                    } else {
                        // This case (snapshot exists but can't be converted) should ideally not happen if data is consistent
                        // If it does, it implies a data consistency issue in Firestore or mapping problem
                        // For now, we've already emitted the local cache, so no further emit here unless we decide local is invalid.
                    }
                } else { // Document does not exist in Firestore
                    if (localProductEntity != null) {
                        // Product was in local cache but not in Firestore, so delete from local
                        productDao.deleteProduct(localProductEntity)
                        emit(null) // Reflect that it's gone
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace() // Log error, local data (or null if it was already null) was already emitted
            }
        }
    }


    override suspend fun addProduct(product: Product): Result<Unit> = try {
        // For addProduct, ensure `createdAt` and `updatedAt` are set on the domain model if not already.
        // The Product domain model already sets these on construction.
        val entity = ProductEntity.fromProduct(product)
        productDao.insertProduct(entity)

        if (networkMonitor.observeNetworkState().first() is com.ministore.presentation.util.NetworkState.Connected) { // Explicit path for NetworkState
            // Store the domain `product` object which now includes all fields including timestamps
            productsCollection.document(product.id).set(product).await()
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateProduct(product: Product): Result<Unit> = try {
        // Ensure `updatedAt` is fresh for the update operation.
        val productToUpdate = product.copy(updatedAt = System.currentTimeMillis())
        val entity = ProductEntity.fromProduct(productToUpdate)
        productDao.updateProduct(entity)

        if (networkMonitor.observeNetworkState().first() is com.ministore.presentation.util.NetworkState.Connected) { // Explicit path for NetworkState
            productsCollection.document(productToUpdate.id).set(productToUpdate).await()
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteProduct(productId: String): Result<Unit> = try {
        val productEntity = productDao.getProductById(productId) // Fetch before deleting from DAO

        if (productEntity != null) {
            // 1. Delete from local DAO
            productDao.deleteProduct(productEntity)

            // 2. Delete image from Firebase Storage if URL exists
            productEntity.imageUrl?.let { imageUrl ->
                if (imageUrl.isNotBlank()) {
                    deleteProductImage(imageUrl).onFailure {
                        System.err.println("Failed to delete image $imageUrl: ${it.message}") // Log image deletion failure
                    }
                }
            }

            // 3. Delete from Firestore if online
            if (networkMonitor.observeNetworkState().first() is com.ministore.presentation.util.NetworkState.Connected) { // Explicit path for NetworkState
                productsCollection.document(productId).delete().await()
            }
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // Method to upload product image
    override suspend fun uploadProductImage(productId: String, imageBytes: ByteArray): Result<String> { // Added override
        return try {
            val imageRef = storageRef.child("$productId/${UUID.randomUUID()}.jpg")
            imageRef.putBytes(imageBytes).await()
            val downloadUrl = imageRef.downloadUrl.await().toString()
            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Method to delete product image
    override suspend fun deleteProductImage(imageUrl: String): Result<Unit> { // Added override
        return try {
            if (imageUrl.isBlank()) return Result.success(Unit)
            // Basic check for typical Firebase Storage URL structure
            if (!imageUrl.startsWith("gs://") && !imageUrl.contains("firebasestorage.googleapis.com")) {
                 // Not a Firebase URL, or malformed. Could log or simply return success if non-Firebase URLs are permissible.
                 // For now, let's assume only valid Firebase URLs should be processed for deletion.
                 // If it's not a firebase URL, we can't delete it via Firebase SDK.
                 System.err.println("Skipping deletion of non-Firebase Storage URL: $imageUrl")
                 return Result.success(Unit)
            }
            storage.getReferenceFromUrl(imageUrl).delete().await()
            Result.success(Unit)
        } catch (e: Exception) { // Catch specific exceptions like IllegalArgumentException if URL is malformed for getReferenceFromUrl
            System.err.println("Failed to delete image from URL $imageUrl: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun updateProductQuantity(productId: String, newQuantity: Int): Result<Unit> =
        try {
            val currentProductEntity = productDao.getProductById(productId)
                ?: return Result.failure(Exception("Product not found for quantity update: $productId"))

            val updatedTimestamp = System.currentTimeMillis()
            // Update local db: only quantity and updatedAt
            // Creating a new entity just for this is not ideal with Room's updateProduct if it replaces all fields.
            // A specific DAO method to update quantity and timestamp would be better.
            // For now, we use the existing updateProductQuantity and then update the timestamp separately if needed.
            productDao.updateProductQuantity(productId, newQuantity)
            // To update timestamp, one might fetch, copy, and save, or have a specific DAO.
            // Let's assume Firestore is the master for updatedAt on quantity change for now.
            // productDao.updateProduct(currentProductEntity.copy(quantity = newQuantity, updatedAt = updatedTimestamp))


            if (networkMonitor.observeNetworkState().first() is com.ministore.presentation.util.NetworkState.Connected) { // Explicit path for NetworkState
                productsCollection.document(productId)
                    .update(mapOf(
                        "quantity" to newQuantity,
                        "updatedAt" to updatedTimestamp // Ensure this field exists in Firestore documents
                    ))
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

    override fun getProductByBarcode(barcode: String): Flow<Product?> {
        return productDao.getProductByBarcode(barcode).map { entity ->
            entity?.toProduct()
        }
    }

    override fun getProductsByCategory(category: String): Flow<List<Product>> {
        return productDao.getProductsByCategory(category).map { entities ->
            entities.map { it.toProduct() }
        }
    }

    override fun searchProducts(query: String): Flow<List<Product>> {
        return productDao.searchProducts(query).map { entities ->
            entities.map { it.toProduct() }
        }
    }

    override suspend fun fetchProductDetailsFromApi(barcode: String): Result<com.ministore.domain.model.Product?> { // Added override
        return try {
            val response = openFoodFactsApiService.getProductByBarcode(barcode)
            if (response.isSuccessful && response.body() != null) {
                val responseDto = response.body()!!
                if (responseDto.status == 1 && responseDto.product != null) {
                    // Map DTO to domain Product model
                    val apiProduct = responseDto.product
                    val domainProduct = com.ministore.domain.model.Product(
                        id = responseDto.code ?: barcode, // Use barcode as ID if API code is missing
                        name = apiProduct.productNameEn ?: apiProduct.productName ?: "Unknown Product",
                        barcode = responseDto.code ?: barcode,
                        category = apiProduct.categories?.split(",")?.firstOrNull()?.trim() ?: "Unknown",
                        imageUrl = apiProduct.imageUrl,
                        // Ensure default values for fields not provided by API
                        sellingPrice = 0.0,
                        purchasePrice = 0.0,
                        quantity = 0,
                        minimumStock = com.ministore.domain.model.Product.DEFAULT_MIN_STOCK,
                        providerId = apiProduct.brands ?: "",  // Using brands as providerId placeholder
                        // createdAt, updatedAt will use defaults from Product domain model
                    )
                    Result.success(domainProduct)
                } else {
                    Result.success(null) // Product not found or error in DTO status
                }
            } else {
                Result.failure(Exception("API call failed with code: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}