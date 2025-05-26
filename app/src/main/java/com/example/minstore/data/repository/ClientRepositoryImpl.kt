package com.example.minstore.data.repository

import com.example.minstore.data.local.dao.ClientDao
import com.example.minstore.data.local.entities.ClientEntity
import com.example.minstore.domain.models.Address
import com.example.minstore.domain.models.Client
import com.example.minstore.domain.repository.ClientRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ClientRepositoryImpl @Inject constructor(
    private val clientDao: ClientDao,
    private val firestore: FirebaseFirestore
) : ClientRepository {

    private val clientsCollection = firestore.collection("clients")

    override fun getAllClients(): Flow<List<Client>> =
        clientDao.getAllClients().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getClientById(id: String): Client? =
        clientDao.getClientById(id)?.toDomain()

    override suspend fun getClientByEmail(email: String): Client? =
        clientDao.getClientByEmail(email)?.toDomain()

    override suspend fun getClientByPhone(phone: String): Client? =
        clientDao.getClientByPhone(phone)?.toDomain()

    override suspend fun insertClient(client: Client) {
        clientDao.insertClient(client.toEntity())
        clientsCollection.document(client.id).set(client).await()
    }

    override suspend fun insertClients(clients: List<Client>) {
        clientDao.insertClients(clients.map { it.toEntity() })
        clients.forEach { client ->
            clientsCollection.document(client.id).set(client).await()
        }
    }

    override suspend fun updateClient(client: Client) {
        clientDao.updateClient(client.toEntity())
        clientsCollection.document(client.id).set(client).await()
    }

    override suspend fun deleteClient(client: Client) {
        clientDao.deleteClient(client.toEntity())
        clientsCollection.document(client.id).delete().await()
    }

    override fun searchClients(query: String): Flow<List<Client>> =
        clientDao.searchClients(query).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun syncWithFirestore() {
        val remoteClients = clientsCollection.get().await()
            .documents.mapNotNull { it.toObject(Client::class.java) }
        clientDao.insertClients(remoteClients.map { it.toEntity() })
    }

    private fun ClientEntity.toDomain() = Client(
        id = id,
        name = name,
        phone = phone,
        email = email,
        address = if (addressStreet != null) {
            Address(
                street = addressStreet,
                number = addressNumber ?: "",
                city = addressCity ?: "",
                state = addressState ?: "",
                zipCode = addressZipCode ?: ""
            )
        } else null,
        preferredPaymentMethod = preferredPaymentMethod,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    private fun Client.toEntity() = ClientEntity(
        id = id,
        name = name,
        phone = phone,
        email = email,
        addressStreet = address?.street,
        addressNumber = address?.number,
        addressCity = address?.city,
        addressState = address?.state,
        addressZipCode = address?.zipCode,
        preferredPaymentMethod = preferredPaymentMethod,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
} 