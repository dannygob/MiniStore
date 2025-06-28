package com.example.ministore.data.repository

import com.example.ministore.data.local.dao.PurchaseDao
import com.example.ministore.data.mapper.toDomain
import com.example.ministore.data.mapper.toEntity
import com.example.ministore.domain.model.Purchase
import com.example.ministore.domain.model.PurchaseItem
import com.example.minstore.domain.models.PurchaseStatus
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PurchaseRepository @Inject constructor(
    private val purchaseDao: PurchaseDao,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    private val purchasesCollection = firestore.collection("purchases")
    private val purchaseItemsCollection = firestore.collection("purchase_items")

    suspend fun insertPurchase(purchase: Purchase, items: List<PurchaseItem>) {
        val purchaseEntity = purchase.toEntity()
        val itemEntities = items.map { it.toEntity(purchase.id) }

        // Save to local database
        purchaseDao.insertPurchaseWithItems(purchaseEntity, itemEntities)

        // Save to Firestore
        purchasesCollection.document(purchase.id).set(purchase).await()
        items.forEach { item ->
            purchaseItemsCollection.document().set(item).await()
        }
    }

    suspend fun updatePurchase(purchase: Purchase) {
        val purchaseEntity = purchase.toEntity()

        // Update local database
        purchaseDao.updatePurchase(purchaseEntity)

        // Update Firestore
        purchasesCollection.document(purchase.id).set(purchase).await()
    }

    suspend fun deletePurchase(purchase: Purchase) {
        val purchaseEntity = purchase.toEntity()

        // Delete from local database
        purchaseDao.deletePurchaseWithItems(purchaseEntity)

        // Delete from Firestore
        purchasesCollection.document(purchase.id).delete().await()
        purchaseItemsCollection
            .whereEqualTo("purchaseId", purchase.id)
            .get()
            .await()
            .documents
            .forEach { doc -> doc.reference.delete().await() }
    }

    fun getPurchaseById(id: String): Flow<Purchase?> {
        return purchaseDao.getPurchaseById(id).map { purchaseEntity ->
            purchaseEntity?.let { entity ->
                val items = purchaseDao.getPurchaseItemsByPurchaseId(id).map { itemEntities ->
                    itemEntities.map { it.toDomain() }
                }
                entity.toDomain(items)
            }
        }
    }

    fun getAllPurchases(): Flow<List<Purchase>> {
        return purchaseDao.getAllPurchases().map { purchases ->
            purchases.map { purchase ->
                val items =
                    purchaseDao.getPurchaseItemsByPurchaseId(purchase.id).map { itemEntities ->
                        itemEntities.map { it.toDomain() }
                    }
                purchase.toDomain(items)
            }
        }
    }

    fun getPurchasesByProvider(providerId: String): Flow<List<Purchase>> {
        return purchaseDao.getPurchasesByProvider(providerId).map { purchases ->
            purchases.map { purchase ->
                val items =
                    purchaseDao.getPurchaseItemsByPurchaseId(purchase.id).map { itemEntities ->
                        itemEntities.map { it.toDomain() }
                    }
                purchase.toDomain(items)
            }
        }
    }

    fun getPurchasesByDateRange(startDate: Date, endDate: Date): Flow<List<Purchase>> {
        return purchaseDao.getPurchasesByDateRange(startDate, endDate).map { purchases ->
            purchases.map { purchase ->
                val items =
                    purchaseDao.getPurchaseItemsByPurchaseId(purchase.id).map { itemEntities ->
                        itemEntities.map { it.toDomain() }
                    }
                purchase.toDomain(items)
            }
        }
    }

    fun getPurchasesByStatus(status: PurchaseStatus): Flow<List<Purchase>> {
        return purchaseDao.getPurchasesByStatus(status).map { purchases ->
            purchases.map { purchase ->
                val items =
                    purchaseDao.getPurchaseItemsByPurchaseId(purchase.id).map { itemEntities ->
                        itemEntities.map { it.toDomain() }
                    }
                purchase.toDomain(items)
            }
        }
    }

    suspend fun uploadReceiptImage(purchaseId: String, imageBytes: ByteArray): String {
        val storageRef = storage.reference
            .child("receipts")
            .child("purchases")
            .child("$purchaseId.jpg")

        return storageRef.putBytes(imageBytes).await()
            .storage
            .downloadUrl
            .await()
            .toString()
    }

    suspend fun syncWithFirestore() {
        // Fetch all purchases from Firestore
        val firestorePurchases = purchasesCollection.get().await()

        firestorePurchases.documents.forEach { doc ->
            val purchase = doc.toObject(Purchase::class.java)
            purchase?.let {
                val items = purchaseItemsCollection
                    .whereEqualTo("purchaseId", it.id)
                    .get()
                    .await()
                    .toObjects(PurchaseItem::class.java)

                // Update local database
                purchaseDao.insertPurchaseWithItems(
                    it.toEntity(),
                    items.map { item -> item.toEntity(it.id) }
                )
            }
        }
    }
} 