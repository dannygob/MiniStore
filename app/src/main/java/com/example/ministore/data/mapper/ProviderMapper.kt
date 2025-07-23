package com.example.ministore.data.mapper

import com.example.ministore.data.local.entity.ProviderEntity
import com.example.ministore.domain.model.Provider

fun ProviderEntity.toDomain(): Provider {
    return Provider(
        id = id,
        name = name,
        contactPerson = contactPerson,
        phone = phone,
        email = email,
        address = address,
        taxId = taxId,
        notes = notes,
        isActive = isActive
    )
}

fun Provider.toEntity(): ProviderEntity {
    return ProviderEntity(
        id = id,
        name = name,
        contactPerson = contactPerson,
        phone = phone,
        email = email,
        address = address,
        taxId = taxId,
        notes = notes,
        isActive = isActive
    )
}