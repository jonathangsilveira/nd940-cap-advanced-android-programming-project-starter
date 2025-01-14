package com.example.android.politicalpreparedness.representative.adapter

import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.representative.model.Representative

@BindingAdapter("profileImage")
fun fetchImage(view: ImageView, src: String?) {
    src?.let {
        val uri = src.toUri().buildUpon().scheme("https").build()
        val options = RequestOptions()
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
        Glide.with(view.context)
                .setDefaultRequestOptions(options)
                .load(uri)
                .into(view)
    }
}

@BindingAdapter("stateValue")
fun Spinner.setNewValue(value: String?) {
    val adapter = toTypedAdapter<String>(this.adapter as ArrayAdapter<*>)
    if (adapter.isEmpty) return
    val position = when (adapter.getItem(0)) {
        is String -> adapter.getPosition(value)
        else -> this.selectedItemPosition
    }
    if (position >= 0) {
        setSelection(position)
    }
}

@BindingAdapter("states")
fun Spinner.setStates(states: List<String>?) {
    val adapter = toTypedAdapter<String>(this.adapter as ArrayAdapter<*>)
    adapter.clear()
    states?.forEach { adapter.add(it) }
}

@BindingAdapter("representatives")
fun RecyclerView.setRepresentatives(representatives: List<Representative>?) {
    val representativeAdapter = adapter as RepresentativeListAdapter?
    representativeAdapter?.submitList(representatives)
}

inline fun <reified T> toTypedAdapter(adapter: ArrayAdapter<*>): ArrayAdapter<T>{
    return adapter as ArrayAdapter<T>
}
