package com.example.ministore.data.repository

import com.example.ministore.data.local.dao.ProviderDao
import com.example.ministore.data.mapper.toDomain
import com.example.ministore.data.mapper.toEntity
import com.example.ministore.domain.model.Provider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProviderRepository @Inject constructor(
    private val providerDao: ProviderDao,
    private val firestore: FirebaseFirestore
) {
    private val providersCollection = firestore.collection("providers")

    suspend fun insertProvider(provider: Provider) {
        val providerEntity = provider.toEntity()

        // Save to local database
        providerDao.insertProvider(providerEntity)

        // Save to Firestore
        providersCollection.document(provider.id).set(provider).await()
    }

    suspend fun updateProvider(provider: Provider) {
        val providerEntity = provider.toEntity()

        // Update local database
        providerDao.updateProvider(providerEntity)

        // Update Firestore
        providersCollection.document(provider.id).set(provider).await()
    }

    suspend fun deleteProvider(provider: Provider) {
        val providerEntity = provider.toEntity()

        // Delete from local database
        providerDao.deleteProvider(providerEntity)

        // Delete from Firestore
        providersCollection.document(provider.id).delete().await()
    }

    fun getProviderById(id: String): Flow<Provider?> {
        return providerDao.getProviderById(id).map { it?.toDomain() }
    }

    fun getAllProviders(): Flow<List<Provider>> {
        return providerDao.getAllProviders().map { providers ->
            providers.map { it.toDomain() }
        }
    }

    fun getActiveProviders(): Flow<List<Provider>> {
        return providerDao.getActiveProviders().map { providers ->
            providers.map { it.toDomain() }
        }
    }

    fun searchProviders(query: String): Flow<List<Provider>> {
        return providerDao.searchProviders(query).map { providers ->
            providers.map { it.toDomain() }
        }
    }

    suspend fun updateProviderActiveStatus(providerId: String, isActive: Boolean) {
        // Update local database
        providerDao.updateProviderActiveStatus(providerId, isActive)

        // Update Firestore
        providersCollection.document(providerId)
            .update("isActive", isActive)
            .await()
    }

    suspend fun syncWithFirestore() {
        // Fetch all providers from Firestore
        val firestoreProviders = providersCollection.get().await()

        firestoreProviders.documents.forEach { doc ->
            val provider = doc.toObject(Provider::class.java)
            provider?.let {
                // Update local database
                providerDao.insertProvider(it.toEntity())
            }
        }
    }
} 