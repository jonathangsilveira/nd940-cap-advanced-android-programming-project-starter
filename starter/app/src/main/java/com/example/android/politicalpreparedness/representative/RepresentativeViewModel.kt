package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.*
import com.example.android.politicalpreparedness.data.RepresentativeRepository
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(private val repo: RepresentativeRepository): ViewModel() {

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    val states: LiveData<List<String>> = repo.observableState()

    fun updateAddress(address: Address) {
        _address.value = address
        loadRepresentatives(address)
    }

    fun validateAndApplyAddress(
            line1: String?,
            line2: String?,
            city: String?,
            state: String?,
            zip: String?
    ) {
        if (line1.isNullOrEmpty() ||
                city.isNullOrEmpty() ||
                state.isNullOrEmpty() ||
                zip.isNullOrEmpty()
        ) {
            return
        }
        updateAddress(
                createAddress(line1, line2, city, state, zip)
        )
    }



    fun createAddress(
            line1: String,
            line2: String?,
            city: String,
            state: String,
            zip: String
    ): Address = Address(line1, line2, city, state, zip)

    private fun loadRepresentatives(address: Address) {
        viewModelScope.launch {
            when (val result = repo.getRepresentatives(address)) {
                is Result.Success -> _representatives.value = result.data
                is Result.Error -> {}
            }
        }
    }

    class Factory(private val repo: RepresentativeRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ElectionsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RepresentativeViewModel(repo) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}
