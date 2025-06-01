package com.ministore.data.remote.api

import com.ministore.data.remote.dto.OpenFoodFactsProductResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenFoodFactsApiService {
    @GET("api/v0/product/{barcode}.json")
    suspend fun getProductByBarcode(
        @Path("barcode") barcode: String
    ): Response<OpenFoodFactsProductResponseDto>
}
