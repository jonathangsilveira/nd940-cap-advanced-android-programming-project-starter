package com.example.android.politicalpreparedness.data.database

import androidx.room.*
import com.example.android.politicalpreparedness.data.network.models.Election

@Dao
interface ElectionDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(election: Election)

    @Query("SELECT * FROM election_table")
    suspend fun all(): List<Election>

    @Query("SELECT * FROM election_table WHERE id = :id")
    suspend fun getElectionById(id: Int): Election?

    @Transaction
    @Query("DELETE FROM election_table WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Transaction
    @Query("DELETE FROM election_table")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM election_table WHERE id = :id)")
    suspend fun exists(id: Int): Boolean

}