package com.ministore.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ministore.data.local.dao.SaleDao
import com.ministore.data.local.entity.SaleEntity
import com.ministore.domain.model.Sale
import com.ministore.domain.repository.SaleRepository
import com.ministore.util.NetworkConnectivityMonitor
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SaleRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val saleDao: SaleDao,
    private val networkMonitor: NetworkConnectivityMonitor
) : SaleRepository {

    private val salesCollection = firestore.collection("sales")

    override fun getSales(): Flow<List<Sale>> = flow {
        // Always emit from local database first
        emitAll(saleDao.getAllSales().map { entities ->
            entities.map { it.toSale() }
        })

        // If online, fetch from Firestore and update local database
        if (networkMonitor.observeNetworkState().first() is NetworkState.Connected) {
            try {
                val snapshot = salesCollection
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .await()

                val sales = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Sale::class.java)?.copy(id = doc.id)
                }

                // Update local database
                saleDao.insertSales(sales.map { SaleEntity.fromProduct(it, true) })
            } catch (e: Exception) {
                // If Firestore fetch fails, we still have local data
                e.printStackTrace()
            }
        }
    }

    override suspend fun addSale(sale: Sale): Result<Unit> = try {
        // Add to local database first
        saleDao.insertSale(SaleEntity.fromSale(sale))

        // If online, sync to Firestore
        if (networkMonitor.observeNetworkState().first() is NetworkState.Connected) {
            salesCollection.document(sale.id).set(sale).await()
            saleDao.markSaleAsSynced(sale.id)
        }

        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getSalesByProduct(productId: String): Flow<List<Sale>> =
        saleDao.getSalesByProduct(productId).map { entities ->
            entities.map { it.toSale() }
        }

    override suspend fun getTotalSales(): Flow<Double> =
        saleDao.getTotalSales().map { it ?: 0.0 }

    // Background sync function to sync unsynced sales when online
    suspend fun syncUnsyncedSales() {
        if (networkMonitor.observeNetworkState().first() is NetworkState.Connected) {
            val unsyncedSales = saleDao.getUnsyncedSales()
            unsyncedSales.forEach { saleEntity ->
                try {
                    salesCollection.document(saleEntity.id).set(saleEntity.toSale()).await()
                    saleDao.markSaleAsSynced(saleEntity.id)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
} 