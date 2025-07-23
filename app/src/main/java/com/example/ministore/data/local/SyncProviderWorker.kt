package com.example.ministore.data.local

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.ministore.data.local.dao.ProviderDao
import com.example.ministore.data.mapper.toDomain
import com.google.firebase.firestore.FirebaseFirestore
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SyncProviderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val providerDao: ProviderDao,
    private val firestore: FirebaseFirestore,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val providerId = inputData.getString("provider_id")
        val action = inputData.getString("action")

        if (providerId == null || action == null) {
            return@withContext Result.failure()
        }

        return@withContext try {
            when (action) {
                "insert", "update" -> {
                    val provider = providerDao.getProviderById(providerId)?.toDomain()
                    if (provider != null) {
                        firestore.collection("providers").document(providerId).set(provider).await()
                    } else {
                        // Provider not found in local DB, maybe deleted before sync
                        Result.failure()
                    }
                }
                "delete" -> {
                    firestore.collection("providers").document(providerId).delete().await()
                }
                else -> Result.failure()
            }
            Result.success()
        } catch (e: Exception) {
            // Log the error or handle it as needed
            Result.retry()
        }
    }

    @dagger.assisted.AssistedFactory
    interface Factory {
        fun create(
            @Assisted context: Context,
            @Assisted workerParams: WorkerParameters,
        ): SyncProviderWorker
    }
}
