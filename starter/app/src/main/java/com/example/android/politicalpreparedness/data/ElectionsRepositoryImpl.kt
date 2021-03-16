package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.data.network.models.Election
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionsRepositoryImpl(
        private val localDataSource: ElectionsDataSource,
        private val remoteDataSource: ElectionsDataSource,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ElectionsRepository {

    override suspend fun upcomingElections(): Result<List<Election>> = withContext(ioDispatcher) {
        try {
            val elections = remoteDataSource.getElections()
            Result.Success(elections)
        } catch (e: Exception) {
            Result.Error(message = e.message)
        }
    }

    override suspend fun savedElections(): Result<List<Election>> = withContext(ioDispatcher) {
        try {
            val elections = localDataSource.getElections()
            Result.Success(elections)
        } catch (e: Exception) {
            Result.Error(message = e.message)
        }
    }

    override suspend fun follow(election: Election): Result<Unit> = withContext(ioDispatcher) {
        try {
            localDataSource.save(election)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(message = e.message)
        }
    }

    override suspend fun unfollow(id: Int): Result<Unit> = withContext(ioDispatcher) {
        try {
            localDataSource.delete(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(message = e.message)
        }
    }

    override suspend fun clear(): Result<Unit> {
        return try {
            localDataSource.deleteAll()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(message = e.message)
        }
    }

    override suspend fun getElectionById(id: Int): Result<Election> {
        return try {
            val election = localDataSource.getById(id)
            if (election != null)
                Result.Success(election)
            else
                Result.Error(message = "No Election Found!")
        } catch (e: Exception) {
            Result.Error(message = e.message)
        }
    }

}