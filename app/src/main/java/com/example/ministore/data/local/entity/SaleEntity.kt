package com.example.ministore.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.ministore.domain.model.PaymentMethod
import com.example.ministore.domain.model.SaleStatus
import java.util.Date

@Entity(tableName = "sales")
data class SaleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val clientId: Long?,
    val total: Double,
    val paymentMethod: PaymentMethod,
    val status: SaleStatus,
    val createdAt: Date,
    val updatedAt: Date,
    val isDeleted: Boolean = false
)

@Entity(
    tableName = "sale_items",
    primaryKeys = ["saleId", "productId"]
)
data class SaleItemEntity(
    val saleId: Long,
    val productId: Long,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double,
    val discount: Double
)

data class SaleWithItems(
    @Relation(
        parentColumn = "id",
        entityColumn = "saleId"
    )
    val sale: SaleEntity,
    val items: List<SaleItemEntity>
) 