package com.example.ministore.domain.model

data class Client(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val address: Address?,
    val createdAt: java.util.Date?,
    val updatedAt: java.util.Date?
)