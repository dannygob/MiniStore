package com.example.ministore.data.repository

import com.example.ministore.data.local.dao.ClientDao
import com.example.ministore.data.mapper.toDomain
import com.example.ministore.data.mapper.toEntity
import com.example.ministore.data.domain.repository.ClientRepository
import com.example.minstore.domain.models.Client
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ClientRepositoryImpl(private val clientDao: ClientDao) : ClientRepository {

    override fun getAllClients(): Flow<List<Client>> {
        return clientDao.getAllClients().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getClientById(id: String): Client? {
        return clientDao.getClientById(id)?.toDomain()
    }

    override suspend fun insertClient(client: Client) {
        clientDao.insertClient(client.toEntity())
    }

    override suspend fun updateClient(client: Client) {
        clientDao.updateClient(client.toEntity())
    }

    override suspend fun deleteClient(client: Client) {
        clientDao.deleteClient(client.toEntity())
    }

    override fun searchClients(query: String): Flow<List<Client>> {
        return clientDao.searchClients(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}