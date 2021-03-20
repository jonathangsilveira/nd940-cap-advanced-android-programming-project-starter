package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.ElectionsRepository
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.data.network.models.Election
import kotlinx.coroutines.launch

class ElectionsViewModel(private val repository: ElectionsRepository): ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    private val _upcoming = MutableLiveData<List<Election>>()
    val upcoming: LiveData<List<Election>>
        get() = _upcoming

    private val _saved = MutableLiveData<List<Election>>()
    val saved: LiveData<List<Election>>
        get() = _saved

    fun fetchUpcoming() {
        viewModelScope.launch {
            _loading.value = true
            when (val result = repository.upcomingElections()) {
                is Result.Success -> _upcoming.value = result.data
                is Result.Error -> _error.value = result.message
            }
            _loading.value = false
        }
    }

    fun loadSaved() {
        viewModelScope.launch {
            when (val result = repository.savedElections()) {
                is Result.Success -> _saved.value = result.data
                is Result.Error -> _error.value = result.message
            }
        }
    }

}