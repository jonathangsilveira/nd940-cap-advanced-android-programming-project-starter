package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative

interface RepresentativeDataSource {
    suspend fun get(address: Address): List<Representative>
    fun states(): List<String>
}