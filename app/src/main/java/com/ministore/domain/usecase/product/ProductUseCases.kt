package com.ministore.domain.usecase.product

import com.ministore.domain.model.Product
import com.ministore.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class ProductUseCases(
    val getProducts: GetProducts,
    val getProduct: GetProduct,
    val searchProducts: SearchProducts,
    val addProduct: AddProduct,
    val updateProduct: UpdateProduct,
    val deleteProduct: DeleteProduct,
    val updateStock: UpdateStock,
    val getLowStockProducts: GetLowStockProducts,
    val lookupProductByBarcode: LookupProductByBarcode
)

class GetProducts @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(sortBy: String? = null): Flow<List<Product>> {
        return if (sortBy != null) {
            repository.getSortedProducts(sortBy)
        } else {
            repository.getAllProducts()
        }
    }
}

class GetProduct @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(id: Long): Product? {
        return repository.getProductById(id)
    }
}

class SearchProducts @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(query: String): Flow<List<Product>> {
        return repository.searchProducts(query)
    }
}

class AddProduct @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(product: Product): Long {
        return repository.insertProduct(product)
    }
}

class UpdateProduct @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(product: Product) {
        repository.updateProduct(product)
    }
}

class DeleteProduct @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: Long) {
        repository.deleteProduct(productId)
    }
}

class UpdateStock @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: Long, quantity: Int) {
        repository.updateStock(productId, quantity)
    }
}

class GetLowStockProducts @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<List<Product>> {
        return repository.getLowStockProducts()
    }
}

class LookupProductByBarcode @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(barcode: String): Product? {
        // First try to find the product in local database
        return repository.getProductByBarcode(barcode)
        // If not found locally, try to fetch from Open Food Facts
            ?: repository.fetchProductInfoFromOpenFoodFacts(barcode)
    }
} 