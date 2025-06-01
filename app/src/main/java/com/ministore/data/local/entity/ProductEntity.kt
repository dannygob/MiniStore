package com.ministore.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
// Removed TypeConverters, DateTimeConverter, StringListConverter, BigDecimal, LocalDateTime as they seem unused now
import com.ministore.domain.model.Product
import com.ministore.domain.model.ProductLocation // Import this

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val barcode: String, // Added
    val purchasePrice: Double, // Added
    val sellingPrice: Double, // Renamed from price
    val quantity: Int,
    val category: String,
    val providerId: String, // Added
    val locationAisle: String?, // Added
    val locationShelf: String?, // Added
    val locationLevel: String?, // Added
    val minimumStock: Int, // Added
    val expirationDate: Long?, // Added
    val imageUrl: String?, // Already existed
    val createdAt: Long, // Added
    val updatedAt: Long // Renamed from lastModified
) {
    fun toProduct(): Product = Product(
        id = id,
        name = name,
        barcode = barcode,
        category = category,
        purchasePrice = purchasePrice,
        sellingPrice = sellingPrice,
        quantity = quantity,
        providerId = providerId,
        location = if (locationAisle != null || locationShelf != null || locationLevel != null) {
            ProductLocation(
                aisle = locationAisle ?: "",
                shelf = locationShelf ?: "",
                level = locationLevel ?: ""
            )
        } else null,
        minimumStock = minimumStock,
        expirationDate = expirationDate,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    companion object {
        fun fromProduct(product: Product): ProductEntity = ProductEntity(
            id = product.id,
            name = product.name,
            barcode = product.barcode,
            purchasePrice = product.purchasePrice,
            sellingPrice = product.sellingPrice,
            quantity = product.quantity,
            category = product.category,
            providerId = product.providerId,
            locationAisle = product.location?.aisle,
            locationShelf = product.location?.shelf,
            locationLevel = product.location?.level,
            minimumStock = product.minimumStock,
            expirationDate = product.expirationDate,
            imageUrl = product.imageUrl,
            createdAt = product.createdAt,
            updatedAt = product.updatedAt
        )
    }
}