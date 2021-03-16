package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.data.ServiceLocator
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter

class ElectionsFragment: Fragment() {

    private val _viewModel: ElectionsViewModel by viewModels {
        val repo = ServiceLocator.provideElectionsRepository(requireContext())
        ElectionsViewModelFactory(repo)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = _viewModel
        binding.upcomingRecyclerView.adapter = ElectionListAdapter(::navigateToVoteInfo)
        binding.savedRecyclerView.adapter = ElectionListAdapter(::navigateToVoteInfo)
        return binding.root
    }

    private fun navigateToVoteInfo(election: Election) {
        val direction = ElectionsFragmentDirections
                .actionElectionsFragmentToVoterInfoFragment(election.id, election.division)
        findNavController().navigate(direction)
    }

}