package com.example.android.politicalpreparedness.data.network

import com.example.android.politicalpreparedness.data.ElectionsDataSource
import com.example.android.politicalpreparedness.data.network.models.Election

class RemoteElectionsDataSource(private val webservice: CivicsApiService): ElectionsDataSource {

    override suspend fun getElections(): List<Election> {
        return webservice.elections().elections
    }

    override suspend fun save(election: Election) {  }

    override suspend fun delete(id: Int) {  }

    override suspend fun deleteAll() {  }

    override suspend fun getById(id: Int): Election? {
        return null
    }

    override suspend fun exists(id: Int): Boolean = false

}