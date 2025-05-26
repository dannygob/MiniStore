package com.example.minstore.domain.models

import java.util.Date

data class Provider(
    val id: String = "",
    val name: String,
    val phone: String?,
    val email: String?,
    val address: Address?,
    val taxId: String?,
    val notes: String?,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
) 