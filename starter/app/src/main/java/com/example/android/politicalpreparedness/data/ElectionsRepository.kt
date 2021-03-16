package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.data.network.models.Election

interface ElectionsRepository {
    suspend fun upcomingElections(): Result<List<Election>>
    suspend fun savedElections(): Result<List<Election>>
    suspend fun clear(): Result<Unit>
    suspend fun getElectionById(id: Int): Result<Election>
}