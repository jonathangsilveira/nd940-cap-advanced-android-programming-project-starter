package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VoterInfoRepositoryImpl(
        private val localElectionsDataSource: ElectionsDataSource,
        private val remoteVoterInfoDataSource: VoterInfoDataSource,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): VoterInfoRepository {
    override suspend fun follow(election: Election): Result<Unit> = withContext(ioDispatcher) {
        try {
            localElectionsDataSource.save(election)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(message = e.message)
        }
    }

    override suspend fun unfollow(electionId: Int): Result<Unit> = withContext(ioDispatcher) {
        try {
            localElectionsDataSource.delete(electionId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(message = e.message)
        }
    }

    override suspend fun getVoteInfo(address: String, electionId: Long): Result<VoterInfoResponse> {
        return withContext(ioDispatcher) {
            try {
                val response = remoteVoterInfoDataSource.fetch(address, electionId)
                Result.Success(response)
            } catch (e: Exception) {
                Result.Error(message = e.message)
            }
        }
    }

    override suspend fun isFollowing(electionId: Int): Result<Boolean> {
        return withContext(ioDispatcher) {
            try {
                val isFollowing = localElectionsDataSource.exists(electionId)
                Result.Success(isFollowing)
            } catch (e: Exception) {
                Result.Error(message = e.message)
            }
        }
    }
}