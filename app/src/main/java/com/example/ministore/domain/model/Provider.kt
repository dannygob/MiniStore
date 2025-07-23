package com.example.ministore.domain.model

import com.example.ministore.domain.model.Address
import java.util.UUID

data class Provider(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val contactPerson: String,
    val phone: String,
    val email: String,
    val address: Address,
    val taxId: String? = null,
    val notes: String? = null,
    val isActive: Boolean = true
)