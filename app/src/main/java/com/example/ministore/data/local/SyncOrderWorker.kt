package com.example.ministore.data.local

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.ministore.data.local.dao.OrderDao
import com.example.ministore.data.mapper.toDomain
import com.google.firebase.firestore.FirebaseFirestore
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SyncOrderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val orderDao: OrderDao,
    private val firestore: FirebaseFirestore,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val orderId = inputData.getString("order_id")
        val action = inputData.getString("action")

        if (orderId == null || action == null) {
            return@withContext Result.failure()
        }

        return@withContext try {
            when (action) {
                "insert", "update" -> {
                    val order = orderDao.getOrderById(orderId)?.toDomain()
                    if (order != null) {
                        firestore.collection("orders").document(orderId).set(order).await()
                    } else {
                        // Order not found in local DB, maybe deleted before sync
                        Result.failure()
                    }
                }
                "delete" -> {
                    firestore.collection("orders").document(orderId).delete().await()
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
        ): SyncOrderWorker
    }
}
