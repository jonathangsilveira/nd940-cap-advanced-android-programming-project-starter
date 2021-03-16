package com.example.android.politicalpreparedness.data

import android.app.RemoteAction
import android.content.Context
import com.example.android.politicalpreparedness.data.database.ElectionDatabase
import com.example.android.politicalpreparedness.data.database.LocalElectionsDataSource
import com.example.android.politicalpreparedness.data.network.CivicsApi
import com.example.android.politicalpreparedness.data.network.CivicsApiService
import com.example.android.politicalpreparedness.data.network.RemoteElectionsDataSource
import com.example.android.politicalpreparedness.data.network.RemoteVoterInfoDataSource

object ServiceLocator {

    private var database: ElectionDatabase? = null

    fun provideDatabase(context: Context): ElectionDatabase {
        return database ?: createDatabase(context)
    }

    private fun createDatabase(context: Context): ElectionDatabase {
        val database = ElectionDatabase.getInstance(context)
        this.database = database
        return database
    }


    fun provideElectionsRepository(context: Context): ElectionsRepository {
        val dao = provideDatabase(context).electionDao
        val webservice = CivicsApi.retrofitService
        return ElectionsRepositoryImpl(
                LocalElectionsDataSource(dao),
                RemoteElectionsDataSource(webservice)
        )
    }

    fun provideVoterInfoRepository(context: Context): VoterInfoRepository {
        val dao = provideDatabase(context).electionDao
        val webservice = CivicsApi.retrofitService
        return VoterInfoRepositoryImpl(
                LocalElectionsDataSource(dao),
                RemoteVoterInfoDataSource(webservice)
        )
    }

}