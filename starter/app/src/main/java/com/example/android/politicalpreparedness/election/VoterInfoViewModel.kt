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

    private val _voterInfo = MutableLiveData<VoterInfoResponse?>()
    val voterInfo: LiveData<VoterInfoResponse?>
        get() = _voterInfo

    //TODO: Add var and methods to support loading URLs

    private val _following = MutableLiveData<Boolean>()
    val following: LiveData<Boolean>
        get() = _following

    fun load(args: VoterInfoFragmentArgs) {
        val electionId: Int = args.argElectionId
        val division: Division = args.argDivision
        viewModelScope.launch {
            loadVoterInfo(division, electionId)
            loadFollowState(electionId)
        }
    }

    private suspend fun loadVoterInfo(division: Division, electionId: Int) {
        val address = "${division.state}, ${division.country}"
        when (val result = repo.getVoteInfo(address = address, electionId = electionId.toLong())) {
            is Result.Success -> _voterInfo.value = result.data
            is Result.Error -> {  }
        }
    }

    private suspend fun loadFollowState(electionId: Int) {
        when (val result = repo.isFollowing(electionId)) {
            is Result.Success -> _following.value = result.data
            is Result.Error -> _following.value = false
        }
    }

    private suspend fun follow() {
        val voterInfo = _voterInfo.value ?: return
        repo.follow(voterInfo.election)
        _following.value = true
    }

    private suspend fun unfollow() {
        val voterInfo = _voterInfo.value ?: return
        repo.unfollow(voterInfo.election.id)
        _following.value = false
    }

    fun toggleFollow() {
        viewModelScope.launch {
            val following = following.value ?: false
            if (following)
                unfollow()
            else
                follow()
        }
    }

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}