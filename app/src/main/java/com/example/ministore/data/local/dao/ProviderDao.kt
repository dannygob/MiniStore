package com.example.ministore.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ministore.data.local.entity.ProviderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProviderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProvider(provider: ProviderEntity)

    @Update
    suspend fun updateProvider(provider: ProviderEntity)

    @Delete
    suspend fun deleteProvider(provider: ProviderEntity)

    @Query("SELECT * FROM providers WHERE id = :id")
    fun getProviderById(id: String): Flow<ProviderEntity?>

    @Query("SELECT * FROM providers ORDER BY name ASC")
    fun getAllProviders(): Flow<List<ProviderEntity>>

    @Query("SELECT * FROM providers WHERE isActive = 1 ORDER BY name ASC")
    fun getActiveProviders(): Flow<List<ProviderEntity>>

    @Query("SELECT * FROM providers WHERE name LIKE '%' || :query || '%' OR contactPerson LIKE '%' || :query || '%'")
    fun searchProviders(query: String): Flow<List<ProviderEntity>>

    @Query("UPDATE providers SET isActive = :isActive WHERE id = :providerId")
    suspend fun updateProviderActiveStatus(providerId: String, isActive: Boolean)
} 