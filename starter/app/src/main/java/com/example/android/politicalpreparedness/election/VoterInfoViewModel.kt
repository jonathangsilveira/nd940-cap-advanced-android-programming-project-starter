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

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    private val _voterInfo = MutableLiveData<VoterInfoResponse?>()
    val voterInfo: LiveData<VoterInfoResponse?>
        get() = _voterInfo

    private val _url = MutableLiveData<String?>()
    val url: LiveData<String?>
        get() = _url

    private val _following = MutableLiveData<Boolean>()
    val following: LiveData<Boolean>
        get() = _following

    private val _votingLocationUrl = MutableLiveData<String?>()
    val votingLocationUrl: LiveData<String?>
        get() = _votingLocationUrl


    private val _ballotInfoUrl = MutableLiveData<String?>()
    val ballotInfoUrl: LiveData<String?>
        get() = _ballotInfoUrl

    fun load(args: VoterInfoFragmentArgs) {
        val electionId: Int = args.argElectionId
        val division: Division = args.argDivision
        loadVoterInfo(division, electionId)
        loadFollowState(electionId)
    }

    private fun loadVoterInfo(division: Division, electionId: Int) {
        val address = "${division.state}, ${division.country}"
        viewModelScope.launch {
            _loading.value = true
            when (val result = repo.getVoteInfo(address = address, electionId = electionId.toLong())) {
                is Result.Success -> {
                    val data = result.data
                    _voterInfo.value = data
                    _votingLocationUrl.value = data.votingLocationUrl()
                    _ballotInfoUrl.value = data.ballotInfoUrl()
                }
                is Result.Error -> { _error.value = result.message }
            }
            _loading.value = false
        }
    }

    private fun loadFollowState(electionId: Int) {
        viewModelScope.launch {
            when (val result = repo.isFollowing(electionId)) {
                is Result.Success -> _following.value = result.data
                is Result.Error -> _following.value = false
            }
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

    fun openBallotInformation() {
        _url.value = _voterInfo.value?.ballotInfoUrl()
    }

    fun openVotingLocation() {
        _url.value = _voterInfo.value?.votingLocationUrl()
    }

    private fun VoterInfoResponse.votingLocationUrl(): String? {
        return this.state?.firstOrNull()
                ?.electionAdministrationBody
                ?.votingLocationFinderUrl
    }

    private fun VoterInfoResponse.ballotInfoUrl(): String? {
        return this.state?.firstOrNull()
                ?.electionAdministrationBody
                ?.ballotInfoUrl
    }

}