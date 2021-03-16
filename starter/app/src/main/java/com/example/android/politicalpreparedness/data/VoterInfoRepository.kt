package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse

interface VoterInfoRepository {
    suspend fun follow(election: Election): Result<Unit>
    suspend fun unfollow(electionId: Int): Result<Unit>
    suspend fun getVoteInfo(address: String, electionId: Long): Result<VoterInfoResponse>
    suspend fun isFollowing(electionId: Int): Result<Boolean>
}