package com.example.ministore.data.repository

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.ministore.data.local.SyncProviderWorker
import com.example.ministore.data.local.dao.ProviderDao
import com.example.ministore.data.mapper.toDomain
import com.example.ministore.data.mapper.toEntity
import com.example.ministore.domain.model.Provider
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProviderRepository @Inject constructor(
    private val providerDao: ProviderDao,
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) {
    private val providersCollection = firestore.collection("providers")

    suspend fun insertProvider(provider: Provider) {
        val providerEntity = provider.toEntity()

        // Save to local database
        providerDao.insertProvider(providerEntity)

        // Enqueue WorkManager task to sync with Firestore
        val data = Data.Builder()
            .putString("provider_id", provider.id)
            .putString("action", "insert")
            .build()
        val syncWorkRequest = OneTimeWorkRequestBuilder<SyncProviderWorker>()
            .setInputData(data)
            .build()
        WorkManager.getInstance(context).enqueue(syncWorkRequest)
    }

    suspend fun updateProvider(provider: Provider) {
        val providerEntity = provider.toEntity()

        // Update local database
        providerDao.updateProvider(providerEntity)

        // Enqueue WorkManager task to sync with Firestore
        val data = Data.Builder()
            .putString("provider_id", provider.id)
            .putString("action", "update")
            .build()
        val syncWorkRequest = OneTimeWorkRequestBuilder<SyncProviderWorker>()
            .setInputData(data)
            .build()
        WorkManager.getInstance(context).enqueue(syncWorkRequest)
    }

    suspend fun deleteProvider(provider: Provider) {
        val providerEntity = provider.toEntity()

        // Delete from local database
        providerDao.deleteProvider(providerEntity)

        // Enqueue WorkManager task to sync with Firestore
        val data = Data.Builder()
            .putString("provider_id", provider.id)
            .putString("action", "delete")
            .build()
        val syncWorkRequest = OneTimeWorkRequestBuilder<SyncProviderWorker>()
            .setInputData(data)
            .build()
        WorkManager.getInstance(context).enqueue(syncWorkRequest)
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

        // Enqueue WorkManager task to sync with Firestore
        val data = Data.Builder()
            .putString("provider_id", providerId)
            .putString("action", "update") // We can potentially add a specific action for status update if needed
            .build()
        val syncWorkRequest = OneTimeWorkRequestBuilder<SyncProviderWorker>()
            .setInputData(data)
            .build()
        WorkManager.getInstance(context).enqueue(syncWorkRequest)
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