package com.example.ministore.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.minstore.data.local.entities.ClientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {
    @Query("SELECT * FROM clients ORDER BY name ASC")
    fun getAllClients(): Flow<List<ClientEntity>>

    @Query("SELECT * FROM clients WHERE id = :id")
    suspend fun getClientById(id: String): ClientEntity?

    @Query("SELECT * FROM clients WHERE email = :email")
    suspend fun getClientByEmail(email: String): ClientEntity?

    @Query("SELECT * FROM clients WHERE phone = :phone")
    suspend fun getClientByPhone(phone: String): ClientEntity?

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertClient(client: ClientEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertClients(clients: List<ClientEntity>)

    @Update
    suspend fun updateClient(client: ClientEntity)

    @Delete
    suspend fun deleteClient(client: ClientEntity)

    @Query(
        """
        SELECT * FROM clients 
        WHERE name LIKE '%' || :query || '%' 
        OR phone LIKE '%' || :query || '%' 
        OR email LIKE '%' || :query || '%'
    """
    )
    fun searchClients(query: String): Flow<List<ClientEntity>>
}