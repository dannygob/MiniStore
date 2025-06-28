package com.example.ministore.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.ministore.data.local.converter.DateConverter
import com.example.ministore.data.local.converter.PaymentMethodConverter
import com.example.ministore.domain.model.PaymentMethod
import com.example.minstore.domain.models.PurchaseStatus
import java.util.Date

@Entity(tableName = "purchases")
@TypeConverters(DateConverter::class, PaymentMethodConverter::class, PurchaseStatusConverter::class)
data class PurchaseEntity(
    @PrimaryKey
    val id: String,
    val providerId: String,
    val totalAmount: Double,
    val paymentMethod: PaymentMethod,
    val status: PurchaseStatus,
    val date: Date,
    val notes: String?,
    val invoiceNumber: String?,
    val receiptUrl: String?
)

@Entity(tableName = "purchase_items")
data class PurchaseItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val purchaseId: String,
    val productId: String,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double
) 