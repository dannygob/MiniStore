package com.ministore.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpenFoodFactsProductResponseDto(
    @Json(name = "status") val status: Int?,
    @Json(name = "code") val code: String?, // This is the barcode
    @Json(name = "product") val product: OpenFoodFactsProductDetailDto?
)

@JsonClass(generateAdapter = true)
data class OpenFoodFactsProductDetailDto(
    @Json(name = "product_name") val productName: String?,
    @Json(name = "product_name_en") val productNameEn: String?, // Often more complete
    @Json(name = "image_url") val imageUrl: String?,
    @Json(name = "categories") val categories: String?, // Comma-separated string
    @Json(name = "brands") val brands: String?,
    @Json(name = "quantity") val quantityValue: String? // e.g., "500 g"
    // Add other fields as needed, carefully matching JSON response
)
