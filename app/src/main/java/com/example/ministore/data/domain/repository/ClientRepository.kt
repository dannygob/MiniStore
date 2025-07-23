package com.example.ministore.data.domain.repository

import com.example.minstore.domain.models.Client
import kotlinx.coroutines.flow.Flow

interface ClientRepository {
    fun getAllClients(): Flow<List<Client>>
    suspend fun getClientById(id: String): Client?
    suspend fun insertClient(client: Client)
    suspend fun updateClient(client: Client)
    suspend fun deleteClient(client: Client)
    fun searchClients(query: String): Flow<List<Client>>
}