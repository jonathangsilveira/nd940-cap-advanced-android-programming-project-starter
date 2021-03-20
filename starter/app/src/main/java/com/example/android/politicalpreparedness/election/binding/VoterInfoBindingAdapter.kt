package com.example.android.politicalpreparedness.election.binding

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import java.text.DateFormat
import java.util.*

@BindingAdapter("electionName")
fun bindVoterInfoElectionName(view: TextView, data: VoterInfoResponse?) {
    val election = data?.election
    view.text = election?.name
}

@BindingAdapter("electionDate")
fun bindVoterInfoElectionDate(view: TextView, data: VoterInfoResponse?) {
    val dateFormat = DateFormat.getDateTimeInstance()
            .apply {
                timeZone = TimeZone.getTimeZone("GMT")
            }
    val electionDay = data?.election?.electionDay
    view.text = if (electionDay != null)
        dateFormat.format(electionDay)
    else
        ""
}

@BindingAdapter("isFollowing")
fun bindVoterInfoFollowing(button: Button, data: Boolean?) {
    val isFollowing = data == true
    val textResId = if (isFollowing)
        R.string.unfollow_election
    else
        R.string.follow_election
    button.setText(textResId)
}

@BindingAdapter("voterInfoAddress")
fun bindVoterInfoAddress(view: TextView, data: VoterInfoResponse?) {
    val address = data?.state?.firstOrNull()
            ?.electionAdministrationBody
            ?.correspondenceAddress
            ?.toFormattedString()
    view.visibility = if (address.isNullOrEmpty())
        View.GONE
    else
        View.VISIBLE
    view.text = address
}

@BindingAdapter("voterInfoVotingLocation")
fun bindVoterInfoVotingLocation(view: TextView, value: String?) {
    val hasValue = !value.isNullOrEmpty()
    view.visibility = if (hasValue) View.VISIBLE else View.GONE
}

@BindingAdapter("voterInfoBallotInfo")
fun bindVoterInfoBallotInfo(view: TextView, value: String?) {
    val hasValue = !value.isNullOrEmpty()
    view.visibility = if (hasValue) View.VISIBLE else View.GONE
}

@BindingAdapter("voterInfoAddressVisibility")
fun bindVoterInfoAddressVisibility(view: View, data: VoterInfoResponse?) {
    val election = data?.election
    view.visibility = if (election?.name.isNullOrEmpty())
        View.GONE
    else
        View.VISIBLE
}