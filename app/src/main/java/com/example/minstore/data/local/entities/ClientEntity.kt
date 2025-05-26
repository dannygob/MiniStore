package com.example.minstore.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.minstore.domain.models.PaymentMethod
import java.util.Date

@Entity(tableName = "clients")
data class ClientEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val phone: String?,
    val email: String?,
    val addressStreet: String?,
    val addressNumber: String?,
    val addressCity: String?,
    val addressState: String?,
    val addressZipCode: String?,
    val preferredPaymentMethod: PaymentMethod,
    val createdAt: Date,
    val updatedAt: Date
) 