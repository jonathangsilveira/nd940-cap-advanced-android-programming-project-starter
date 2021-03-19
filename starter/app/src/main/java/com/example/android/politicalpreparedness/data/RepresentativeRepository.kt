package com.example.android.politicalpreparedness.data

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative

interface RepresentativeRepository {
    suspend fun getRepresentatives(address: Address): Result<List<Representative>>
    fun observableState(): LiveData<List<String>>
}