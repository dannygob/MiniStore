package com.example.minstore.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.minstore.domain.models.PaymentMethod
import com.example.minstore.domain.models.PurchaseStatus
import java.util.Date

@Entity(tableName = "purchases")
data class PurchaseEntity(
    @PrimaryKey
    val id: String,
    val providerId: String,
    val total: Double,
    val status: PurchaseStatus,
    val paymentMethod: PaymentMethod,
    val createdAt: Date,
    val updatedAt: Date
)

@Entity(
    tableName = "purchase_details",
    foreignKeys = [
        ForeignKey(
            entity = PurchaseEntity::class,
            parentColumns = ["id"],
            childColumns = ["purchaseId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PurchaseDetailEntity(
    @PrimaryKey
    val id: String,
    val purchaseId: String,
    val productId: String,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double
) 