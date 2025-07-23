package com.example.ministore.data.domain.repository

import com.example.ministore.domain.model.Provider
import kotlinx.coroutines.flow.Flow

interface ProviderRepository {
    suspend fun insertProvider(provider: Provider)
    suspend fun updateProvider(provider: Provider)
    suspend fun deleteProvider(provider: Provider)
    fun getProviderById(id: String): Flow<Provider?>
    fun getAllProviders(): Flow<List<Provider>>
    fun getActiveProviders(): Flow<List<Provider>>
    fun searchProviders(query: String): Flow<List<Provider>>
    suspend fun updateProviderActiveStatus(providerId: String, isActive: Boolean)
}