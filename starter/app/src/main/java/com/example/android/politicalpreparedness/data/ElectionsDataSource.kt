package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.data.network.models.Election

interface ElectionsDataSource {
    suspend fun getElections(): List<Election>
    suspend fun save(election: Election)
    suspend fun delete(id: Int)
    suspend fun deleteAll()
    suspend fun getById(id: Int): Election?
    suspend fun exists(id: Int): Boolean
}