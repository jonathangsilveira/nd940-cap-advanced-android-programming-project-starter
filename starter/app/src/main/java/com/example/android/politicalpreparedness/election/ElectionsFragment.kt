package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.data.ServiceLocator
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.google.android.material.snackbar.Snackbar

class ElectionsFragment: Fragment() {

    private val _viewModel: ElectionsViewModel by viewModels {
        val repo = ServiceLocator.provideElectionsRepository(requireContext())
        ElectionsViewModelFactory(repo)
    }

    private lateinit var binding: FragmentElectionBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = _viewModel
        binding.upcomingRecyclerView.adapter = ElectionListAdapter(::navigateToVoteInfo)
        binding.savedRecyclerView.adapter = ElectionListAdapter(::navigateToVoteInfo)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewModel.error.observe(viewLifecycleOwner, ::onError)
        _viewModel.fetchUpcoming()
        _viewModel.loadSaved()
    }

    private fun navigateToVoteInfo(election: Election) {
        val direction = ElectionsFragmentDirections
                .actionElectionsFragmentToVoterInfoFragment(election.id, election.division)
        findNavController().navigate(direction)
    }

    private fun onError(message: String?) {
        Snackbar.make(binding.root, message.orEmpty(), Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok) {}
                .show()
    }

}