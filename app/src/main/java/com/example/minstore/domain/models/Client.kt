package com.example.minstore.domain.models

import java.util.Date

data class Client(
    val id: String = "",
    val name: String,
    val phone: String?,
    val email: String?,
    val address: Address?,
    val preferredPaymentMethod: PaymentMethod = PaymentMethod.CASH,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

data class Address(
    val street: String,
    val number: String,
    val city: String,
    val state: String,
    val zipCode: String
) 