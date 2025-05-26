package com.example.minstore.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.minstore.data.local.entities.ProviderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProviderDao {
    @Query("SELECT * FROM providers ORDER BY name ASC")
    fun getAllProviders(): Flow<List<ProviderEntity>>

    @Query("SELECT * FROM providers WHERE id = :id")
    suspend fun getProviderById(id: String): ProviderEntity?

    @Query("SELECT * FROM providers WHERE email = :email")
    suspend fun getProviderByEmail(email: String): ProviderEntity?

    @Query("SELECT * FROM providers WHERE phone = :phone")
    suspend fun getProviderByPhone(phone: String): ProviderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProvider(provider: ProviderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProviders(providers: List<ProviderEntity>)

    @Update
    suspend fun updateProvider(provider: ProviderEntity)

    @Delete
    suspend fun deleteProvider(provider: ProviderEntity)

    @Query(
        """
        SELECT * FROM providers 
        WHERE name LIKE '%' || :query || '%' 
        OR phone LIKE '%' || :query || '%' 
        OR email LIKE '%' || :query || '%'
        OR taxId LIKE '%' || :query || '%'
    """
    )
    fun searchProviders(query: String): Flow<List<ProviderEntity>>
} 