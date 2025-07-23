package com.example.ministore.data.repository

import com.example.ministore.data.local.dao.ProviderDao
import com.example.ministore.data.mapper.toDomain
import com.example.ministore.data.mapper.toEntity
import com.example.ministore.data.domain.repository.ProviderRepository
import com.example.ministore.domain.model.Provider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProviderRepositoryImpl(private val providerDao: ProviderDao) : ProviderRepository {
    override suspend fun insertProvider(provider: Provider) {
        providerDao.insertProvider(provider.toEntity())
    }

    override suspend fun updateProvider(provider: Provider) {
        providerDao.updateProvider(provider.toEntity())
    }

    override suspend fun deleteProvider(provider: Provider) {
        providerDao.deleteProvider(provider.toEntity())
    }

    override fun getProviderById(id: String): Flow<Provider?> {
        return providerDao.getProviderById(id).map { it?.toDomain() }
    }

    override fun getAllProviders(): Flow<List<Provider>> {
        return providerDao.getAllProviders().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getActiveProviders(): Flow<List<Provider>> {
        return providerDao.getActiveProviders().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun searchProviders(query: String): Flow<List<Provider>> {
        return providerDao.searchProviders(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun updateProviderActiveStatus(providerId: String, isActive: Boolean) {
        providerDao.updateProviderActiveStatus(providerId, isActive)
    }
}