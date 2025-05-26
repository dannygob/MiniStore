package com.ministore.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import java.math.BigDecimal

interface OpenFoodFactsApi {
    @GET("api/v0/product/{barcode}.json")
    suspend fun getProduct(@Path("barcode") barcode: String): OpenFoodFactsResponse
}

data class OpenFoodFactsResponse(
    val status: Int,
    val product: OpenFoodFactsProduct?
)

data class OpenFoodFactsProduct(
    val id: String,
    val productName: String?,
    val description: String?,
    val categories: String?,
    val labels: String?,
    val imageUrl: String?,
    val price: BigDecimal?
) 