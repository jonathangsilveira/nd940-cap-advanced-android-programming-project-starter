package com.example.android.politicalpreparedness.data.network

import com.example.android.politicalpreparedness.data.VoterInfoDataSource
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse

class RemoteVoterInfoDataSource(private val webservice : CivicsApiService): VoterInfoDataSource {
    override suspend fun fetch(address: String, electionId: Long): VoterInfoResponse {
        return webservice.voterInfo(address, electionId)
    }
}