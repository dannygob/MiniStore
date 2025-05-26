package com.example.ministore.domain.usecase.product

import com.example.ministore.domain.model.Product
import com.example.ministore.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(query: String): Flow<List<Product>> {
        return productRepository.searchProducts(query)
    }
} 