package com.example.minstore.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.minstore.domain.models.PaymentMethod
import com.example.minstore.domain.models.SaleStatus
import java.util.Date

@Entity(tableName = "sales")
data class SaleEntity(
    @PrimaryKey
    val id: String,
    val clientId: String?,
    val total: Double,
    val paymentMethod: PaymentMethod,
    val status: SaleStatus,
    val createdAt: Date,
    val updatedAt: Date
)

@Entity(
    tableName = "sale_details",
    foreignKeys = [
        ForeignKey(
            entity = SaleEntity::class,
            parentColumns = ["id"],
            childColumns = ["saleId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SaleDetailEntity(
    @PrimaryKey
    val id: String,
    val saleId: String,
    val productId: String,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double
) 