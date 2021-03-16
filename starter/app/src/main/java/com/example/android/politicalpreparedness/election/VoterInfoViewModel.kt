package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.data.VoterInfoRepository
import com.example.android.politicalpreparedness.data.network.models.Division
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import kotlinx.coroutines.launch

class VoterInfoViewModel(private val repo: VoterInfoRepository) : ViewModel() {

    //TODO: Add live data to hold voter info
    private val _voterInfo = MutableLiveData<VoterInfoResponse?>()
    private val voterInfo: LiveData<VoterInfoResponse?>
        get() = _voterInfo

    //TODO: Add var and methods to populate voter info


    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status
    private val _following = MutableLiveData<Boolean>()
    val following: LiveData<Boolean>
        get() = _following

    fun load(electionId: Int, division: Division) {
        viewModelScope.launch {
            loadVoterInfo(division, electionId)
            loadFollowState(electionId)
        }
    }

    private suspend fun loadVoterInfo(division: Division, electionId: Int) {
        val address = "${division.state}, ${division.country}"
        when (val result = repo.getVoteInfo(address = address, electionId = electionId.toLong())) {
            is Result.Success -> _voterInfo.value = result.data
            is Result.Error -> {
            }
        }
    }

    private suspend fun loadFollowState(electionId: Int) {
        when (val result = repo.isFollowing(electionId)) {
            is Result.Success -> _following.value = result.data
            is Result.Error -> _following.value = false
        }
    }

    fun follow() {
        viewModelScope.launch {
            val voterInfo = _voterInfo.value ?: return@launch
            repo.follow(voterInfo.election)
            _following.value = true
        }
    }

    fun unfollow() {
        viewModelScope.launch {
            val voterInfo = _voterInfo.value ?: return@launch
            repo.unfollow(voterInfo.election.id)
            _following.value = false
        }
    }

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}