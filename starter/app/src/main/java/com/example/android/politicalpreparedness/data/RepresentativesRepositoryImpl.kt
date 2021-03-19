package com.example.android.politicalpreparedness.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepresentativesRepositoryImpl(
        private val dataSource: RepresentativeDataSource,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): RepresentativeRepository {

    override suspend fun getRepresentatives(address: Address): Result<List<Representative>> {
        return withContext(ioDispatcher) {
            try {
                val representatives = dataSource.get(address)
                Result.Success(representatives)
            } catch (e: Exception) {
                Result.Error(message = e.message)
            }
        }
    }

    override fun observableState(): LiveData<List<String>> = MutableLiveData(dataSource.states())

}