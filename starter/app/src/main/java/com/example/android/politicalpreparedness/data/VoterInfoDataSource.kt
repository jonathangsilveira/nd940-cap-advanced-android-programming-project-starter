package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse

interface VoterInfoDataSource {
    suspend fun fetch(address: String, electionId: Long): VoterInfoResponse
}