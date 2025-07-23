package com.example.ministore.data.local

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.ministore.data.local.dao.ClientDao
import com.example.minstore.data.mapper.toDomain
import com.google.firebase.firestore.FirebaseFirestore
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SyncClientWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val clientDao: ClientDao,
    private val firestore: FirebaseFirestore,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val clientId = inputData.getString("client_id")
        val action = inputData.getString("action")

        if (clientId == null || action == null) {
            return@withContext Result.failure()
        }

        return@withContext try {
            when (action) {
                "insert", "update" -> {
                    val client = clientDao.getClientById(clientId)?.toDomain()
                    if (client != null) {
                        firestore.collection("clients").document(clientId).set(client).await()
                    } else {
                        // Client not found in local DB, maybe deleted before sync
                        Result.failure()
                    }
                }
                "delete" -> {
                    firestore.collection("clients").document(clientId).delete().await()
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
        ): SyncClientWorker
    }
}
