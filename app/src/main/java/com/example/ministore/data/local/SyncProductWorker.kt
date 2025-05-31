package com.example.ministore.data.local

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.ministore.data.local.dao.ProductDao
import com.example.ministore.data.mapper.toDomain
import com.google.firebase.firestore.FirebaseFirestore
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SyncProductWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val productDao: ProductDao,
    private val firestore: FirebaseFirestore,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        return@withContext try {
            val productId = inputData.getLong("product_id", -1)
            if (productId == -1L) {
                return@withContext Result.failure()
            }

            val product = productDao.getProductById(productId)?.toDomain()
            if (product == null) {
                return@withContext Result.failure()
            }

            val quantity = inputData.getInt("quantity", -1)
            if (quantity == -1) {
                firestore.collection("products")
                    .document(productId.toString())
                    .set(product)
            } else {
                firestore.collection("products")
                    .document(productId.toString())
                    .update("stockQuantity", quantity)
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    @dagger.assisted.AssistedFactory
    interface Factory {
        fun create(
            @Assisted context: Context,
            @Assisted workerParams: WorkerParameters,
        ): SyncProductWorker
    }
}
