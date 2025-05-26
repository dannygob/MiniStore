package com.example.minstore.domain.repository

import com.example.minstore.domain.models.Client
import kotlinx.coroutines.flow.Flow

interface ClientRepository {
    fun getAllClients(): Flow<List<Client>>
    suspend fun getClientById(id: String): Client?
    suspend fun getClientByEmail(email: String): Client?
    suspend fun getClientByPhone(phone: String): Client?
    suspend fun insertClient(client: Client)
    suspend fun insertClients(clients: List<Client>)
    suspend fun updateClient(client: Client)
    suspend fun deleteClient(client: Client)
    fun searchClients(query: String): Flow<List<Client>>

    // Firebase specific operations
    suspend fun syncWithFirestore()
} 