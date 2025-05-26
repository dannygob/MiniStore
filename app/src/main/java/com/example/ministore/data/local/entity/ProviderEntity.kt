package com.example.ministore.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ministore.domain.model.Address

@Entity(tableName = "providers")
data class ProviderEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val contactPerson: String,
    val phone: String,
    val email: String,
    @Embedded(prefix = "address_")
    val address: Address,
    val taxId: String?,
    val notes: String?,
    val isActive: Boolean
) 