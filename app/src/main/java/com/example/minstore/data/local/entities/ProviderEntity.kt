package com.example.minstore.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "providers")
data class ProviderEntity(
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
    val taxId: String?,
    val notes: String?,
    val createdAt: Date,
    val updatedAt: Date
) 