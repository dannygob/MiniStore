package com.ministore.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ministore.domain.model.Sale

@Entity(tableName = "sales")
data class SaleEntity(
    @PrimaryKey
    val id: String,
    val productId: String,
    val productName: String,
    val quantity: Int,
    val pricePerUnit: Double,
    val totalAmount: Double,
    val timestamp: Long,
    val syncedToCloud: Boolean = false
) {
    fun toSale(): Sale = Sale(
        id = id,
        productId = productId,
        productName = productName,
        quantity = quantity,
        pricePerUnit = pricePerUnit,
        totalAmount = totalAmount,
        timestamp = timestamp
    )

    companion object {
        fun fromSale(sale: Sale, syncedToCloud: Boolean = false): SaleEntity = SaleEntity(
            id = sale.id,
            productId = sale.productId,
            productName = sale.productName,
            quantity = sale.quantity,
            pricePerUnit = sale.pricePerUnit,
            totalAmount = sale.totalAmount,
            timestamp = sale.timestamp,
            syncedToCloud = syncedToCloud
        )
    }
} 