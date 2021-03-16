package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.politicalpreparedness.data.ServiceLocator
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    private val _viewModel: VoterInfoViewModel by viewModels {
        val repo = ServiceLocator.provideVoterInfoRepository(requireContext())
        VoterInfoViewModelFactory(repo)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val binding = FragmentVoterInfoBinding.inflate(inflater)
        //TODO: Add binding values
        binding.lifecycleOwner = this
        binding.viewModel = _viewModel

        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */


        //TODO: Handle loading of URLs

        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks
        return binding.root
    }

    //TODO: Create method to load URL intents

}