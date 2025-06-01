package com.ministore.di

import com.ministore.data.remote.api.OpenFoodFactsApiService // Updated import
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory // Updated import
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val OPEN_FOOD_FACTS_BASE_URL = "https://world.openfoodfacts.org/"

    @Provides
    @Singleton
    fun provideMoshi(): Moshi { // Added Moshi provider
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        // TODO: Add HttpLoggingInterceptor only for debug builds (e.g., if (BuildConfig.DEBUG))
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            // Add other configurations like timeouts if needed
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenFoodFactsRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit { // Renamed, added Moshi, uses MoshiConverterFactory
        return Retrofit.Builder()
            .baseUrl(OPEN_FOOD_FACTS_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi)) // Use Moshi
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenFoodFactsApiService(retrofit: Retrofit): OpenFoodFactsApiService { // Renamed and updated return type
        return retrofit.create(OpenFoodFactsApiService::class.java)
    }
}