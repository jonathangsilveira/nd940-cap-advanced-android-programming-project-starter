package com.example.android.politicalpreparedness.data.database

import com.example.android.politicalpreparedness.data.ElectionsDataSource
import com.example.android.politicalpreparedness.data.network.models.Election

class LocalElectionsDataSource(private val dao: ElectionDao): ElectionsDataSource {

    override suspend fun getElections(): List<Election> {
        return dao.all()
    }

    override suspend fun save(election: Election) {
        dao.insertOrUpdate(election)
    }

    override suspend fun delete(id: Int) {
        dao.deleteById(id)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    override suspend fun getById(id: Int): Election? {
        return dao.getElectionById(id)
    }

    override suspend fun exists(id: Int): Boolean = dao.exists(id)

}