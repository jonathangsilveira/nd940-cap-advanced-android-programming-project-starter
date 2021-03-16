package com.example.android.politicalpreparedness.util

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Election>?) {
    val adapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(data)
}

@BindingAdapter("is_loading")
fun bindProgressBar(progressBar: ProgressBar, isLoading: Boolean?) {
    progressBar.visibility = if (isLoading == true) View.VISIBLE else View.GONE
}